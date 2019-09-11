package com.miguel.myapplication.repository.remote

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//TODO Move to more secure place
const val CLIENT_ID= "PDBS2HEWEFRXPF2PYVV2V022X0KAQ1ZV3KLWV2UNLZHE41SE"
const val CLIENT_SECRET = "AJM1S4HFRJNRDIH5NOZDYDLZVG1RR0UALTYBVQJFXTLNKJVL"
const val VERSION ="20181010"

interface ApiUsers {

    @GET("search")
    fun searchUsers(@Query("name") name: String):
            Single<Response<List<String>>>

}

interface ApiVenues{

    @GET("search")
    fun searchVenues(
        @Query("client_id") clientId: String=CLIENT_ID,
        @Query("client_secret") clientSecret: String=CLIENT_SECRET,
        @Query("v") version: String=VERSION,
        @Query("near") near: String="London",
        @Query("query") name: String ="Costa Coffee"):
            Single<Response<List<Any>>>
}
