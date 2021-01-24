package com.uxi.bambupaymerchant.utils

import android.os.Build
import android.text.TextUtils


/**
 * Created by Eraño Payawal on 1/22/21.
 * hunterxer31@gmail.com
 */
fun getDeviceName(): String {
    val manufacturer: String = Build.MANUFACTURER
    val model: String = Build.MODEL
    return if (model.startsWith(manufacturer)) {
        capitalize(model)
    } else "${capitalize(manufacturer)} $model"//capitalize(manufacturer) + " " + model
}

private fun capitalize(str: String): String {
    if (TextUtils.isEmpty(str)) {
        return str
    }
    val arr = str.toCharArray()
    var capitalizeNext = true
    val phrase = StringBuilder()
    for (c in arr) {
        if (capitalizeNext && Character.isLetter(c)) {
            phrase.append(Character.toUpperCase(c))
            capitalizeNext = false
            continue
        } else if (Character.isWhitespace(c)) {
            capitalizeNext = true
        }
        phrase.append(c)
    }
    return phrase.toString()
}

fun isSunmiDevice(): Boolean {
    return getDeviceName() == Constants.SUNMI_P2_EU
}