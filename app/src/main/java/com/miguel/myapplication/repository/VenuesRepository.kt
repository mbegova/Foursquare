package com.miguel.myapplication.repository

import com.miguel.myapplication.datasource.API_ERROR
import com.miguel.myapplication.datasource.Resource
import com.miguel.myapplication.datasource.remote.VenueDataResponse
import com.miguel.myapplication.repository.remote.ApiVenues
import io.reactivex.Single


interface IVenueRepository{

    fun searchVenues(city: String, venue: String): Single<Resource<VenueDataResponse?>>

}

class VenuesRepository(val venuesApi: ApiVenues):IVenueRepository{

    override fun searchVenues(city: String, venue: String): Single<Resource<VenueDataResponse?>> {
        return venuesApi.searchVenues(near = city, name = venue).map {response->
            if(response.isSuccessful){
                Resource.success(response.body())
            } else {
                Resource.error(API_ERROR)
            }
        }
    }

}