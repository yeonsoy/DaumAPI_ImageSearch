package com.yeon.imagesearch.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

// Daum 이미지 검색 API
// https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide

class ImageModel(@SerializedName("meta") var meta: Meta, @SerializedName("documents") var documents: ArrayList<Documents>) : BaseModel() {

    class Meta {
        // 검색된 문서 수
        @SerializedName("total_count")
        var total_count: Int = 0

        // total_count 중 노출 가능 문서 수
        @SerializedName("pageable_count")
        var pageable_count: Int = 0

        // 현재 페이지가 마지막 페이지인지 여부
        @SerializedName("is_end")
        var is_end: Boolean = false
    }

    @Parcelize
    class Documents (
        // 컬렉션
        @SerializedName("collection")
        var collection: String = "",

        // 미리보기 이미지 URL
        @SerializedName("thumbnail_url")
        var thumbnail_url: String = "",

        // 이미지 URL
        @SerializedName("image_url")
        var image_url: String = "",

        // 이미지의 가로 길이
        @SerializedName("width")
        var width: String = "",

        // 이미지의 세로 길이
        @SerializedName("height")
        var height: String = "",

        // 출처
        @SerializedName("display_sitename")
        var display_sitename: String = "",

        // 문서 URL
        @SerializedName("doc_url")
        var doc_url: String = "",

        // 문서 작성시간
        @SerializedName("datetime")
        var datetime: String = "") : Parcelable {}
}