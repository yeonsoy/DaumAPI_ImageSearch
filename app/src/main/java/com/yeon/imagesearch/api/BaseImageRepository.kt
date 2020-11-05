package com.yeon.imagesearch.api

import com.yeon.imagesearch.model.BaseModel

abstract class BaseImageRepository<T> : getResponseInterFace<BaseModel> {

    protected fun getQuery( ): Map<String, String> {
        val map = getDefaultMap()

        for (key in queryValuesMap().keys) {
            if (!queryValuesMap().isEmpty()) {
                map[key] = queryValuesMap()[key]!!
            }
        }
        return map
    }


    private fun getDefaultMap( ): LinkedHashMap<String, String> {
        val map = LinkedHashMap<String,String>()
        return map
    }

    protected  fun getValuesMap(): LinkedHashMap<String, String> {
        return LinkedHashMap()
    }
}