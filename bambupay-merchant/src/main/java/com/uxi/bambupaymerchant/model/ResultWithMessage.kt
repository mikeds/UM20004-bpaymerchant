package com.uxi.bambupaymerchant.model

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
sealed class ResultWithMessage<out T : Any> {

    data class Success<out T : Any>(val value: T, val message: String?) : ResultWithMessage<T>()

    data class Error(val refresh: Boolean, val message: String?, val cause: Exception? = null) : ResultWithMessage<Nothing>()

}