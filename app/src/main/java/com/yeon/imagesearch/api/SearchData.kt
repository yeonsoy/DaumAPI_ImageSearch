package com.yeon.imagesearch.api

import com.yeon.imagesearch.model.ImageModel
import io.reactivex.Single

// 싱글톤 객체 (동일 구조 여러 번 사용하기)
class SearchData {
    // 검색을 원하는 질의어
    var query: String = ""
    // 결과 문서 정렬 방식 (accuracy : 정확도 recency : 최신순)
    var sort: String = "accuracy"
    // 결과 페이지 번호
    var page: Int = 1
    // 한 페이지에 보여질 문서 수
    var size: Int = 30

    companion object {
        private var ourInstance: SearchData? = null
        @JvmStatic
        val instance: SearchData
            get() {
                if (ourInstance == null) {
                    synchronized(SearchData::class) {
                        if (ourInstance == null) {
                            ourInstance = SearchData()
                        }
                    }
                }
                return ourInstance!!
            }
    }

    private fun queryValuesMap(): java.util.LinkedHashMap<String, String> {
        val valuesMap = getValuesMap()
        valuesMap["query"] = query
        valuesMap["sort"] = sort
        valuesMap["page"] = page.toString()
        valuesMap["size"] = size.toString()
        return valuesMap
    }

    fun getQuery( ): Map<String, String> {
        val map = getDefaultMap()

        for (key in queryValuesMap().keys) {
            if (queryValuesMap().isNotEmpty()) {
                map[key] = queryValuesMap()[key]!!
            }
        }
        return map
    }

    private fun getDefaultMap( ): LinkedHashMap<String, String> {
        val map = LinkedHashMap<String,String>()
        return map
    }

    private fun getValuesMap(): LinkedHashMap<String, String> {
        return LinkedHashMap()
    }

    fun getResponse(): Single<BaseSearchData> {
        return  null!!
    }

    fun getResponse(query: String, sort: String = "recency", page : Int = 1,  size : Int = 30 ) : Single<ImageModel> {
        this.query = query
        this.sort  = sort
        this.page = page
        this.size = size

        return RetrofitManager.getKaKaoAPI()
            .getSearchImage(getQuery())
    }


}