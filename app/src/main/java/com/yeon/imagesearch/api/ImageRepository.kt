package com.yeon.imagesearch.api

import com.yeon.imagesearch.model.BaseModel
import com.yeon.imagesearch.model.ImageModel
import io.reactivex.Single
import java.util.LinkedHashMap

class ImageRepository : BaseImageRepository<ImageModel>() {
    // 검색을 원하는 질의어
    var query: String = ""
    // 결과 문서 정렬 방식 (accuracy : 정확도 recency : 최신순)
    var sort: String = "accuracy"
    // 결과 페이지 번호
    var page: Int = 1
    // 한 페이지에 보여질 문서 수
    var size: Int = 20

    companion object {
        private var ourInstance: ImageRepository? = null
        @JvmStatic
        val instance: ImageRepository
            get() {
                if (ourInstance == null) {
                    synchronized(ImageRepository::class) {
                        if (ourInstance == null) {
                            ourInstance = ImageRepository()
                        }
                    }
                }
                return ourInstance!!
            }
    }


    override fun getResponse(): Single<BaseModel> {
            return  null!!
    }

    fun getResponse(query: String, sort: String = "accuracy", page : Int = 1,  size : Int = 20 ) : Single<ImageModel>{
        this.query = query
        this.sort  = sort
        this.page = page
        this.size = size

        return RetrofitManager.getKakaoAPI()
                .getSearchImage(getQuery())
    }

    override fun queryValuesMap(): LinkedHashMap<String, String> {
        val valuesMap = getValuesMap()
        valuesMap["query"] = query
        valuesMap["sort"] = sort
        valuesMap["page"] = page.toString()
        valuesMap["size"] = size.toString()
        return valuesMap
    }

}