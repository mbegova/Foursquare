package com.miguel.myapplication.usecase

import arrow.core.orNull
import com.bridgeinternationalacademies.letsmark.utils.TestException
import com.miguel.myapplication.datasource.local.entities.VenueData
import com.miguel.myapplication.repository.IVenueRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LastQueryCoroutineUseCaseTests {
    private lateinit var venuesRepository: IVenueRepository
    private lateinit var lastQueryCoroutineUseCase: LastQueryCoroutineUseCase

    @Before
    fun setup() {
        venuesRepository = mockk(relaxed = true)
        lastQueryCoroutineUseCase = LastQueryCoroutineUseCase(venuesRepository)
    }

    @Test
    fun run_success() = runBlocking {

        val list: List<VenueData> = mockk(relaxed = true)

        coEvery { venuesRepository.lastQueryVenuesCoroutines()} returns list
        val result = lastQueryCoroutineUseCase.run(Any())

        coVerify(exactly = 1) { venuesRepository.lastQueryVenuesCoroutines() }
        Assert.assertTrue(result.isRight())
        Assert.assertEquals(list, result.orNull())

    }

    @Test
    fun run_exception() = runBlocking {

        coEvery { venuesRepository.lastQueryVenuesCoroutines()} throws TestException()
        val result = lastQueryCoroutineUseCase.run(Any())

        coVerify(exactly = 1) { venuesRepository.lastQueryVenuesCoroutines() }
        Assert.assertTrue(result.isLeft())

    }

}