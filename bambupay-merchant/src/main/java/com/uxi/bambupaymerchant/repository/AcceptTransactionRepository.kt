package com.uxi.bambupaymerchant.repository

import com.uxi.bambupaymerchant.api.GenericApiResponse
import com.uxi.bambupaymerchant.api.Request
import com.uxi.bambupaymerchant.api.WebService
import com.uxi.bambupaymerchant.model.OtcCashIn
import com.uxi.bambupaymerchant.model.ResultWithMessage
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
constructor(private val webService: WebService): BaseRepository() {

    fun loadAcceptCashIn(request: Request) : Flowable<ResultWithMessage<OtcCashIn>> {
        return webService.acceptCashIn(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: OtcCashIn? = res.response) {
                    null -> ResultWithMessage.Error(false, res?.message)
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
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