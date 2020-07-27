package com.uxi.bambupaymerchant.repository

import com.uxi.bambupaymerchant.db.UserDao
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 7/12/20.
 * hunterxer31@gmail.com
 */
class SettingsRepository @Inject constructor(private val userDao: UserDao)  {

    fun resetDb() {
        userDao.deleteAll()
    }

}