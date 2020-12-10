package com.yeon.mvvm.view.epoxy

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.yeon.mvvm.R

@EpoxyModelClass(layout = R.layout.view_error)
abstract class ImageErrorEpoxyModel : EpoxyModelWithHolder<ImageErrorEpoxyModel.Holder>() {

    @EpoxyAttribute
    var errorStr: String? = null

    @EpoxyAttribute
    lateinit var listener: () -> Unit

    override fun bind(holder: Holder) {
        holder.errorTextView.text = errorStr
        holder.retryButton.setOnClickListener { listener() }
    }

    class Holder : EpoxyHolder() {
        lateinit var errorTextView: TextView
        lateinit var retryButton: Button
        override fun bindView(itemView: View) {
            val context = itemView.rootView.context
            val layoutParams = itemView.layoutParams
            layoutParams.width = getWidth(context)
            errorTextView = itemView.findViewById(R.id.tv_error)
            retryButton = itemView.findViewById(R.id.btnRetry)
        }

        private fun getWidth(context: Context): Int {
            val dm = context.applicationContext.resources.displayMetrics
            return dm.widthPixels
        }
    }
}
