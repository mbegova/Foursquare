package com.miguel.myapplication.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.miguel.myapplication.R
import com.miguel.myapplication.viewmodel.VenuesViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val venuesViewModel: VenuesViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setVenuesLiveData()

    }

    override fun onResume() {
        super.onResume()
        venuesViewModel.searchVenue()
    }

    fun setVenuesLiveData() {
        venuesViewModel.venueListLiveData.observe(this, Observer {

            Log.i("TEST-I", "DATOS:${it}")
        })


        venuesViewModel.errorLiveData.observe(this, Observer {

            Log.i("TEST-I", "ERROR:${it}")
        })

    }

}
