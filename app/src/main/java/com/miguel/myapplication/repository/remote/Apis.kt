package com.miguel.myapplication.repository.remote

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiUsers {

    @GET("search")
    fun searchUsers(@Query("name") name: String):
            Single<Response<List<String>>>

}

interface ApiVenues{

    @GET("search")
    fun searchVenues(@Query("query") name: String):
            Single<Response<List<String>>>
}
