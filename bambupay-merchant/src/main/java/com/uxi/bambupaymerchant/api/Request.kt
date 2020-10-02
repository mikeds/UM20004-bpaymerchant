package com.uxi.bambupaymerchant.api

import com.google.gson.annotations.SerializedName

/**
 * Created by Eraño Payawal on 7/13/20.
 * hunterxer31@gmail.com
 */
open class Request private constructor(
    private val merchant: String?,

    private val amount: String?,

    @SerializedName("send_to")
    private val sentTo: String?,

    private val username: String?,

    private val password: String?,

    private val code: String?,

    @SerializedName("transaction_number")
    private val transactionNumber: String?,

    @SerializedName("sender_ref_id")
    private val senderRefId: String?) {

    data class Builder(
        private var merchant: String? = null,

        private var amount: String? = null,

        @SerializedName("send_to")
        private var sentTo: String? = null,

        private var username: String? = null,

        private var password: String? = null,

        private var code: String? = null,

        @SerializedName("transaction_number")
        private var transactionNumber: String? = null,

        @SerializedName("sender_ref_id")
        private var senderRefId: String? = null) {

        fun setUsername(username: String) = apply { this.username = username }
        fun setCode(code: String) = apply { this.code = code }
        fun setPassword(password: String?) = apply { this.password = password }
        fun setMerchant(merchant: String) = apply { this.merchant = merchant }
        fun setAmount(amount: String) = apply { this.amount = amount }
        fun setSendTo(sentTo: String?) = apply { this.sentTo = sentTo }
        fun setTransactionNumber(transactionNumber: String?) = apply { this.transactionNumber = transactionNumber }
        fun setSenderRefId(senderRefId: String?) = apply { this.senderRefId = senderRefId }

        fun build() = Request(merchant, amount, sentTo, username, password, code, transactionNumber, senderRefId)
    }

}