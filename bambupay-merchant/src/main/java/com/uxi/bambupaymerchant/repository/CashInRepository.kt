package com.uxi.bambupaymerchant.repository

import com.uxi.bambupaymerchant.api.Request
import com.uxi.bambupaymerchant.api.WebService
import com.uxi.bambupaymerchant.model.ResultWithMessage
import com.uxi.bambupaymerchant.model.paynamics.Paynamics
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/15/20.
 * hunterxer31@gmail.com
 */
class CashInRepository @Inject
constructor(private val webService: WebService): BaseRepository() {

    fun loadCashInPaynamics(
        amount: String,
        cardHolder: String,
        cardNumber: String,
        cardMonth: String,
        cardYear: String,
        ccv: String
    ): Flowable<ResultWithMessage<Paynamics>> {
        val requestBody = hashMapOf(
            "type" to "paynamics",
            "amount" to amount,
            "card_holder" to cardHolder,
            "card_number" to cardNumber,
            "card_month" to cardMonth,
            "card_year" to cardYear,
            "ccv" to ccv
        )
        return webService.cashInPaynamics(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: Paynamics? = res.response) {
                    null -> ResultWithMessage.Error(false, "")
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

    fun loadCashInPaynamics(type: String, amount: String): Flowable<ResultWithMessage<Paynamics>> {
        val requestBuilder = Request.Builder()
            .setType(type)
            .setAmount(amount).build()
        return webService.cashInPaynamics(requestBuilder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { res ->
                when (val obj: Paynamics? = res.response) {
                    null -> ResultWithMessage.Error(false, res?.errorMessage)
                    else -> ResultWithMessage.Success(obj, res.successMessage)
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

}