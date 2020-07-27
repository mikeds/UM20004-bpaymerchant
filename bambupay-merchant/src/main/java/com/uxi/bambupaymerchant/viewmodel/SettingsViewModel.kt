package com.uxi.bambupaymerchant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.repository.SettingsRepository
import com.uxi.bambupaymerchant.utils.Utils
import javax.inject.Inject

class SettingsViewModel @Inject
constructor(private val repository: SettingsRepository, private val utils: Utils) : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text

    fun deleteLocalData() {
        repository.resetDb()
        utils.clearPref()
    }
}