package com.miguel.myapplication.usecase

import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class CoroutineBaseUseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<Exception, Type>

    open operator fun invoke(
        scope: CoroutineScope,
        params: Params,
        onResult: (Either<Exception, Type>) -> Unit = {}
    ) {
        val backgroundJob = scope.async { run(params) }
        scope.launch { onResult(backgroundJob.await()) }
    }
}
