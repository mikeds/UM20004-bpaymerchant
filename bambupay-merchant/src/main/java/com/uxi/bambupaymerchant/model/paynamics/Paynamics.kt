package com.uxi.bambupaymerchant.model.paynamics

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eraño Payawal on 12/22/20.
 * hunterxer31@gmail.com
 */
open class Paynamics : Serializable {

    @SerializedName("sender_ref_id")
    var senderRefId: String? = null

    @SerializedName("qr_code")
    var qrCode: String? = null

    var timestamp: String? = null

    @SerializedName("gateway_message")
    var gatewayMessage: String? = null

    @SerializedName("redirect")
    var redirectUrl: String? = null

}