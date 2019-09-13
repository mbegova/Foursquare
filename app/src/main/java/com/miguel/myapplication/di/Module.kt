package com.miguel.myapplication.di

import com.miguel.myapplication.repository.VenuesRepository
import com.miguel.myapplication.usecase.LastQueryUseCase
import com.miguel.myapplication.usecase.SearchVenuesUseCase
import com.miguel.myapplication.viewmodel.VenuesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val applicationModule = module {

}

val viewModelModule = module {
    viewModel{ VenuesViewModel(get(), get()) }
}

val databaseModule = module {

}
val repositoryModule = module {
    single { VenuesRepository(get(), get()) }
    single { DatabaseFactory.getDBInstance(get()) }
}

val useCaseModule = module {
    single { SearchVenuesUseCase(get())}
    single { LastQueryUseCase(get()) }
}

val networkModule = module {
    factory { VenuesAPIFactory.retrofitVenues() }
    factory { UserAPIFactory.retrofitUser() }
}



