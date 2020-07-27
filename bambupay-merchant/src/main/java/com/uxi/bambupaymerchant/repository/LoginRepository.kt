package com.uxi.bambupaymerchant.repository

import com.uxi.bambupaymerchant.api.GenericApiResponse
import com.uxi.bambupaymerchant.api.Request
import com.uxi.bambupaymerchant.api.WebService
import com.uxi.bambupaymerchant.db.UserDao
import com.uxi.bambupaymerchant.model.TokenResponse
import com.uxi.bambupaymerchant.model.User
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
@Singleton
class LoginRepository @Inject
constructor(
    private val userDao: UserDao,
    private val webService: WebService
) {

    fun loadToken() : Flowable<TokenResponse> {
        val map = HashMap<String, String>()
        map["grant_type"] = "client_credentials"
        return webService.getToken(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun loadLogin(request: Request) : Flowable<GenericApiResponse<User>> {
        return webService.login(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveUser(user: User) {
        userDao.copyOrUpdate(user)
    }

}