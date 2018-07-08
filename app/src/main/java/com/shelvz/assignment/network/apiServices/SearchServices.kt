package com.shelvz.assignment.network.apiServices

import com.shelvz.assignment.models.Article
import com.shelvz.assignment.models.response.APIResponse
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

    /**
     * @param orderBy 'newest' or 'relevance' string
     * @param dateFrom Dates must be an ISO8601 date or datetime
     * (e.g. 2010-07-20 or 2010-07-20T10:00:00+05:00). Remember to URL-encode
     * the '+' if you provide a timezone offset.
     * */
    @GET("$SEARCH_ENDPOINT")
    fun search(@Query("q") query: String? = null,
               @Query("page") page: Int = 1,
               @Query("show-fields") showFields: String = "thumbnail",
               @Query("page-size") pageSize: Int = 10,
               @Query("order-by") orderBy: String = "newest",
               @Query("from-date") dateFrom: String? = null): Flowable<APIResponse<Article>>

}
