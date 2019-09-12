package com.miguel.myapplication.database


import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miguel.myapplication.datasource.local.FoursquareDatabase
import com.miguel.myapplication.datasource.local.VenueDao
import com.miguel.myapplication.datasource.local.entities.VenueData
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class VenueDaoTest {

    private lateinit var venueDao: VenueDao
    private lateinit var foursquareDatabase: FoursquareDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        foursquareDatabase = Room.inMemoryDatabaseBuilder(context, FoursquareDatabase::class.java).build()
        venueDao = foursquareDatabase.venueDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        foursquareDatabase.close()
    }

    @Test
    fun testAcademyTable_basic_access() {
        val address1 = "address1"
        val postCode1 = "postCode1"

        val address2 = "address2"
        val postCode2 = "postCode2"

        val venue1 = VenueData(address = address1, postCode = postCode1)
        val venue2 = VenueData(address = address2, postCode = postCode2)

        val venueList = listOf(venue1, venue2)

        venueList.forEach {
            val id = venueDao.insertVenue(it)
            it.venueId = id
        }

        val result = venueDao.getVenues()
        val resultTest = result.test()
        resultTest.assertComplete()
        resultTest.assertNoErrors()
        resultTest.assertValue(venueList)
    }


}