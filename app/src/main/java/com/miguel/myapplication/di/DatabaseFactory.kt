package com.miguel.myapplication.di

import android.content.Context
import androidx.room.Room
import com.miguel.myapplication.datasource.local.FoursquareDatabase

object DatabaseFactory {

    // TODO("Implement a proper migration process)
    fun getDBInstance(context: Context) = Room.databaseBuilder(context, FoursquareDatabase::class.java, "ScannerDatabase")
        .fallbackToDestructiveMigration()
        .build()
}
