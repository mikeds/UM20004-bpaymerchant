package com.uxi.bambupaymerchant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.model.ResultWithMessage
import com.uxi.bambupaymerchant.model.paynamics.Paynamics
import com.uxi.bambupaymerchant.repository.CashInRepository
import com.uxi.bambupaymerchant.utils.Constants
import com.uxi.bambupaymerchant.utils.Utils
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/15/20.
 * hunterxer31@gmail.com
 */
class CashInViewModel @Inject
constructor(private val repository: CashInRepository, private val utils: Utils) : BaseViewModel() {

    val isAmountEmpty = MutableLiveData<Boolean>()
    private val _paynamicsData = MutableLiveData<Paynamics>()
    val paynamicsData: LiveData<Paynamics> = _paynamicsData
    private val _successMessage = MutableLiveData<String>()

    fun subscribeCashInPaynamics(amount: String?, cardNum: String?, cardMonthYear: String?, cvv: String?, cardName: String?) {
        if (amount.isNullOrEmpty() || cardNum.isNullOrEmpty() || cardMonthYear.isNullOrEmpty() || cvv.isNullOrEmpty() || cardName.isNullOrEmpty()) {
            errorMessage.value = "All fields are required!"
            return
        }

        if (cardMonthYear.isNullOrEmpty() || !cardMonthYear.contains("/")) {
            errorMessage.value = "Invalid card expiration"
            Timber.tag("DEBUG").e("invalid card expiration")
            return
        }

        val tempCardMonYr = cardMonthYear.split("/")
        val month = tempCardMonYr[0]
        val year = tempCardMonYr[1]

        if (month.isEmpty() || year.isEmpty()) {
            Timber.tag("DEBUG").e("month or year empty")
            return
        }

        repository.loadCashInPaynamics(amount, cardName, cardNum, month, year, cvv)
            .doOnSubscribe { _isLoading.value = true }
            .doOnComplete { _isLoading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)
    }

    private fun <T : Any> resultState(t: ResultWithMessage<T>) {
        when (t) {
            is ResultWithMessage.Success -> {
                when (t.value) {
                    is Paynamics -> {
                        val paynamics = t.value as Paynamics
                        _paynamicsData.postValue(paynamics)
                        _successMessage.postValue(t.message)
                    }
                }
            }
            is ResultWithMessage.Error -> {
                if (t.refresh) {
                    utils.saveUserTokenPack("", true)
                    isSuccess.value = false
                } else {
                    errorMessage.value = t.message
                }
                loading.value = false
                isSuccess.value = false
                _isLoading.value = false
            }
        }
    }

    fun subscribeCashInPaynamics(amount: String?, fromScreen: String?) {
        if (amount.isNullOrEmpty()) {
            isAmountEmpty.value = true
            return
        }

        if (fromScreen.isNullOrEmpty()) {
            return
        }

        val type = when (fromScreen) {
            Constants.CASH_IN_CARD_SCREEN -> Constants.CASH_IN_PAYNAMICS_CC
            Constants.CASH_IN_BANCNET_SCREEN -> Constants.CASH_IN_PAYNAMICS_BANCNET
            Constants.CASH_IN_GRAB_SCREEN -> Constants.CASH_IN_PAYNAMICS_GRAB_PAY
            Constants.CASH_IN_GCASH_SCREEN -> Constants.CASH_IN_PAYNAMICS_GCASH
            Constants.CASH_IN_PAYMAYA_SCREEN -> Constants.CASH_IN_PAYNAMICS_PAYMAYA
            else -> ""
        }

        repository.loadCashInPaynamics(type, amount)
            .doOnSubscribe { _isLoading.value = true }
            .doOnComplete { _isLoading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)

    }

}