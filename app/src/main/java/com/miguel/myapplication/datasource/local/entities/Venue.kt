package com.miguel.myapplication.datasource.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(indices = [Index("venueId")])
data class VenueData(
    @PrimaryKey(autoGenerate = true)
    var venueId: Long=0,
    val address: String? = "",
    val postCode: String? = ""
)