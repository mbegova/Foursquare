package com.miguel.myapplication.di

import com.miguel.myapplication.repository.remote.ApiUsers
import com.miguel.myapplication.repository.remote.ApiVenues
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_TIMEOUT: Long = 60
const val BASE_API_URL = "https://api.foursquare.com/v2"
const val API_VENUES_URL = "/venues/"
const val API_USERS_URL = "/users/"

object UserAPIFactory {

    fun retrofitUser(): ApiUsers {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        builder.writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        builder.connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        //Uncomment to send ckey by header
        /*
        val requestInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val clientID= ""
            val clientSecret = ""
            val newRequest = originalRequest.newBuilder()
                .addHeader("client_id", clientID)
                .addHeader("client_screen", clientSecret)
                .build()
            chain.proceed(newRequest)
        }
        builder.addInterceptor(requestInterceptor)
    */
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logInterceptor)

        return Retrofit.Builder()
            .baseUrl(BASE_API_URL+API_USERS_URL)
            .client(builder.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiUsers::class.java)
    }
}


object VenuesAPIFactory {

    fun retrofitVenues(): ApiVenues {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        builder.writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        builder.connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
        //Uncomment to send ckey by header
        /*
        val requestInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val clientID= ""
            val clientSecret = ""
            val newRequest = originalRequest.newBuilder()
                .addHeader("client_id", clientID)
                .addHeader("client_screen", clientSecret)
                .build()
            chain.proceed(newRequest)
        }
        builder.addInterceptor(requestInterceptor)
    */
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logInterceptor)

        return Retrofit.Builder()
            .baseUrl(BASE_API_URL+API_VENUES_URL)
            .client(builder.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiVenues::class.java)
    }
}

