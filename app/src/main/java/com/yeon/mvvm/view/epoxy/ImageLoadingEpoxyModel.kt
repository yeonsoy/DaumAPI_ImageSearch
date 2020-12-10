package com.yeon.mvvm.view.epoxy

import android.content.Context
import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.yeon.mvvm.R

@EpoxyModelClass(layout = R.layout.view_loading)
abstract class ImageLoadingEpoxyModel : EpoxyModelWithHolder<ImageLoadingEpoxyModel.Holder>() {

    class Holder : EpoxyHolder() {
        override fun bindView(itemView: View) {
            val context = itemView.rootView.context
            val layoutParams = itemView.layoutParams
            layoutParams.width = getWidth(context)
        }

        private fun getWidth(context: Context): Int {
            val dm = context.applicationContext.resources.displayMetrics
            return dm.widthPixels
        }
    }
}
