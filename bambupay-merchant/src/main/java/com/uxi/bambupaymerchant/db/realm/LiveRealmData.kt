package com.uxi.bambupaymerchant.db.realm

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
class LiveRealmData<T : RealmModel>(private val results: RealmResults<T>) : LiveData<RealmResults<T>>() {
    private val listener = RealmChangeListener<RealmResults<T>> {
            results -> value = results
    }

    override fun onActive() {
        results.addChangeListener(listener)
        value = results
    }

    override fun onInactive() {
        results.removeChangeListener(listener)
    }
}