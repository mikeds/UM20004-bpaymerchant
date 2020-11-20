package com.uxi.bambupaymerchant.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uxi.bambupaymerchant.api.GenericApiResponse
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import timber.log.Timber

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
abstract class BaseViewModel : ViewModel() {

    protected var disposable: CompositeDisposable? = null
    val error = MutableLiveData<Error>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>(false)
    val isSuccess = MutableLiveData<Boolean>()

    protected val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    protected val _refreshToken = MutableLiveData<Boolean>()
    val refreshToken: LiveData<Boolean> = _refreshToken

    init {
        disposable = CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable?.clear()
            disposable = null
        }
    }

    fun refreshToken(error: Throwable) : Boolean {
        if (error is HttpException) {
            error.response()?.let { res ->
                return when {
                    res.code() == 403 || res.code() == 401 -> {
                        Log.e("DEBUG", "token expired.")
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
        return false
    }

    fun errorHandling(error: Throwable) {
        if (error is HttpException) {
            error.response()?.let { res ->
                res.errorBody()?.let { body ->
                    val responseBody = GenericApiResponse.create<Any>(body.string())
                    when (responseBody.error) {
                        true -> {
                            Log.e("DEBUG", "ERROR message:: ${responseBody.message}")
                        }
                        else -> Timber.e(error)
                    }
                }
            }
        }
    }

}