package com.uxi.bambupaymerchant.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.api.Request
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
    val successMessage = MutableLiveData<String>()

    fun subscribeCashIn(transactionNumber: String?) {
        if (transactionNumber.isNullOrEmpty()) {
            isTransactionNumberEmpty.value = true
            return
        }

        val requestBuilder = Request.Builder()
            .setTransactionNumber(transactionNumber).build()

        disposable?.add(repository.loadAcceptCashIn(requestBuilder)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.response != null) {

                } else {
                    it.message?.let { error ->
                        errorMessage.value = error
                        Log.e("DEBUG", "error message:: $error")
                    }
                    it.successMessage?.let { message ->
                        successMessage.value = message
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

    fun subscribeCashOut(transactionNumber: String?) {
        if (transactionNumber.isNullOrEmpty()) {
            isTransactionNumberEmpty.value = true
            return
        }

        val requestBuilder = Request.Builder()
            .setTransactionNumber(transactionNumber).build()

        disposable?.add(repository.loadAcceptCashOut(requestBuilder)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.response != null) {

                } else {
                    it.message?.let { error ->
                        errorMessage.value = error
                        Log.e("DEBUG", "error message:: $error")
                    }
                    it.successMessage?.let { message ->
                        successMessage.value = message
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
            .setTransactionNumber(transactionNumber).build()

        disposable?.add(repository.loadAcceptSendMoney(requestBuilder)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                if (it.response != null) {

                } else {
                    it.message?.let { error ->
                        errorMessage.value = error
                        Log.e("DEBUG", "error message:: $error")
                    }
                    it.successMessage?.let { message ->
                        successMessage.value = message
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