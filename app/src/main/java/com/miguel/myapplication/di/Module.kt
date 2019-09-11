package com.miguel.myapplication.di

import com.miguel.myapplication.repository.VenuesRepository
import org.koin.dsl.module


val applicationModule = module {

}

val viewModelModule = module {

}

val databaseModule = module {

}
val repositoryModule = module {
    single { VenuesRepository(get()) }
}

val useCaseModule = module {

}

val networkModule = module {

    factory { VenuesAPIFactory.retrofitVenues() }
    factory { UserAPIFactory.retrofitUser() }
}



