package com.shelvz.assignment.network.apiServices

import com.shelvz.assignment.models.response.APIResponse
import com.shelvz.assignment.models.Article
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by shafic on 7/7/17.
 */
interface SearchServices {
    companion object {
        const val SEARCH_ENDPOINT = "/search"
    }

    @GET("$SEARCH_ENDPOINT")
    fun search(@Query("q") query: String? = null,
               @Query("page") page: Int = 1,
               @Query("show-fields") showFields: String = "thumbnail",
               @Query("page-size") pageSize: Int = 10): Flowable<APIResponse<Article>>

}
