package com.shelvz.assignment.network

import com.google.gson.*
import com.shelvz.assignment.network.NetworkConstants.Companion.API_STRING_DATE_FULL_FORMAT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by shafic on 7/15/17.
 */

object NetworkClientFactory {

    internal fun createJsonClient(baseUrl: String): NetworkClient {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)

        return NetworkClient(retrofit, createDefaultClient(), createDefaultUnSignedClient())
    }

    private fun createGson(): Gson {
        val gson: Gson = GsonBuilder()
                .setDateFormat(DateFormat.FULL, DateFormat.FULL)
                .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, _, _ ->
                    try {
                        val sdf = SimpleDateFormat(API_STRING_DATE_FULL_FORMAT)
                        return@JsonDeserializer sdf.parse(json.asString)
                    } catch (exception: ParseException) {
                        exception.printStackTrace()
                    }

                    null
                })
                .registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
                    Date(json.asJsonPrimitive.asLong * 1000)
                })
                .registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, _, _ ->
                    JsonPrimitive(src.time / 1000)
                })
                .create()
        return gson
    }

    private fun createDefaultUnSignedClient(): OkHttpClient {
        val defaultClient = OkHttpClient.Builder()

        return defaultClient
                .readTimeout(NetworkConstants.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(createLoggingInterceptor())
                .build()
    }

    private fun createDefaultClient(): OkHttpClient {
        val defaultClient = OkHttpClient.Builder()

        return defaultClient
                .readTimeout(NetworkConstants.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(createLoggingInterceptor())
                .build()
    }

    private fun createLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        return logging

    }
}
