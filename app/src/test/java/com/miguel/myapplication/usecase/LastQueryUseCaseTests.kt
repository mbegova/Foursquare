package com.miguel.myapplication.usecase

import com.bridgeinternationalacademies.letsmark.utils.RxSchedulerRule
import com.miguel.myapplication.repository.VenuesRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LastQueryUseCaseTests {
    private lateinit var venuesRepository: VenuesRepository
    private lateinit var lastQueryUseCase: LastQueryUseCase

    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    @Before
    fun setup() {
        venuesRepository = mockk(relaxed = true)
        lastQueryUseCase = LastQueryUseCase(venuesRepository)
    }

    @Test
    fun run_success() {
        lastQueryUseCase.run(Any())
        verify(exactly = 1) { venuesRepository.lastQueryVenues() }
    }
}