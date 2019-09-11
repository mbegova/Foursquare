package com.miguel.myapplication.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miguel.myapplication.R
import com.miguel.myapplication.di.VenuesAPIFactory
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    val venuesApi = VenuesAPIFactory.retrofitVenues()
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO code only for manual testing, delete it
        compositeDisposable.add(venuesApi.searchVenues().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
            {},
            {}
        ))

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}
