package com.miguel.myapplication.datasource.local

import androidx.room.*
import com.miguel.myapplication.datasource.local.entities.VenueData
import io.reactivex.Single

@Dao
interface VenueDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVenue(venues: VenueData):Long

    @Delete
    fun deleteVenue(venue: VenueData)

    @Query("DELETE FROM VenueData")
    fun deleteAll()

    @Query("SELECT * FROM VenueData")
    fun getVenues(): Single<List<VenueData>>

    @Query("SELECT * FROM VenueData")
    suspend fun getVenuesCoroutines(): List<VenueData>

}
