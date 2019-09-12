package com.miguel.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import com.miguel.myapplication.datasource.Status
import com.miguel.myapplication.datasource.UNHANDLE_ERROR_CODE
import com.miguel.myapplication.datasource.remote.Venue
import com.miguel.myapplication.usecase.SearchVenuesUseCase
import otherwise
import timber.log.Timber


 class VenuesViewModel(
    private val searchVenuesUseCase: SearchVenuesUseCase
) : RxViewModel() {

    val venueListLiveData = MutableLiveData<List<Venue>>()
    val errorLiveData = MutableLiveData<Int>()

     fun searchVenue() {
        compositeDisposable.add(
            searchVenuesUseCase.run(Any()).subscribe({ venueResponse ->
                if (venueResponse.status != Status.ERROR) {
                    venueResponse.data?.response?.venues?.let { venuesList ->
                        venueListLiveData.value = venuesList
                    }.otherwise {
                        venueListLiveData.value = emptyList()
                    }
                } else {
                    errorLiveData.value = venueResponse.errorCode.let { it }.otherwise { UNHANDLE_ERROR_CODE }
                }
            }, { error ->
                errorLiveData.value = UNHANDLE_ERROR_CODE
                Timber.e("Error loading employee data e:${error.message}")
                error.printStackTrace()
            })
        )
    }
}