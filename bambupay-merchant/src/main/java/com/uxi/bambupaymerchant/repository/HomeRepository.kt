package com.uxi.bambupaymerchant.repository

import com.uxi.bambupaymerchant.api.WebService
import com.uxi.bambupaymerchant.db.UserDao
import com.uxi.bambupaymerchant.model.Balance
import com.uxi.bambupaymerchant.model.ResultWithMessage
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
class HomeRepository @Inject constructor(
    private val userDao: UserDao,
    private val webService: WebService
) : BaseRepository() {

    fun loadBalance(): Flowable<ResultWithMessage<Balance>> {
        return webService.balance()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                when (val obj: Balance? = it.response) {
                    null -> ResultWithMessage.Error(false, null)
                    else -> {
                        userDao.saveNewBalance(obj)
                        ResultWithMessage.Success(obj, it.successMessage)
                    }
                }
            }
            .onErrorReturn { errorHandler(it) }
    }

}