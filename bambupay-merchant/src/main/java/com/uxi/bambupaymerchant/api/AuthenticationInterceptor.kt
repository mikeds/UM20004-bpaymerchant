package com.uxi.bambupaymerchant.api

import android.util.Log
import com.uxi.bambupaymerchant.BuildConfig
import com.uxi.bambupaymerchant.utils.Utils
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class AuthenticationInterceptor
@Inject constructor(val utils: Utils?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder: Request.Builder

        if (BuildConfig.DEBUG) {
            Log.e("DEBUG", "TOKEN:: ${utils?.token}")
            Log.e("DEBUG", "USER TOKEN:: ${utils?.userToken}")
        }

        var token = ""
        utils?.let {
            if (!it.isLoggedIn) {
                token = if (it.token.isNullOrBlank() && it.isTokenExpired) {
                    // this is for generating token for login
                    val basicAuth = getBasicAuth()
                    "".plus(basicAuth)
                } else {
                    // this is for login only
                    "Bearer ${it.token}"
                }
            } else {
                token = if (it.userToken.isNullOrBlank() && it.isUserTokenExpired) {
                    val userAuth = Credentials.basic(it.userSecretKey, it.userSecretCode)
                    "".plus(userAuth)
                } else {
                    "Bearer ${it.userToken}"
                }
            }
        }

        if (BuildConfig.DEBUG) {
            Log.e("Authentication", "Bearer Token:: $token")
        }

        builder = original.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .header("Authorization", token)
        val request = builder.build()
        return chain.proceed(request)
    }

    private fun getBasicAuth() : String {
        val username = "ecf3662266040ba80bfa49d925f74999e56d7f80f0b3b14e430a5d526c591dcf"
        val password = "9326fdf58cfe187a90ba4a1c7bcabbbdb72afb1ad0495472a1befa6c4a0c61f9"
        return Credentials.basic(username, password)
    }

}