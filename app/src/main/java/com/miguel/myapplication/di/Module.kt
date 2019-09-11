package com.miguel.myapplication.di

import com.miguel.myapplication.repository.VenuesRepository
import com.miguel.myapplication.usecase.SearchVenuesUseCase
import com.miguel.myapplication.viewmodel.VenuesViewModel
import org.koin.dsl.module


val applicationModule = module {

}

val viewModelModule = module {
    single{ VenuesViewModel(get()) }
}

val databaseModule = module {

}
val repositoryModule = module {
    single { VenuesRepository(get()) }
}

val useCaseModule = module {
    single { SearchVenuesUseCase(get()) }
}

val networkModule = module {

    factory { VenuesAPIFactory.retrofitVenues() }
    factory { UserAPIFactory.retrofitUser() }
}



