package com.uxi.bambupaymerchant.api

import com.uxi.bambupaymerchant.lookup.TxDetails
import com.uxi.bambupaymerchant.model.*
import com.uxi.bambupaymerchant.model.paynamics.Paynamics
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
    fun acceptCashIn(@Body params: Request): Flowable<GenericApiResponse<OtcCashIn>>

    @POST("merchant/accept/cash-out")
    fun acceptCashOut(@Body params: Request): Flowable<GenericApiResponse<Void>>

    @POST("transactions/merchant/createpayqr/accept")
    fun acceptPayQr(@Body params: Request): Flowable<GenericApiResponse<QuickPayScanQr>>

    @POST("transactions/merchant/scanpayqr/create")
    fun createPayQr(@Body params: Request): Flowable<GenericApiResponse<ScanQr>>

    @GET("merchants/balance")
    fun balance(): Flowable<GenericApiResponse<Balance>>

    @GET("merchants/transactions/{transactionId}")
    fun history(@Path("transactionId") transactionId: String): Flowable<GenericApiResponse<History>>
//    fun history(@Path(value = "transactionId", encoded = true) transactionId: String): Flowable<GenericApiResponse<History>>

    @POST("transactions/merchant/top-up")
    fun cashInPaynamics(@Body payload: HashMap<String, String>): Flowable<GenericApiResponse<Paynamics>>

    @POST("transactions/merchant/top-up")
    fun cashInPaynamics(@Body params: Request): Flowable<GenericApiResponse<Paynamics>>

    @GET("lookup/merchant/tx/{refIdNumber}")
    fun getTxDetails(@Path("refIdNumber") refIdNumber: String): Flowable<GenericApiResponse<TxDetails>>

}