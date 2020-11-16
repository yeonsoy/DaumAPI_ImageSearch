package com.yeon.imagesearch.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yeon.imagesearch.R
import com.yeon.imagesearch.util.DaumAPI
import com.yeon.imagesearch.util.DaumAPI.Companion.getImageDocuments
import com.yeon.imagesearch.util.DaumAPI.Companion.isInternetAvailable
import com.yeon.imagesearch.util.Documents
import com.yeon.imagesearch.util.ImageModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private val api: DaumAPI by lazy { DaumAPI.create() }
    private lateinit var adapter: RecyclerAdapter
    private var imageList: MutableList<Documents> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        if(isInternetAvailable()) {
            search_query.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    getImageList(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                    val timer = Timer()
                    timer.schedule(object: TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                getImageList(s.toString())
                            }
                        }
                    }, 1000)
                }

            })
        }
    }

    private fun init()
    {
        imageList = mutableListOf()
        adapter = RecyclerAdapter(imageList, this)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, 1)
        staggeredGridLayoutManager.orientation = StaggeredGridLayoutManager.VERTICAL
        recycler_view.layoutManager = staggeredGridLayoutManager
        recycler_view.addItemDecoration(GridSpacingItemDecoration(3, 0, true))
        recycler_view.adapter = adapter
    }

    private fun getImageList(query: String, sort: String? = "accuracy", page: Int? = 1, size: Int? = 80) {
        // retrofit2 만을 사용한 예외처리
        api.getImageList(query, sort, page, size).enqueue(object : Callback<ImageModel.Result> {
            override fun onResponse(
                call: Call<ImageModel.Result>,
                response: Response<ImageModel.Result>
            ) {
                if (response.message() != "OK") {
                    imageList.clear()
                    adapter.notifyDataSetChanged()
                    println(response)
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()
                }
                else {
                    val imageResponse = response.body()
                    imageList.clear()
                    imageResponse?.let { imageList.addAll(getImageDocuments(it)) }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ImageModel.Result>, t: Throwable) {
                imageList.clear()
                adapter.notifyDataSetChanged()
            }
        })
    }
}