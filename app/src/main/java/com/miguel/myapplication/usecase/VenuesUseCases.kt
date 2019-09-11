package com.miguel.myapplication.usecase

import com.bridgeinternationalacademies.letsmark.usecases.InteractorSingle
import com.miguel.myapplication.datasource.Resource
import com.miguel.myapplication.datasource.remote.VenueDataResponse
import com.miguel.myapplication.repository.VenuesRepository
import io.reactivex.Single

class SearchVenuesUseCase(private val venuesRepository: VenuesRepository) :
    InteractorSingle<Any, Resource<VenueDataResponse?>>() {
    override fun buildUseCase(params: Any): Single<Resource<VenueDataResponse?>> {
        return venuesRepository.searchVenues()
    }
}

//TODO Create in this file rest of venues use cases

