package com.shelvz.assignment.network

import com.shelvz.assignment.BuildConfig


/**
 * NetworkConstants Singleton
 *
 * Static final fields
 * Created by shafic on 7/15/17.
 */

class NetworkConstants {
    companion object {
        const val READ_TIMEOUT_SECONDS = 60L
        const val API_STRING_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val API_STRING_DATE_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val API_STRING_DATE_FULL_FORMAT_2 = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        val API_KEY by lazy {
            BuildConfig.API_KEY
        }
        val API_URL by lazy {
            BuildConfig.API_BASE_URL
        }
    }
}
