package com.uxi.bambupaymerchant.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
open class Balance : RealmObject() {

    @PrimaryKey
    @SerializedName("account_number")
    var accountNumber: String? = null

    @SerializedName("balance")
    var balance: String? = null

}