package com.uxi.bambupaymerchant.api

import com.uxi.bambupaymerchant.model.TokenResponse
import com.uxi.bambupaymerchant.model.User
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Eraño Payawal on 7/27/20.
 * hunterxer31@gmail.com
 */
interface WebService {

    @POST("token")
    fun getToken(@Body map: HashMap<String, String>): Flowable<TokenResponse>

    @POST("merchant/login")
    fun login(@Body params: Request): Flowable<GenericApiResponse<User>>

}