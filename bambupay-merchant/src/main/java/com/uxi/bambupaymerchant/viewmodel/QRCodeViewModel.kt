package com.uxi.bambupaymerchant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.api.Request
import com.uxi.bambupaymerchant.lookup.TxDetails
import com.uxi.bambupaymerchant.model.QuickPayScanQr
import com.uxi.bambupaymerchant.model.ResultWithMessage
import com.uxi.bambupaymerchant.model.ScanQr
import com.uxi.bambupaymerchant.repository.QrCodeRepository
import com.uxi.bambupaymerchant.utils.Utils
import io.reactivex.rxkotlin.addTo
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
    private val _txDetails = MutableLiveData<TxDetails>()
    val txDetails: LiveData<TxDetails> = _txDetails

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

        repository.loadAcceptPayQr(requestBuilder)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)

    }

    fun subscribeCreatePayQr(amount: String?) {
        if (amount.isNullOrEmpty()) {
            isAmountEmpty.value = true
            return
        }

        val request = Request.Builder()
            .setAmount(amount).build()

        repository.loadCreatePayQr(request)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)
    }

    fun subscribeTxDetails(refIdNumber: String?) {
        if (refIdNumber.isNullOrEmpty()) return

        repository.loadTxDetails(refIdNumber)
            .doOnSubscribe { loading.value = true }
            .doAfterTerminate { loading.value = false }
            .subscribe({
                resultState(it)
            }, Timber::e)
            .addTo(disposable)
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
                    is TxDetails -> {
                        val txDetails = t.value as TxDetails
                        _txDetails.value = txDetails
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