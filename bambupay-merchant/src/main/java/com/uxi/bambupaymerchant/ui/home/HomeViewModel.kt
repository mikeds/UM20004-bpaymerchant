package com.uxi.bambupaymerchant.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.model.Balance
import com.uxi.bambupaymerchant.model.ResultWithMessage
import com.uxi.bambupaymerchant.repository.HomeRepository
import com.uxi.bambupaymerchant.utils.Utils
import com.uxi.bambupaymerchant.viewmodel.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val utils: Utils
) : BaseViewModel() {

    private val _balanceData = MutableLiveData<String>()
    val balanceData: LiveData<String> = _balanceData

    private fun resultState(result: ResultWithMessage<Balance>) {
        when (result) {
            is ResultWithMessage.Success -> {
                result.value.balance?.let {
                    val balFormat = utils.currencyFormat(it)
                    _balanceData.postValue(balFormat)
                }
            }
            is ResultWithMessage.Error -> {
                if (result.refresh) {
                    utils.saveUserTokenPack("", true)
                }
                _refreshToken.postValue(result.refresh)
            }
        }
    }

    fun subscribeBalance() {
        disposable?.add(
            repository.loadBalance()
                .doOnSubscribe { _isLoading.value = true }
                .doOnComplete { _isLoading.value = false }
                .subscribe({
                    resultState(it)
                }, Timber::e)
        )
    }

}