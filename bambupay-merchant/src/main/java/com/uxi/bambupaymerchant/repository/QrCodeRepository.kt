package com.uxi.bambupaymerchant.repository

import com.uxi.bambupaymerchant.api.GenericApiResponse
import com.uxi.bambupaymerchant.api.Request
import com.uxi.bambupaymerchant.api.WebService
import com.uxi.bambupaymerchant.model.OtcCashIn
import com.uxi.bambupaymerchant.model.QuickPayScanQr
import com.uxi.bambupaymerchant.model.ResultWithMessage
import com.uxi.bambupaymerchant.model.ScanQr
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 10/12/20.
 * hunterxer31@gmail.com
 */
@Singleton
class QrCodeRepository @Inject constructor(private val webService: WebService): BaseRepository() {

    fun loadAcceptPayQr(request: Request) : Flowable<ResultWithMessage<QuickPayScanQr>> {
        return webService.acceptPayQr(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: QuickPayScanQr? = res.response) {
                    null -> ResultWithMessage.Error(false, res?.message)
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

    fun loadCreatePayQr(request: Request) : Flowable<ResultWithMessage<ScanQr>> {
        return webService.createPayQr(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: ScanQr? = res.response) {
                    null -> ResultWithMessage.Error(false, res?.message)
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

}