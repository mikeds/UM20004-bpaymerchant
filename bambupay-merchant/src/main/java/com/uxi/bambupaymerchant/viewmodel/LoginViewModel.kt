package com.uxi.bambupaymerchant.viewmodel

import androidx.lifecycle.MutableLiveData
import com.uxi.bambupaymerchant.api.Request
import com.uxi.bambupaymerchant.repository.LoginRepository
import com.uxi.bambupaymerchant.utils.Constants
import com.uxi.bambupaymerchant.utils.Utils
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class LoginViewModel @Inject
constructor(private val repository: LoginRepository, private val utils: Utils) : BaseViewModel() {

    val isSuccessLoggedIn = MutableLiveData<Boolean>()
    val refreshLogin = MutableLiveData<Boolean>()
    val isEmailEmpty = MutableLiveData<Boolean>()
    val isPasswordEmpty = MutableLiveData<Boolean>()

    fun subscribeToken() {
        // Log.e("DEBUG", "TOKEN=> ${utils.token}")
        Timber.tag("DEBUG").e("isTokenExpired=> ${utils.isTokenExpired}")

        if (utils.token?.isNotEmpty()!! && !utils.isTokenExpired) return

        disposable?.add(repository.loadToken()
            .subscribe({
                it.let { token ->
                    // Log.e("DEBUG", "accessToken:: ${token.accessToken}")
                    utils.saveTokenPack(token.accessToken, false)

                    if (isSuccessLoggedIn.value == false) {
                        refreshLogin.value = true
                    }
                }
            }, {
                Timber.e(it)
            })
        )
    }

    fun subscribeLogin(username: String, password: String) {
        if (isValidateCredentials(username, password)) {

            val encryptedPassword = utils.sha256(password)
            Timber.tag("DEBUG").e("encryptedPassword:: ${encryptedPassword.toLowerCase()}")

            val requestBuilder = Request.Builder()
                .setUsername(username)
                .setPassword(encryptedPassword.toLowerCase()).build()

            disposable?.add(repository.loadLogin(requestBuilder)
                .doOnSubscribe { loading.value = true }
                .doAfterTerminate { loading.value = false }
                .subscribe({

                    if (it.response != null) {
                        it.response?.let { user ->
                            repository.saveUser(user)
                            utils.saveLoggedIn(true)
                            utils.saveUserKeyPack(user.secretKey, user.secretCode)
                            isSuccessLoggedIn.value = true
                        }
                    } else {
                        it.message?.let { error ->
                            errorMessage.value = error
                            Timber.tag("DEBUG").e("error message:: $error")
                        }
                    }

                }, {
                    Timber.e(it)
                    if (refreshToken(it)) {
                        Timber.tag("DEBUG").e("error refreshToken")
                        utils.saveTokenPack("", true)
                        isSuccessLoggedIn.value = false
                    }
//                    errorHandling(it)
                })
            )

        }
    }

    private fun isValidateCredentials(username: String?, password: String?) : Boolean {
        if (username.isNullOrEmpty()/* || !utils.isEmailValid(username)*/) {
            isEmailEmpty.value = true
            return false
        }

        if (password.isNullOrEmpty() || password.length < Constants.PASSWORD_MIN_LENGTH) {
            isPasswordEmpty.value = true
            return false
        }

        return true
    }

}