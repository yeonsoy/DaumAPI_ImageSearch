package com.yeon.mvvm.view.epoxy

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil.bind
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.yeon.mvvm.R
import com.yeon.mvvm.model.Documents

@EpoxyModelClass(layout = R.layout.item_image)
abstract class ImageItemEpoxyHolder : EpoxyModelWithHolder<ImageItemEpoxyHolder.Holder>() {

    @EpoxyAttribute
    lateinit var data: Documents

    @EpoxyAttribute
    lateinit var listener: () -> Unit

    @EpoxyAttribute
    var divide = 3

    override fun bind(holder: Holder) {
        val draweeViewLayoutParams = holder.ivImage.layoutParams
        draweeViewLayoutParams.width = getWidth(holder.root.context) / divide
        draweeViewLayoutParams.height = draweeViewLayoutParams.width
        holder.ivImage.layoutParams = draweeViewLayoutParams
        holder.ivImage.setImageURI(Uri.parse(data.image_url))
        holder.ivImage.setOnClickListener { listener() }
    }

    override fun unbind(holder: Holder) {
    }

    private fun getWidth(context: Context): Int {
        val dm = context.applicationContext.resources.displayMetrics
        return dm.widthPixels
    }

    private fun getHeight(context: Context): Int {
        val dm = context.applicationContext.resources.displayMetrics
        return dm.heightPixels
    }

    private fun getRatioHeight(width: Int, originHeight: Int, originWidth: Int): Int {
        return (originHeight.toFloat() * width / originWidth.toFloat()).toInt()
    }

    class Holder : BaseEpoxyHolder() {
        // val context = itemView.context by bind()
        val ivImage: ImageView by bind(R.id.ivImage)
        val root: LinearLayout by bind(R.id.root)
    }

}