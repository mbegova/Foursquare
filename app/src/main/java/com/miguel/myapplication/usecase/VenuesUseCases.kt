package com.miguel.myapplication.usecase

import arrow.core.Either
import com.bridgeinternationalacademies.letsmark.usecases.InteractorSingle
import com.miguel.myapplication.datasource.Resource
import com.miguel.myapplication.datasource.local.entities.VenueData
import com.miguel.myapplication.datasource.remote.VenueDataResponse
import com.miguel.myapplication.repository.IVenueRepository
import com.miguel.myapplication.repository.VenuesRepository
import io.reactivex.Single

class SearchVenuesUseCase(private val venuesRepository: IVenueRepository) :
    InteractorSingle<SearchVenuesUseCase.SearchVenueDataIn, Resource<VenueDataResponse?>>() {
    override fun buildUseCase(params: SearchVenueDataIn): Single<Resource<VenueDataResponse?>> =
        params.run {
            venuesRepository.searchVenues(city, venue)
        }


    data class SearchVenueDataIn(
        val city: String,
        val venue: String
    )
}

class LastQueryUseCase(private val venuesRepository: IVenueRepository) :
    InteractorSingle<Any, List<VenueData>>() {
    override fun buildUseCase(params: Any): Single<List<VenueData>> =
        venuesRepository.lastQueryVenues()

}

class LastQueryCoroutineUseCase(
    private val venuesRepository: IVenueRepository
) : CoroutineBaseUseCase<List<VenueData>, Any>() {

    override suspend fun run(params: Any): Either<Exception, List<VenueData>> {
        return try {
            val venuesList = venuesRepository.lastQueryVenuesCoroutines()
            Either.Right(venuesList)
        } catch (exp: Exception) {
            Either.Left(LastQueryCoroutineUseCaseException(exp))
        }
    }

    class LastQueryCoroutineUseCaseException(error: Exception) : java.lang.Exception(error)
}
