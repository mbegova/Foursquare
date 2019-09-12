package com.miguel.myapplication.repository

import com.bridgeinternationalacademies.letsmark.utils.RxSchedulerRule
import com.bridgeinternationalacademies.letsmark.utils.TestException
import com.miguel.myapplication.datasource.API_ERROR
import com.miguel.myapplication.datasource.Resource
import com.miguel.myapplication.datasource.remote.VenueDataResponse
import com.miguel.myapplication.repository.remote.ApiVenues
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.net.HttpURLConnection

class VenuesRepositoryTest {

    private lateinit var venuesRepository: VenuesRepository
    private lateinit var venuesApi: ApiVenues

    val fakeErrorResponseBody: ResponseBody = ResponseBody.create(MediaType.parse(""), "")

    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    @Before
    fun setup() {

        venuesApi = mockk(relaxed = true)
        venuesRepository = VenuesRepository(venuesApi)

    }

    @Test
    fun test_searchVenues_success() {

        val city = "city"
        val venue = "venue"
        val venueDataResponse: VenueDataResponse = mockk(relaxed = true)
        val response = Response.success(venueDataResponse)
        val responseObservable = Single.just(response)
        val resource = Resource.success(venueDataResponse)

        every { venuesApi.searchVenues(near = city, name=venue) } returns responseObservable

        val single = venuesRepository.searchVenues(city, venue)

        verify(exactly = 1) { venuesApi.searchVenues(near = city, name=venue) }

        val singleTest = single.test()
        singleTest.assertComplete()
        singleTest.assertNoErrors()
        singleTest.assertValue(resource)

    }

    @Test
    fun test_searchVenues_exception() {

        val city = "city"
        val venue = "venue"

        val exception = TestException()
        val responseObservable = Single.error<Response<VenueDataResponse>>(exception)

        every { venuesApi.searchVenues(near = city, name=venue) } returns responseObservable

        val single = venuesRepository.searchVenues(city, venue)

        verify(exactly = 1) { venuesApi.searchVenues(near = city, name=venue) }

        val singleTest = single.test()
        singleTest.assertNotComplete()
        singleTest.assertError(exception)
    }

    @Test
    fun test_searchVenues_error() {

        val city = "city"
        val venue = "venue"
        val response =
            Response.error<VenueDataResponse>(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, fakeErrorResponseBody)

        val responseObservable = Single.just(response)
        val resource = Resource.error<VenueDataResponse>(API_ERROR)


        every { venuesApi.searchVenues(near = city, name=venue) } returns responseObservable

        val single = venuesRepository.searchVenues(city, venue)

        verify(exactly = 1) { venuesApi.searchVenues(near = city, name=venue) }

        val singleTest = single.test()
        singleTest.assertComplete()
        singleTest.assertNoErrors()
        singleTest.assertValue(resource)

    }

}