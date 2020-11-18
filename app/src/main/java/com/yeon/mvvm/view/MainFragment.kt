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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.yeon.mvvm.model.Documents
import com.yeon.mvvm.viewmodel.ImageViewModel
import com.yeon.mvvm.R
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: ImageViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val imageView = image_view

        // viewModel = ViewModelProvider(this).get(ImageViewModel::class.java) DI 사용으로 생성 불필요

        search_query.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val timer = Timer()
                    timer.schedule(object: TimerTask() {
                        override fun run() {
                            viewModel.getImageList(s.toString())
                        }
                    }, 1000)
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })

        viewModel.inputImage.observe(viewLifecycleOwner, Observer<ArrayList<Documents>> {
                newImage -> updateImage(newImage[0], requireContext(), imageView)
        })
    }

    private fun updateImage(imageItem: Documents, context : Context, imageView: ImageView) {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val imageViewLayoutParams = imageView.layoutParams
        imageViewLayoutParams.width = getWidth(context)
        imageViewLayoutParams.height = getRatioHeight(context, imageItem.hegiht, imageItem.width)
        imageView.layoutParams = imageViewLayoutParams

        Glide.with(context)
            .load(imageItem.image_url)
            .placeholder(circularProgressDrawable)
            .error(
                Glide.with(context)
                    .load(R.drawable.ic_placeholder)
                    .override(imageItem.width, imageItem.hegiht)
                    .centerCrop())
            .into(imageView)
    }

    private fun getWidth(context: Context): Int {
        val dm = context.applicationContext.resources.displayMetrics
        return dm.widthPixels
    }

    private fun getRatioHeight(context: Context, originHeight: Int, originWidth: Int) : Int{
        return (originHeight.toFloat() * getWidth(context) / originWidth.toFloat()).toInt()
    }
}