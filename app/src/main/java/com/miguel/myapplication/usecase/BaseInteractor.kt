package com.bridgeinternationalacademies.letsmark.usecases

interface BaseInteractor<in PARAMS> {

    fun buildUseCase(params: PARAMS): Any

    fun run(params: PARAMS): Any {
        return this.buildUseCase(params)
    }
}
