package com.miguel.myapplication.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miguel.myapplication.datasource.local.entities.VenueData

@Database(
    entities = [VenueData::class],
    version = 2, exportSchema = false
)
abstract class FoursquareDatabase : RoomDatabase() {

    // Get data access object to handle venues operation with the db
    abstract fun venueDao(): VenueDao

}
