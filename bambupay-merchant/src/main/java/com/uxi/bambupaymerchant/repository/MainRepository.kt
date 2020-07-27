package com.uxi.bambupaymerchant.repository

import com.uxi.bambupaymerchant.db.UserDao
import com.uxi.bambupaymerchant.model.User
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 6/29/20.
 * hunterxer31@gmail.com
 */
class MainRepository @Inject constructor(private val userDao: UserDao) {

    fun loadCurrentUser(): User? {
        return userDao.getCurrentUser()
    }

}