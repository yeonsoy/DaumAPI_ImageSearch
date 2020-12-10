package com.yeon.mvvm.model

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    var meta: Meta,
    val documents: ArrayList<Documents>
)

data class Meta(
    var total_count: Int, // 검색된 문서 수
    var pageable_count: Int, // total_count 중 노출 가능 문서 수
    var is_end: Boolean = true // 현재 페이지가 마지막 페이지인지 여부
)

@Serializable
data class Documents(
    var collection: String? = null, // 컬렉션
    var thumbnail_url: String? = null, // 미리보기 이미지 URL
    var image_url: String? = null, // 이미지 URL
    var width: Int = 0, // 이미지의 가로 길이
    var height: Int = 0, // 이미지의 세로 길이
    var display_sitename: String? = null, // 출처
    var doc_url: String? = null, // 문서 URL
    var datetime: String? = null // 문서 작성시간
) : java.io.Serializable
