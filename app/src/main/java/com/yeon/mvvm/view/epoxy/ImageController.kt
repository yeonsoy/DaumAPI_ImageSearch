package com.yeon.mvvm.view.epoxy

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.airbnb.epoxy.EpoxyController
import com.yeon.mvvm.model.Documents
import com.yeon.mvvm.view.ImageDetailActivity
import com.yeon.mvvm.viewmodel.ImageViewModel
import java.util.*

class ImageController(private val context: Context, private val viewModel: ImageViewModel) :
    EpoxyController() {
    private var data: MutableList<Documents> = Collections.emptyList()
    private var loadingMore = true

    fun setLoadingMore(loadingMore: Boolean) {
        this.loadingMore = loadingMore
        // requestModelBuild()
    }

    fun setData(data: List<Documents>?) {
        data?.let {
            this.data = data as MutableList<Documents>
            requestModelBuild()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun buildModels() {
        data?.forEachIndexed { index, s ->
            imageItemEpoxyHolder {
                id(index)
                data(s)
                spanSizeOverride { _, _, _ -> 3 } // 이미지 한 장이 몇 칸을 차지하는 지
                listener {
                    context.startActivity(
                        Intent(
                            context,
                            ImageDetailActivity::class.java
                        ).putExtra("item", s)
                    )
                }
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ImageLoadingEpoxyModel_().apply {
                id("loading")
                spanSizeOverride { _, _, _ -> 3 }
            }
                .addIf(context.isInternetAvailable() && loadingMore, this)

            ImageErrorEpoxyModel_().apply {
                id("error")
                errorStr("Network Error")
                spanSizeOverride { _, _, _ -> 3 }
                listener {
                    // api 다시 요청
                    viewModel.fetchData(
                        viewModel.query!!,
                        viewModel.sort,
                        viewModel.page,
                        viewModel.size,
                        this@ImageController
                    )
                }
            }
                .addIf(!context.isInternetAvailable(), this)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun Context.isInternetAvailable() =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }


    private fun delete(position: Int, item: Documents) {
        // this.data.remove(item)
        this.data.removeAt(position)
        requestModelBuild()
    }
}