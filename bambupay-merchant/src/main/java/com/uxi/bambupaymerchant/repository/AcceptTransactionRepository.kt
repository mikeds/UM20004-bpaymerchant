package com.uxi.bambupaymerchant.repository

import com.uxi.bambupaymerchant.api.GenericApiResponse
import com.uxi.bambupaymerchant.api.Request
import com.uxi.bambupaymerchant.api.WebService
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 8/3/20.
 * hunterxer31@gmail.com
 */
@Singleton
class AcceptTransactionRepository @Inject
constructor(private val webService: WebService) {

    fun loadAcceptCashIn(request: Request) : Flowable<GenericApiResponse<Void>> {
        return webService.acceptCashIn(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadAcceptCashOut(request: Request) : Flowable<GenericApiResponse<Void>> {
        return webService.acceptCashOut(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadAcceptSendMoney(request: Request) : Flowable<GenericApiResponse<Void>> {
        return webService.acceptSendMoney(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}