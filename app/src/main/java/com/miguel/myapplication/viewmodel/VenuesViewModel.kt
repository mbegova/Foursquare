package com.miguel.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import com.miguel.myapplication.datasource.NO_ERROR
import com.miguel.myapplication.datasource.Status
import com.miguel.myapplication.datasource.UNHANDLE_ERROR_CODE
import com.miguel.myapplication.datasource.ui.Venue
import com.miguel.myapplication.usecase.LastQueryUseCase
import com.miguel.myapplication.usecase.SearchVenuesUseCase
import otherwise
import timber.log.Timber


 class VenuesViewModel(
    private val searchVenuesUseCase: SearchVenuesUseCase,
    private val lastQueryUseCase: LastQueryUseCase
) : RxViewModel() {

    val venueListLiveData = MutableLiveData<List<Venue>>()
    val errorLiveData = MutableLiveData<Int>()

     fun searchVenue(city:String, venue:String) {
         val searchVenueDataIn = SearchVenuesUseCase.SearchVenueDataIn(city, venue)
        compositeDisposable.add(
            searchVenuesUseCase.run(searchVenueDataIn).subscribe({ venueResponse ->
                if (venueResponse.status != Status.ERROR) {
                    venueResponse.data?.response?.venues?.let { venuesList ->
                        venueListLiveData.value = venuesList.map { venue -> venue.mapToVenueUI() }
                    }.otherwise {
                        venueListLiveData.value = emptyList()
                    }
                } else {
                    errorLiveData.value = venueResponse.errorCode.let { it }.otherwise { UNHANDLE_ERROR_CODE }
                }
            }, { error ->
                errorLiveData.value = UNHANDLE_ERROR_CODE
                Timber.e("Error loading venue data from endpoint e:${error.message}")
                error.printStackTrace()
            })
        )
    }

     fun lastQuery(){
         compositeDisposable.add(
             lastQueryUseCase.run(Any()).subscribe(
                 {venueList->
                     venueListLiveData.value =  venueList.map { venue -> venue.mapToVenueUI() }
                 },
                 {error->
                     errorLiveData.value = UNHANDLE_ERROR_CODE
                     Timber.e("Error loading venue data from db e:${error.message}")
                     error.printStackTrace()
                 }
             )
         )
     }
}
