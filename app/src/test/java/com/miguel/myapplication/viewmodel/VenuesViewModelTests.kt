package com.miguel.myapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bridgeinternationalacademies.letsmark.utils.RxSchedulerRule
import com.bridgeinternationalacademies.letsmark.utils.TestException
import com.miguel.myapplication.datasource.API_ERROR
import com.miguel.myapplication.datasource.Resource
import com.miguel.myapplication.datasource.UNHANDLE_ERROR_CODE
import com.miguel.myapplication.datasource.remote.*
import com.miguel.myapplication.usecase.SearchVenuesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class VenuesViewModelTests {
    lateinit var searchVenuesUseCase: SearchVenuesUseCase
    private lateinit var venuesViewModel: VenuesViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    @Before
    fun setup() {
        searchVenuesUseCase = mockk(relaxed = true)
        venuesViewModel = VenuesViewModel(searchVenuesUseCase)
    }

    @Test
    fun searchVenue_success() {
        val meta: Meta = mockk(relaxed = true)
        val geocode: Geocode = mockk(relaxed = true)
        val venue1: Venue = mockk(relaxed = true)
        val venue2: Venue = mockk(relaxed = true)
        val venueList = listOf(venue1, venue2)
        val response = Response(venueList, geocode)

        val venueDataResponse = VenueDataResponse(meta, response)
        val venueDataResponseResource = Resource.success(venueDataResponse)
        val single = Single.just<Resource<VenueDataResponse?>>(venueDataResponseResource)

        every { searchVenuesUseCase.run(any()) } returns single

        venuesViewModel.searchVenue()
        val testSingle = single.test()
        testSingle.assertComplete()
        testSingle.assertNoErrors()
        testSingle.assertValue(venueDataResponseResource)

        verify(exactly = 1) { searchVenuesUseCase.run(any()) }
        Assert.assertEquals(venueList, venuesViewModel.venueListLiveData.value)
    }

    @Test
    fun searchVenue_no_data() {
        val venueDataResponseResource = Resource.success(null)
        val single = Single.just<Resource<VenueDataResponse?>>(venueDataResponseResource)

        every { searchVenuesUseCase.run(any()) } returns single

        venuesViewModel.searchVenue()
        val testSingle = single.test()
        testSingle.assertComplete()
        testSingle.assertNoErrors()
        testSingle.assertValue(venueDataResponseResource)

        verify(exactly = 1) { searchVenuesUseCase.run(any()) }
        Assert.assertEquals(emptyList<Venue>(), venuesViewModel.venueListLiveData.value)
    }

    @Test
    fun searchVenue_error() {

        val venueDataResponseResource = Resource.error<VenueDataResponse?>(API_ERROR)
        val single = Single.just<Resource<VenueDataResponse?>>(venueDataResponseResource)

        every { searchVenuesUseCase.run(any()) } returns single

        venuesViewModel.searchVenue()
        val testSingle = single.test()
        testSingle.assertComplete()
        testSingle.assertNoErrors()
        testSingle.assertValue(venueDataResponseResource)

        verify(exactly = 1) { searchVenuesUseCase.run(any()) }
        Assert.assertEquals(API_ERROR, venuesViewModel.errorLiveData.value)
    }

    @Test
    fun searchVenue_exception() {

        val exception = TestException()
        val single = Single.error<Resource<VenueDataResponse?>>(exception)

        every { searchVenuesUseCase.run(any()) } returns single

        venuesViewModel.searchVenue()

        val testSingle = single.test()
        testSingle.assertNotComplete()
        testSingle.assertError(exception)

        verify(exactly = 1) { searchVenuesUseCase.run(any()) }
        Assert.assertEquals(UNHANDLE_ERROR_CODE, venuesViewModel.errorLiveData.value)
    }

}