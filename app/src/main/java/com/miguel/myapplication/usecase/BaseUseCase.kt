package com.bridgeinternationalacademies.letsmark.usecases

interface BaseUseCase<in Params, Result> {

    fun run(data: Params): Result
}
