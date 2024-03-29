package com.uxi.bambupaymerchant.db.realm

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
fun <T: RealmModel> RealmResults<T>.asLiveData() = LiveRealmData(this)

fun <T : RealmObject> T.unmanaged(): T = when {
    this.isManaged -> this.realm.copyFromRealm(this)
    else -> this
}

fun <T: RealmObject> Iterable<T>.unmanaged(): List<T> = this.map { it.unmanaged() }