package com.yeon.imagesearch.page

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yeon.imagesearch.GlideApp
import com.yeon.imagesearch.R
import com.yeon.imagesearch.util.Documents
import kotlinx.android.synthetic.main.item_image.view.*


class RecyclerAdapter(val imageList: MutableList<Documents>, private val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
        ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)
            return ViewHolder(inflatedView)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bindTo(imageList[position], context)
    }

    override fun getItemCount() = imageList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindTo(imageItem: Documents, context: Context) {
            val imageView = itemView.item_image_view

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val imageViewLayoutParams = imageView.layoutParams
            imageViewLayoutParams.width = getWidth(context) / 3
            imageViewLayoutParams.height = getRatioHeight(
                context,
                imageItem.hegiht,
                imageItem.width
            ) / 3
            imageView.layoutParams = imageViewLayoutParams

            GlideApp.with(context)
                .load(imageItem.image_url)
                .placeholder(circularProgressDrawable)
                .error(Glide.with(context)
                        .load(R.drawable.ic_placeholder)
                        .override(imageItem.width, imageItem.hegiht)
                        .centerCrop())
                .into(imageView)
        }

        fun getWidth(context: Context): Int {
            val dm = context.applicationContext.resources.displayMetrics
            return dm.widthPixels
        }

        fun getRatioHeight(context: Context, originHeight: Int, originWidth: Int) : Int{
            return (originHeight.toFloat() * getWidth(context) / originWidth.toFloat()).toInt()
        }
    }

}