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

    @POST("merchants/login")
    fun login(@Body params: Request): Flowable<GenericApiResponse<User>>

    @POST("merchant/accept")
    fun acceptSendMoney(@Body params: Request): Flowable<GenericApiResponse<Void>>

    @POST("transactions/merchant/accept-cash-in")
    fun acceptCashIn(@Body params: Request): Flowable<GenericApiResponse<Void>>

    @POST("merchant/accept/cash-out")
    fun acceptCashOut(@Body params: Request): Flowable<GenericApiResponse<Void>>

}