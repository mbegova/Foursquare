package com.miguel.myapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bridgeinternationalacademies.letsmark.utils.RxSchedulerRule
import com.bridgeinternationalacademies.letsmark.utils.TestException
import com.miguel.myapplication.datasource.API_ERROR
import com.miguel.myapplication.datasource.Resource
import com.miguel.myapplication.datasource.UNHANDLE_ERROR_CODE
import com.miguel.myapplication.datasource.local.entities.VenueData
import com.miguel.myapplication.datasource.remote.*
import com.miguel.myapplication.usecase.LastQueryUseCase
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
    lateinit var lastQueryUseCase: LastQueryUseCase
    private lateinit var venuesViewModel: VenuesViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    @Before
    fun setup() {
        searchVenuesUseCase = mockk(relaxed = true)
        lastQueryUseCase = mockk(relaxed = true)
        venuesViewModel = VenuesViewModel(searchVenuesUseCase, lastQueryUseCase)
    }

    @Test
    fun searchVenue_success() {

        val city = "city"
        val venue = "venue"
        val searchVenueDataIn = SearchVenuesUseCase.SearchVenueDataIn(city, venue)
        val meta: Meta = mockk(relaxed = true)
        val geocode: Geocode = mockk(relaxed = true)
        val venue1: Venue = mockk(relaxed = true)
        val venue2: Venue = mockk(relaxed = true)
        val venueList = listOf(venue1, venue2)
        val response = Response(venueList, geocode)

        val venueDataResponse = VenueDataResponse(meta, response)
        val venueDataResponseResource = Resource.success(venueDataResponse)
        val single = Single.just<Resource<VenueDataResponse?>>(venueDataResponseResource)

        every { searchVenuesUseCase.run(searchVenueDataIn) } returns single

        venuesViewModel.searchVenue(city, venue)

        verify(exactly = 1) {searchVenuesUseCase.run(searchVenueDataIn)  }

        val testSingle = single.test()
        testSingle.assertComplete()
        testSingle.assertNoErrors()
        testSingle.assertValue(venueDataResponseResource)

        Assert.assertEquals(venueList.map { it.mapToVenueUI() }, venuesViewModel.venueListLiveData.value)
    }

    @Test
    fun searchVenue_no_data() {

        val city = "city"
        val venue = "venue"
        val searchVenueDataIn = SearchVenuesUseCase.SearchVenueDataIn(city, venue)
        val venueDataResponseResource = Resource.success(null)
        val single = Single.just<Resource<VenueDataResponse?>>(venueDataResponseResource)

        every { searchVenuesUseCase.run(searchVenueDataIn) } returns single

        venuesViewModel.searchVenue(city, venue)

        verify(exactly = 1) {searchVenuesUseCase.run(searchVenueDataIn)  }
        val testSingle = single.test()
        testSingle.assertComplete()
        testSingle.assertNoErrors()
        testSingle.assertValue(venueDataResponseResource)

        Assert.assertEquals(emptyList<Venue>(), venuesViewModel.venueListLiveData.value)
    }

    @Test
    fun searchVenue_error() {

        val city = "city"
        val venue = "venue"
        val searchVenueDataIn = SearchVenuesUseCase.SearchVenueDataIn(city, venue)

        val venueDataResponseResource = Resource.error<VenueDataResponse?>(API_ERROR)
        val single = Single.just<Resource<VenueDataResponse?>>(venueDataResponseResource)

        every { searchVenuesUseCase.run(searchVenueDataIn) } returns single

        venuesViewModel.searchVenue(city, venue)

        verify(exactly = 1) {searchVenuesUseCase.run(searchVenueDataIn)  }
        val testSingle = single.test()
        testSingle.assertComplete()
        testSingle.assertNoErrors()
        testSingle.assertValue(venueDataResponseResource)

        Assert.assertEquals(API_ERROR, venuesViewModel.errorLiveData.value)
    }

    @Test
    fun searchVenue_exception() {

        val city = "city"
        val venue = "venue"
        val searchVenueDataIn = SearchVenuesUseCase.SearchVenueDataIn(city, venue)

        val exception = TestException()
        val single = Single.error<Resource<VenueDataResponse?>>(exception)

        every { searchVenuesUseCase.run(searchVenueDataIn) } returns single

        venuesViewModel.searchVenue(city, venue)

        verify(exactly = 1) {searchVenuesUseCase.run(searchVenueDataIn)  }

        val testSingle = single.test()
        testSingle.assertNotComplete()
        testSingle.assertError(exception)

        Assert.assertEquals(UNHANDLE_ERROR_CODE, venuesViewModel.errorLiveData.value)
    }

    @Test
    fun lastQuery_success() {
        val venueId1 = 1L
        val name1 ="name1"
        val address1="address1"
        val postCode1= "postCode1"
        val venueId2 = 2L
        val name2 ="name2"
        val address2="address2"
        val postCode2= "postCode2"
        val venue1=  VenueData(venueId1, name1, address1, postCode1)
        val venue2=  VenueData(venueId2, name2, address2, postCode2)
        val venueList = listOf(venue1, venue2)

        val single = Single.just<List<VenueData>>(venueList)

        every { lastQueryUseCase.run(any()) } returns single

        venuesViewModel.lastQuery()

        verify(exactly = 1) {lastQueryUseCase.run(any())  }

        val testSingle = single.test()
        testSingle.assertComplete()
        testSingle.assertNoErrors()
        testSingle.assertValue(venueList)

        Assert.assertEquals(venueList.map { it.mapToVenueUI() }, venuesViewModel.venueListLiveData.value)
    }

}