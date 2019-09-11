package com.miguel.myapplication

import android.app.Application
import com.miguel.myapplication.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import timber.log.Timber


open class FoursquareApplication : Application() {

    val appComponent: List<Module> =
        mutableListOf(applicationModule, viewModelModule, databaseModule,
            useCaseModule, networkModule, repositoryModule)

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(appComponent)
        }


        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
