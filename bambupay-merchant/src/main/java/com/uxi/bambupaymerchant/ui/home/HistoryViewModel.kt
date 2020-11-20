package com.uxi.bambupaymerchant.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.model.History
import com.uxi.bambupaymerchant.model.ResultWithMessage
import com.uxi.bambupaymerchant.model.Transaction
import com.uxi.bambupaymerchant.repository.HistoryRepository
import com.uxi.bambupaymerchant.utils.Utils
import com.uxi.bambupaymerchant.viewmodel.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository,
    private val utils: Utils
) : BaseViewModel() {

    private val _recentHistory = MutableLiveData<List<Transaction>>()
    val recentHistory: LiveData<List<Transaction>> = _recentHistory

//    private var lastId: String = ""
    private var lastTransactionId: String = ""

    private fun resultState(result: ResultWithMessage<History>) {
        when (result) {

            is ResultWithMessage.Success -> {
                lastTransactionId = if (result.value.lastId.isNullOrEmpty()) {
                    ""
                } else {
                    result.value.lastId!!
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

    fun subscribeHistory(isPullToRefresh: Boolean) {
        if (isPullToRefresh) {
            _isLoading.value = true
//            lastId = ""
            lastTransactionId = ""
        }

        disposable?.add(
            repository.loadHistory(lastTransactionId, isPullToRefresh)
                .doFinally { _isLoading.value = false }
                .subscribe({
                    resultState(it)
                }, Timber::e)
        )
    }

    fun getRecentHistory() {
        disposable?.add(
            repository.history()
                .map { list ->
                    list.sortedByDescending { it.dateCreated }.take(10)
                }
                .subscribe({
                    _recentHistory.postValue(it)
                }, Timber::e)
        )
    }

    fun getHistory() {
        disposable?.add(
            repository.history()
                .map { list ->
                    list.sortedByDescending { it.dateCreated }
                }
                .subscribe({
                    _recentHistory.postValue(it)
                }, Timber::e)
        )
    }

}