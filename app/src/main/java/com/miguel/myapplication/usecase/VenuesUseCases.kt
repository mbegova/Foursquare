package com.miguel.myapplication.usecase

import com.bridgeinternationalacademies.letsmark.usecases.InteractorSingle
import com.miguel.myapplication.datasource.Resource
import com.miguel.myapplication.datasource.remote.VenueDataResponse
import com.miguel.myapplication.repository.VenuesRepository
import io.reactivex.Single

class SearchVenuesUseCase(private val venuesRepository: VenuesRepository) :
    InteractorSingle<SearchVenuesUseCase.SearchVenueDataIn, Resource<VenueDataResponse?>>() {
    override fun buildUseCase(params: SearchVenueDataIn): Single<Resource<VenueDataResponse?>>  =
         params.run {
             venuesRepository.searchVenues(city, venue)
         }


    data class SearchVenueDataIn(
        val city: String,
        val venue: String
    )
}

//TODO Create in this file rest of venues use cases

