package com.miguel.myapplication.repository

import com.miguel.myapplication.datasource.API_ERROR
import com.miguel.myapplication.datasource.Resource
import com.miguel.myapplication.datasource.local.FoursquareDatabase
import com.miguel.myapplication.datasource.local.entities.VenueData
import com.miguel.myapplication.datasource.remote.VenueDataResponse
import com.miguel.myapplication.repository.remote.ApiVenues
import io.reactivex.Single


interface IVenueRepository{

    fun searchVenues(city: String, venue: String): Single<Resource<VenueDataResponse?>>

}

class VenuesRepository(
    val venuesApi: ApiVenues,
    val database:FoursquareDatabase):IVenueRepository{

    override fun searchVenues(city: String, venue: String): Single<Resource<VenueDataResponse?>> {
        return venuesApi.searchVenues(near = city, name = venue).doOnSuccess {resource->
            val venuesDao = database.venueDao()
            if(resource?.isSuccessful==true) {
                venuesDao.deleteAll()
                val venues = resource.body()?.response?.venues
                venues?.forEach {venue->
                    venue.location?.run{
                        val venueData = VenueData(address = address, postCode = postalCode)
                        venuesDao.insertVenue(venueData)
                    }
                }
            }
        }.map {response->
            if(response.isSuccessful){
                Resource.success(response.body())
            } else {
                Resource.error(API_ERROR)
            }
        }
    }

}