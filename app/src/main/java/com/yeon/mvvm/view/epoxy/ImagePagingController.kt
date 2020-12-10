package com.yeon.mvvm.view.epoxy

import android.content.Context
import android.content.Intent
import android.util.Log
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.yeon.mvvm.model.Documents
import com.yeon.mvvm.view.ImageDetailActivity

class ImagePagingController(private val context: Context) : PagedListEpoxyController<Documents>() {
    var isLoading = false
        set(value) {
            if (field) {
                field = value
                requestModelBuild()
            }
        }

    override fun buildItemModel(currentPosition: Int, item: Documents?): EpoxyModel<*> {
        return if (item == null) {
            //Loading View Model
            return ImageLoadingEpoxyModel_().apply {
                id("loading")
            }
        } else {
            //Image Item View Model
            return ImageItemEpoxyHolder_().apply {
                id("image${currentPosition}")
                data(item)
                spanSizeOverride { _, _, _ -> 1 } // 이미지 한 장이 몇 칸을 차지하는 지
                listener {
                    context.startActivity(
                        Intent(context, ImageDetailActivity::class.java)
                            .putExtra("item", item)
                    )
                }
            }
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        if (isLoading) {
            super.addModels(
                models.plus(
                    //Error View Model
                    ImageErrorEpoxyModel_()
                        .id("error")
                ).distinct()
            )
        } else {
            super.addModels(models.distinct())
        }
    }

    override fun onExceptionSwallowed(exception: RuntimeException) {

    }

    private fun delete(position: Int, item: Documents)
    {
        Log.d("check ", "delete: " + adapter.getModelAtPosition(position))
    }
}