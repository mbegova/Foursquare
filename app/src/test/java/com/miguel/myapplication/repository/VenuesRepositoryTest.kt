package com.miguel.myapplication.repository

import com.bridgeinternationalacademies.letsmark.utils.RxSchedulerRule
import com.bridgeinternationalacademies.letsmark.utils.TestException
import com.miguel.myapplication.datasource.API_ERROR
import com.miguel.myapplication.datasource.Resource
import com.miguel.myapplication.datasource.local.FoursquareDatabase
import com.miguel.myapplication.datasource.local.VenueDao
import com.miguel.myapplication.datasource.local.entities.VenueData
import com.miguel.myapplication.datasource.remote.*
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
    private lateinit var  database: FoursquareDatabase
    private lateinit var venuesApi: ApiVenues

    val fakeErrorResponseBody: ResponseBody = ResponseBody.create(MediaType.parse(""), "")

    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    @Before
    fun setup() {

        venuesApi = mockk(relaxed = true)
        database = mockk(relaxed = true)
        venuesRepository = VenuesRepository(venuesApi, database)

    }

    @Test
    fun test_lastQueryVenues_call(){

        val venuesDao: VenueDao = mockk(relaxed = true)
        every { database.venueDao() } returns venuesDao

        venuesRepository.lastQueryVenues()

        verify(exactly = 1) { venuesDao.getVenues() }

    }

   /* @Test
    fun test_lastQueryVenues_success(){
        val venue1: VenueData = mockk(relaxed = true)
        val venue2: VenueData = mockk(relaxed = true)
        val venueList = listOf(venue1, venue2)

        val single = Single.just<List<VenueData>>(venueList)

        val venuesDao: VenueDao = mockk(relaxed = true)
        every { database.venueDao() } returns venuesDao

        every { venuesDao.getVenues() } returns

        venuesRepository.lastQueryVenues()

        verify(exactly = 1) { venuesDao.getVenues() }

    }
    */

    @Test
    fun test_searchVenues_success() {

        val city = "city"
        val venue = "venue"
        val address = "address"
        val postCode = "postCode"
        val meta: Meta = mockk(relaxed = true)
        val geocode: Geocode = mockk(relaxed = true)
        val location = Location(address=address, postalCode=postCode)

        val venue1= Venue(name = venue, location=location)
        val venueList = listOf(venue1)
        val remoteResponse = com.miguel.myapplication.datasource.remote.Response(venueList, geocode)
        val venueDataResponse = VenueDataResponse(meta, remoteResponse)
        val response = Response.success(venueDataResponse)
        val responseObservable = Single.just(response)
        val resource = Resource.success(venueDataResponse)
        val venuesDao: VenueDao = mockk(relaxed = true)
        val venueData = VenueData(name = venue, address=address, postCode=postCode)

        every { venuesApi.searchVenues(near = city, name=venue) } returns responseObservable
        every { database.venueDao() } returns venuesDao

        val single = venuesRepository.searchVenues(city, venue)

        verify(exactly = 1) { venuesApi.searchVenues(near = city, name=venue) }

        val singleTest = single.test()
        singleTest.assertComplete()
        singleTest.assertNoErrors()
        singleTest.assertValue(resource)

        verify(exactly = 1) { venuesDao.insertVenue(venueData) }

    }

    @Test
    fun test_searchVenues_exception() {

        val city = "city"
        val venue = "venue"

        val exception = TestException()
        val responseObservable = Single.error<Response<VenueDataResponse>>(exception)
        val venuesDao: VenueDao = mockk(relaxed = true)

        every { venuesApi.searchVenues(near = city, name=venue) } returns responseObservable
        every { database.venueDao() } returns venuesDao

        val single = venuesRepository.searchVenues(city, venue)

        verify(exactly = 1) { venuesApi.searchVenues(near = city, name=venue) }

        val singleTest = single.test()
        singleTest.assertNotComplete()
        singleTest.assertError(exception)
        verify(exactly = 0) { venuesDao.insertVenue(any()) }
    }

    @Test
    fun test_searchVenues_error() {

        val city = "city"
        val venue = "venue"
        val response =
            Response.error<VenueDataResponse>(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, fakeErrorResponseBody)

        val responseObservable = Single.just(response)
        val resource = Resource.error<VenueDataResponse>(API_ERROR)
        val venuesDao: VenueDao = mockk(relaxed = true)


        every { venuesApi.searchVenues(near = city, name=venue) } returns responseObservable
        every { database.venueDao() } returns venuesDao

        val single = venuesRepository.searchVenues(city, venue)

        verify(exactly = 1) { venuesApi.searchVenues(near = city, name=venue) }

        val singleTest = single.test()
        singleTest.assertComplete()
        singleTest.assertNoErrors()
        singleTest.assertValue(resource)
        verify(exactly = 0) { venuesDao.insertVenue(any()) }

    }

}