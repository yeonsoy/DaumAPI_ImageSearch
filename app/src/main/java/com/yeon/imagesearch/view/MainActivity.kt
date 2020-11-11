package com.yeon.imagesearch.view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View
import com.yeon.imagesearch.model.ImageModel
import com.yeon.imagesearch.api.Status
import com.yeon.imagesearch.viewmodel.ImageViewModel
import com.jakewharton.rxbinding2.widget.RxTextView
import com.yeon.imagesearch.R
import com.yeon.imagesearch.view.adapter.GridSpacingItemDecoration
import com.yeon.imagesearch.view.adapter.RecyclerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : BaseViewModelActivity<ImageViewModel>(), ImageViewModel.ImageViewModelInterface {

    private lateinit var adapter: RecyclerAdapter

    override fun viewModel(): ImageViewModel {
        val factory = ImageViewModel.ImageViewModelFactory(application, this)
        return ViewModelProviders.of(this, factory)
                .get<ImageViewModel>(ImageViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setMessageTextSetting(getString(R.string.input_text))
        adapterInit()

        mViewModel?.dataLayoutSubject?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { visibility ->
                    if (visibility) {
                        tv_msg.visibility = View.VISIBLE
                        rv_list.visibility = View.GONE
                    } else {
                        tv_msg.visibility = View.GONE
                        rv_list.visibility = View.VISIBLE
                    }
                }

        RxTextView.textChanges(et_query)
                .throttleLast(1, TimeUnit.SECONDS,AndroidSchedulers.mainThread())
                .subscribe(
                        { text ->
                           if (text.isNotEmpty()) {
                                removeDisposable("list")
                                mViewModel?.getImagesPaging(text.toString(), "accuracy")
                                mViewModel?.dataLayoutSubject?.onNext(false)
                            } else {
                                setMessageTextSetting(getString(R.string.input_text))
                                mViewModel?.dataLayoutSubject?.onNext(true)
                            }
                        }
                ) { th -> run { Log.e("textChanges", th.message!!) } }
    }

    private fun adapterInit() {
        adapter = RecyclerAdapter(this) {
            mViewModel?.retry()
        }
        setStaggeredSetting()

        rv_list.adapter = adapter
    }

    private fun setStaggeredSetting() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, 1)
        staggeredGridLayoutManager.orientation = StaggeredGridLayoutManager.VERTICAL
        rv_list.layoutManager = staggeredGridLayoutManager
        rv_list.addItemDecoration(GridSpacingItemDecoration(3, 0, true))
    }

    override fun getImages(items: LiveData<PagedList<ImageModel.Documents>>) {
        if (::adapter.isInitialized) {
            mViewModel?.netWorkState?.observe(this, Observer { it ->
                run {
                    if (it?.status == Status.FAILED) {
                        setMessageTextSetting(it.message!!)
                        mViewModel?.dataLayoutSubject?.onNext(true)
                    }
                }
            })

            mViewModel?.dataState?.observe(this, Observer { it ->
                run {
                    if (it == true) {
                        setMessageTextSetting(getString(R.string.no_result))
                        mViewModel?.dataLayoutSubject?.onNext(true)
                    }
                }
            })
            items.observe(this, Observer { it ->
                run {
                    adapter.submitList(it)
                    mViewModel?.dataLayoutSubject?.onNext(false)

                }
            })


        }
    }

    private fun setMessageTextSetting(msg: String) {
        tv_msg.text = msg
    }


    override fun putDisposableMap(tag: String, disposable: Disposable) {
        super.putDisposableMap(tag, disposable)
    }


}
