package com.uxi.bambupaymerchant.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.api.Request
import com.uxi.bambupaymerchant.model.OtcCashIn
import com.uxi.bambupaymerchant.model.ResultWithMessage
import com.uxi.bambupaymerchant.repository.AcceptTransactionRepository
import com.uxi.bambupaymerchant.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 8/3/20.
 * hunterxer31@gmail.com
 */
class AcceptTransactionViewModel @Inject
constructor(private val repository: AcceptTransactionRepository, private val utils: Utils) : BaseViewModel() {

    val isTransactionNumberEmpty = MutableLiveData<Boolean>()

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    private val _cashIn = MutableLiveData<OtcCashIn>()
    val cashInDataWithMessage: MediatorLiveData<Pair<String?, OtcCashIn?>> = MediatorLiveData<Pair<String?, OtcCashIn?>>()
        .apply {
            addSource(_successMessage) { message ->
                this.value = this.value?.copy(first = message) ?: Pair(message, null)
            }
            addSource(_cashIn) {
                this.value = this.value?.copy(second = it) ?: Pair(null, it)
            }
        }

    private fun <T : Any> resultState(t: ResultWithMessage<T>) {
        when (t) {
            is ResultWithMessage.Success -> {
                when (t.value) {
                    is OtcCashIn -> {
                        val ubpCashOut = t.value as OtcCashIn
                        _cashIn.postValue(ubpCashOut)
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

    fun subscribeCashIn(transactionNumber: String?) {
        if (transactionNumber.isNullOrEmpty()) {
            isTransactionNumberEmpty.value = true
            return
        }

        val requestBuilder = Request.Builder()
            .setSenderRefId(transactionNumber).build()

        disposable?.add(repository.loadAcceptCashIn(requestBuilder)
            .doOnSubscribe { _isLoading.value = true }
            .doOnComplete { _isLoading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
        )
    }

    fun subscribeCashOut(transactionNumber: String?) {
        if (transactionNumber.isNullOrEmpty()) {
            isTransactionNumberEmpty.value = true
            return
        }

        val requestBuilder = Request.Builder()
            .setTransactionNumber(transactionNumber).build()

        disposable?.add(repository.loadAcceptCashOut(requestBuilder)
            .doOnSubscribe { _isLoading.value = true }
            .doOnComplete { _isLoading.value = false }
            .subscribe({
                if (it.response != null) {

                } else {
                    it.errorMessage?.let { error ->
                        errorMessage.value = error
                        Log.e("DEBUG", "error message:: $error")
                    }
                    it.successMessage?.let { message ->
                        _successMessage.value = message
                    }
                }

            }, {
                Timber.e(it)
                if (refreshToken(it)) {
                    Log.e("DEBUG", "error refreshToken")
                    utils.saveUserTokenPack("", true)
                    isSuccess.value = false
                }
            })
        )
    }

    fun subscribeSendMoney(transactionNumber: String?) {
        if (transactionNumber.isNullOrEmpty()) {
            isTransactionNumberEmpty.value = true
            return
        }

        val requestBuilder = Request.Builder()
            .setSenderRefId(transactionNumber).build()

        disposable?.add(repository.loadAcceptSendMoney(requestBuilder)
            .doOnSubscribe { _isLoading.value = true }
            .doOnComplete { _isLoading.value = false }
            .subscribe({
                if (it.response != null) {

                } else {
                    it.errorMessage?.let { error ->
                        errorMessage.value = error
                        Log.e("DEBUG", "error message:: $error")
                    }
                    it.successMessage?.let { message ->
                        _successMessage.value = message
                    }
                }

            }, {
                Timber.e(it)
                if (refreshToken(it)) {
                    Log.e("DEBUG", "error refreshToken")
                    utils.saveUserTokenPack("", true)
                    isSuccess.value = false
                }
            })
        )
    }

}