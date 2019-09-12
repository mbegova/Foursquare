package com.miguel.myapplication.usecase

import com.bridgeinternationalacademies.letsmark.utils.RxSchedulerRule
import com.miguel.myapplication.repository.VenuesRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchVenuesUseCaseTests {
    private lateinit var venuesRepository: VenuesRepository
    private lateinit var searchVenuesUseCase: SearchVenuesUseCase

    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    @Before
    fun setup() {
        venuesRepository = mockk(relaxed = true)
        searchVenuesUseCase = SearchVenuesUseCase(venuesRepository)
    }

    @Test
    fun run_success() {
        val city = "city"
        val venue = "venue"
        val searchVenueDataIn = SearchVenuesUseCase.SearchVenueDataIn(city, venue)
        searchVenuesUseCase.run(searchVenueDataIn)
        verify(exactly = 1) { venuesRepository.searchVenues(city, venue) }
    }
}