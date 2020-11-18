package com.uxi.bambupaymerchant.repository

import com.uxi.bambupaymerchant.api.WebService
import com.uxi.bambupaymerchant.db.HistoryDao
import com.uxi.bambupaymerchant.model.History
import com.uxi.bambupaymerchant.model.ResultWithMessage
import com.uxi.bambupaymerchant.model.Transaction
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
@Singleton
class HistoryRepository @Inject constructor(
    private val historyDao: HistoryDao,
    private val webService: WebService
) : BaseRepository() {

    fun loadHistory(
        lastId: String,
        deleteOldData: Boolean
    ): Flowable<ResultWithMessage<History>> {
        return webService.history(lastId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                when (val obj: History? = it.response) {
                    null -> ResultWithMessage.Error(false, null)
                    else -> {

                        if (deleteOldData) {
                            historyDao.saveNewData(obj)
                        } else {
                            historyDao.copyOrUpdate(obj)
                        }

                        ResultWithMessage.Success(obj, it.successMessage)
                    }
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

    fun history(): Flowable<List<Transaction>> {
        return historyDao.getGroups()
    }
}