package com.yeon.mvvm.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.yeon.mvvm.model.Documents
import com.yeon.mvvm.viewmodel.ImageViewModel
import com.yeon.mvvm.R
import com.yeon.mvvm.view.epoxy.ImageController
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var keywords: String = "test"
    private var page = 1
    private var page_count = 30
    private val viewModel: ImageViewModel by viewModel()
    private val viewController: ImageController = ImageController(requireContext(), viewModel)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // viewModel = ViewModelProvider(this).get(ImageViewModel::class.java) DI 사용으로 생성 불필요

        search_query.addTextChangedListener(object: TextWatcher {
            var timer = Timer()
                override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    timer.cancel()
                    timer = Timer()
                    page = 0
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            viewModel.getImageList(s.toString(), "accuracy", page, page_count)
                        }
                    }, 1000)
                }

            })

        viewModel.inputImage.observe(viewLifecycleOwner, Observer<ArrayList<Documents>> {
            viewController.setData(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvEpoxy: RecyclerView = view.findViewById(R.id.rvEpoxy)

        val spanCount = 3
        val layoutManager = StaggeredGridLayoutManager(spanCount, 1)
        viewController.spanCount = spanCount
        //layoutManager.spanSizeLookup = viewController.spanSizeLookup
        rvEpoxy.layoutManager = layoutManager

        rvEpoxy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !viewModel.is_end) {
                    page++;
                    viewModel.fetchData(keywords, "accuracy", page, page_count, viewController)
                }
            }
        })

        rvEpoxy.adapter = viewController.adapter
    }

    private fun getWidth(context: Context): Int {
        val dm = context.applicationContext.resources.displayMetrics
        return dm.widthPixels
    }

    private fun getRatioHeight(context: Context, originHeight: Int, originWidth: Int) : Int{
        return (originHeight.toFloat() * getWidth(context) / originWidth.toFloat()).toInt()
    }
}