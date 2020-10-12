package com.uxi.bambupaymerchant.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.api.Request
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
    val successMessage = MutableLiveData<String>()
    val quickPayData = MutableLiveData<ScanQr>()
    val quickPaySuccessMsg = MutableLiveData<String>()

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
                if (it.response != null) {
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