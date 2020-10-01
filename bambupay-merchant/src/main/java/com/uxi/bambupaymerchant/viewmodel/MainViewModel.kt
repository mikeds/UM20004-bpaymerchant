package com.uxi.bambupaymerchant.viewmodel

import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.repository.MainRepository
import com.uxi.bambupaymerchant.utils.Utils
import com.uxi.bambupaymerchant.utils.buildName
import com.uxi.bambupaymerchant.utils.getNameInitial
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 6/29/20.
 * hunterxer31@gmail.com
 */
class MainViewModel @Inject
constructor(private val repository: MainRepository, private val utils: Utils) : BaseViewModel() {

    val initials = MutableLiveData<String>()
    val avatarUrl = MutableLiveData<String>()
    val fullName = MutableLiveData<String>()
    val mobileNumber = MutableLiveData<String>()
    val qrCode = MutableLiveData<String>()

    fun getCurrUser() {
        val user = repository.loadCurrentUser()
        user?.let {
            initials.value = getNameInitial(it.firstName!!, it.lastName!!)
            avatarUrl.value = it.avatarImageUrl
            fullName.value = buildName(it.firstName!!, it.lastName!!)
            mobileNumber.value = it.mobileNumber
            qrCode.value = it.qrCode
        }
    }

}