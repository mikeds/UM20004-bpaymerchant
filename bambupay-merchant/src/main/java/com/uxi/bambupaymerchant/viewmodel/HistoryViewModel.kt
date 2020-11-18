package com.uxi.bambupaymerchant.viewmodel

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
class HistoryViewModel /*@Inject constructor(
    private val repository: HistoryRepository,
    private val utils: Utils
) : BaseViewModel()*/ {

//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _recentHistory = MutableLiveData<List<Transaction>>()
//    val recentHistory: LiveData<List<Transaction>> = _recentHistory

    private var lastId: String? = null

    fun subscribeHistory(isPullToRefresh: Boolean) {
//        if (isPullToRefresh) {
//            _isLoading.value = true
//            lastId = null
//        }
//
//        disposable?.add(
//            repository.loadHistory(lastId, isPullToRefresh)
//                .doFinally { _isLoading.value = false }
//                .subscribe({
//
//                }, Timber::e)
//        )
    }

    fun getRecentHistory() {
//        disposable?.add(
//            repository.history()
//                .map { list ->
//                    list.sortedBy { it.dateCreated }.take(10)
//                }
//                .subscribe({
//                    _recentHistory.postValue(it)
//                }, Timber::e)
//        )
    }

}