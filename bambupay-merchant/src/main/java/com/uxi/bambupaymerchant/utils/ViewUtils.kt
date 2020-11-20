package com.uxi.bambupaymerchant.utils

import android.view.View

fun View.makeVisibleOrHide(condition: Boolean) = when {
    condition -> this.visibility = View.VISIBLE
    else -> this.visibility = View.INVISIBLE
}

fun View.makeVisibleOrGone(condition: Boolean) = when {
    condition -> this.visibility = View.VISIBLE
    else -> this.visibility = View.GONE
}

fun View.makeGone(condition: Boolean) {
    if (condition) this.visibility = View.GONE
}


















