package com.uxi.bambupaymerchant.viewmodel

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.api.Request
import com.uxi.bambupaymerchant.model.OtcCashIn
import com.uxi.bambupaymerchant.model.QuickPayScanQr
import com.uxi.bambupaymerchant.model.ResultWithMessage
import com.uxi.bambupaymerchant.model.ScanQr
import com.uxi.bambupaymerchant.repository.QrCodeRepository
import com.uxi.bambupaymerchant.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 10/12/20.
 * hunterxer31@gmail.com
 */
class QRCodeViewModel @Inject
constructor(private val repository: QrCodeRepository, private val utils: Utils) : BaseViewModel() {

    val isTransactionNumberEmpty = MutableLiveData<Boolean>()
    val quickPaySuccessMsg = MutableLiveData<String>()

    val isAmountEmpty = MutableLiveData<Boolean>()

    private val _successMessage = MutableLiveData<String>()
    private val _quickPayData = MutableLiveData<QuickPayScanQr>()
    private val _createPayQrData = MutableLiveData<ScanQr>()

    val quickPayQrWithMessage: MediatorLiveData<Pair<String?, QuickPayScanQr?>> = MediatorLiveData<Pair<String?, QuickPayScanQr?>>()
        .apply {
            addSource(_successMessage) { message ->
                this.value = this.value?.copy(first = message) ?: Pair(message, null)
            }
            addSource(_quickPayData) {
                this.value = this.value?.copy(second = it) ?: Pair(null, it)
            }
        }

    val createPayQrWithMessage: MediatorLiveData<Pair<String?, ScanQr?>> = MediatorLiveData<Pair<String?, ScanQr?>>()
        .apply {
            addSource(_successMessage) { message ->
                this.value = this.value?.copy(first = message) ?: Pair(message, null)
            }
            addSource(_createPayQrData) {
                this.value = this.value?.copy(second = it) ?: Pair(null, it)
            }
        }

    fun subscribeScanPayQr(refIdNumber: String) {
        if (refIdNumber.isNullOrEmpty()) {
            isTransactionNumberEmpty.value = true
            return
        }

        val requestBuilder = Request.Builder()
            .setSenderRefId(refIdNumber).build()

        disposable?.add(repository.loadAcceptPayQr(requestBuilder)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                resultState(it)
                /*if (it.response != null) {
                    quickPaySuccessMsg.value = it.successMessage
                    quickPayData.value = it.response
                } else {
                    it.message?.let { error ->
                        errorMessage.value = error
                        Log.e("DEBUG", "error message:: $error")
                    }
                    it.successMessage?.let { message ->
                        successMessage.value = message
                    }
                }*/

            }, Timber::e)
        )

    }

    fun subscribeCreatePayQr(amount: String?) {
        if (amount.isNullOrEmpty()) {
            isAmountEmpty.value = true
            return
        }

        val request = Request.Builder()
            .setAmount(amount).build()

        disposable?.add(repository.loadCreatePayQr(request)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                /*if (it.response != null) {
                    it.response?.let { it1 ->
                        createPayQrData.value = it1
                    }
                } else {
                    it.message?.let { error ->
                        errorMessage.value = error
                        Log.e("DEBUG", "error message:: $error")
                    }
                }*/
                resultState(it)
            }, Timber::e)
        )
    }

    private fun <T : Any> resultState(t: ResultWithMessage<T>) {
        when (t) {
            is ResultWithMessage.Success -> {
                when (t.value) {
                    is QuickPayScanQr -> {
                        val quickPayScanQr = t.value as QuickPayScanQr
                        _quickPayData.postValue(quickPayScanQr)
                        _successMessage.postValue(t.message)
                    }
                    is ScanQr -> {
                        val createPayScanQr = t.value as ScanQr
                        _createPayQrData.postValue(createPayScanQr)
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

}