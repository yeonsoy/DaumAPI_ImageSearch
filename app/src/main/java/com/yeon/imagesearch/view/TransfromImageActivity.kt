package com.yeon.imagesearch.view

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yeon.imagesearch.R
import com.yeon.imagesearch.api.RetrofitManager
import me.relex.photodraweeview.PhotoDraweeView

class TransformImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transform_image)
        val imageUrl = intent.getStringExtra("image_url")
        val photoDraweeView = findViewById<PhotoDraweeView>(R.id.my_image_view)
        photoDraweeView.setPhotoUri(Uri.parse(imageUrl))

        val layoutParams = photoDraweeView.layoutParams
        layoutParams.width = RetrofitManager.getWidth(this)
        layoutParams.height = RetrofitManager.getHeight(this)
        photoDraweeView.layoutParams = layoutParams
    }
    override fun onDestroy() {
        super.onDestroy()
        if (intent != null) {
            intent.removeExtra("image_url")
        }
    }
}
