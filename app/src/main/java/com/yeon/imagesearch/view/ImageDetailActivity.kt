package com.yeon.imagesearch.view

import android.os.Bundle
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yeon.imagesearch.R
import com.yeon.imagesearch.api.RetrofitManager
import com.yeon.imagesearch.model.ImageModel
import java.text.SimpleDateFormat
import java.util.*

class ImageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        val parcelable = intent.getParcelableExtra<ImageModel.Documents>("item")!!
        val imageView = findViewById<ImageView>(R.id.my_image_view)

        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(this)
                .load(parcelable.image_url)
                .placeholder(circularProgressDrawable)
                .thumbnail(Glide.with(this)
                        .load(parcelable.thumbnail_url)
                        .override(parcelable.width.toInt(), parcelable.height.toInt())
                        .centerCrop())
                .into(imageView)

        val ratioHeight = RetrofitManager.getRatioHeight(this, parcelable.height.toInt(), parcelable.width.toInt())
        val layoutParams = imageView.layoutParams
        layoutParams.width = RetrofitManager.getWidth(this)
        layoutParams.height = ratioHeight
        imageView.layoutParams = layoutParams

        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val formatter = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")

        findViewById<TextView>(R.id.tv_departure).text = String.format(Locale.KOREA, "출처: %s", parcelable.display_sitename)
        findViewById<TextView>(R.id.tv_doc_url).text = String.format(Locale.KOREA, "문서 작성시각: %s", formatter.format(parser.parse(parcelable.datetime)))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (intent != null) {
            intent.removeExtra("item")
        }
    }
}
