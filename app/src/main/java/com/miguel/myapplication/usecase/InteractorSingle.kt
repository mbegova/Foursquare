package com.bridgeinternationalacademies.letsmark.usecases

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class InteractorSingle<in PARAMS, RESULT> : BaseInteractor<PARAMS> {

    abstract override fun buildUseCase(params: PARAMS): Single<RESULT>

    override fun run(params: PARAMS): Single<RESULT> {
        return this.buildUseCase(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    } }
