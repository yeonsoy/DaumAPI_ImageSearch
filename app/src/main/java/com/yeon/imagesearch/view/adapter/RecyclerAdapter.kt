package com.yeon.imagesearch.view.adapter

import android.annotation.SuppressLint
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.content.Intent
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yeon.imagesearch.R
import com.yeon.imagesearch.api.NetworkState
import com.yeon.imagesearch.api.RetrofitManager
import com.yeon.imagesearch.api.Status
import com.yeon.imagesearch.model.ImageModel
import com.yeon.imagesearch.view.ImageDetailActivity
import kotlinx.android.synthetic.main.item_network_state.view.*

/** Displays {@link com.bumptech.glide.samples.gallery.MediaStoreData} in a recycler view. */
class RecyclerAdapter(private val context: Context, private val retryCallback: () -> Unit) : PagedListAdapter<ImageModel.Documents, RecyclerView.ViewHolder>(ImageDiffCallback) {
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_image -> ImageItemViewHolder.create(parent)
            R.layout.item_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_image -> (holder as ImageItemViewHolder).bindTo(context, getItem(position))
            R.layout.item_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_image
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.networkState
                val hadExtraRow = hasExtraRow()
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    companion object {
        val ImageDiffCallback = object : DiffUtil.ItemCallback<ImageModel.Documents>() {

            override fun areItemsTheSame(oldItem: ImageModel.Documents, newItem: ImageModel.Documents): Boolean {
                return oldItem.datetime == newItem.datetime
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ImageModel.Documents, newItem: ImageModel.Documents): Boolean {
                return oldItem == newItem
            }
        }
    }

    class NetworkStateViewHolder(val view: View, private val retryCallback: () -> Unit) : RecyclerView.ViewHolder(view) {

        init {
            itemView.retryLoadingButton.setOnClickListener { retryCallback() }
        }

        fun bindTo(networkState: NetworkState?) {
            itemView.errorMessageTextView.visibility = if (networkState?.message != null) View.VISIBLE else View.GONE
            if (networkState?.message != null) {
                itemView.errorMessageTextView.text = networkState.message
            }

            itemView.retryLoadingButton.visibility = if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
            itemView.loadingProgressBar.visibility = if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE
        }

        companion object {
            fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
                return NetworkStateViewHolder(view, retryCallback)
            }
        }

    }

    class ImageItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindTo(context: Context, imageDoc: ImageModel.Documents?) {

            val imageView = itemView.findViewById<ImageView>(R.id.my_image_view)

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(context)
                    .load(imageDoc?.image_url)
                    .thumbnail(
                            Glide.with(context)
                                    .load(imageDoc?.thumbnail_url)
                                    .override(imageDoc?.width?.toInt()!!, imageDoc?.height?.toInt()!!)
                                    .centerCrop())
                    .placeholder(circularProgressDrawable)
                    .into(imageView)

            val ratioHeight = RetrofitManager.getRatioHeight(context, imageDoc.height?.toInt(), imageDoc?.width?.toInt())
            val imageViewLayoutParams = imageView.layoutParams
            imageViewLayoutParams.width = RetrofitManager.getWidth(context) / 3
            imageViewLayoutParams.height = ratioHeight / 3
            imageView.layoutParams = imageViewLayoutParams

            imageView.setOnClickListener { _: View? -> context.startActivity( Intent(context, ImageDetailActivity::class.java).putExtra("item", imageDoc)) }
        }

        companion object {
            fun create(parent: ViewGroup): ImageItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_image, parent, false)
                return ImageItemViewHolder(view)
            }
        }

    }

}