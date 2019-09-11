package com.miguel.myapplication.datasource.remote

data class VenueDataResponse(

    val meta: Meta? = null,
    val response: Response? = null

)

data class Meta(
    val code: Int? = null,
    val requestId: String? = null
)

data class Response(
    val venues: List<Venue>? = null,
    val geocode: Geocode? = null
)

data class Venue(
    val id: String? = null,
    val name: String? = null,
    val location: Location? = null,
    val categories: List<Category>? = null,
    val referralId: String? = null,
    val hasPerk: Boolean? = null
)

data class Location(
    val address: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val postalCode: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null

)

data class Category(
    val id: String? = null,
    val name: String? = null,
    val pluralName: String? = null,
    val shortName: String? = null,
    val icon: Icon? = null,
    val primary: Boolean? = null
    )

data class Icon(
    val prefix: String?=null,
    val suffix: String?=null
)

data class Geocode(
    val what:String? =null,
    val where:String? = null
)