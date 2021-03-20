package com.uxi.bambupaymerchant.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 10/5/20.
 * hunterxer31@gmail.com
 */
class ScanQr : Serializable {

    @SerializedName("sender_ref_id")
    var senderRefId: String? = null

    @SerializedName("qr_code")
    var qrCode: String? = null

    var amount: String? = null

    var fee: String? = null

    var timestamp: String? = null

}