package com.yeon.mvvm.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yeon.mvvm.R
import com.yeon.mvvm.model.Documents
import java.text.SimpleDateFormat

class ImageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        val imageData = intent.getSerializableExtra("item") as Documents

        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val formatter = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")

        val context = findViewById<LinearLayout>(R.id.activity_image_detail).context
        val ivDetailImage = findViewById<ImageView>(R.id.iv_detail_image)
        val tvDocUrl = findViewById<TextView>(R.id.tv_doc_url)
        var tvUploadTime = findViewById<TextView>(R.id.tv_upload_time)

        val ratioHeight = getRatioHeight(getWidth(context), imageData.height, imageData.width)
        val draweeViewLayoutParams = ivDetailImage.layoutParams
        draweeViewLayoutParams.width = getWidth(context)
        draweeViewLayoutParams.height = ratioHeight
        ivDetailImage.layoutParams = draweeViewLayoutParams
        ivDetailImage.setImageURI(Uri.parse(imageData.image_url))
        tvDocUrl.text = imageData.display_sitename + " : " + imageData.doc_url
        tvUploadTime.text = "작성시간 : " + formatter.format(parser.parse(imageData.datetime))
    }

    private fun getWidth(context: Context): Int {
        val dm = context.applicationContext.resources.displayMetrics
        return dm.widthPixels
    }

    private fun getRatioHeight(width : Int, originHeight: Int, originWidth: Int) : Int{
        return (originHeight.toFloat() * width / originWidth.toFloat()).toInt()
    }
}
