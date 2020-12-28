package com.uxi.bambupaymerchant.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 12/28/20.
 * hunterxer31@gmail.com
 */
class OtcCashIn : Serializable {

    @SerializedName("tx_amount")
    var amount: String? = null

    @SerializedName("tx_fee")
    var fee: String? = null

    @SerializedName("tx_total_amount")
    var totalAmount: Int = 0

    @SerializedName("transaction_id")
    var transactionId: String? = null

    @SerializedName("sender_ref_id")
    var senderRefId: String? = null

    var timestamp: String? = null

}