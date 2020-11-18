package com.yeon.mvvm.model

import com.google.gson.annotations.SerializedName

object ImageModel {
    data class Result(
            var meta: Meta,

            @SerializedName("documents")
        var documents: ArrayList<Documents>
    )
}

data class Meta (
    @SerializedName("total_count")
    var total_count: Int, // 검색된 문서 수

    @SerializedName("pageable_count")
    var pageable_count: Int, // total_count 중 노출 가능 문서 수

    @SerializedName("is_end")
    var is_end: Boolean = true // 현재 페이지가 마지막 페이지인지 여부
)

data class Documents (
    @SerializedName("collection")
    var collection: String, // 컬렉션

    @SerializedName("thumbnail_url")
    var thumbnail_url: String, // 미리보기 이미지 URL

    @SerializedName("image_url")
    var image_url: String, // 이미지 URL

    @SerializedName("width")
    var width: Int, // 이미지의 가로 길이

    @SerializedName("height")
    var hegiht: Int, // 이미지의 세로 길이

    @SerializedName("display_sitename")
    var display_sitename: String, // 출처

    @SerializedName("doc_url")
    var doc_url: String, // 문서 URL

    @SerializedName("datetime")
    var datetime: String // 문서 작성시간
)
