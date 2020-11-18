package com.uxi.bambupaymerchant.db

import com.uxi.bambupaymerchant.db.realm.unmanaged
import com.uxi.bambupaymerchant.model.History
import com.uxi.bambupaymerchant.model.Transaction
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.kotlin.where

/**
 * Created by Eraño Payawal on 11/18/20.
 * hunterxer31@gmail.com
 */
class HistoryDao(val realm: Realm) {

    fun copyOrUpdate(obj: History) {
        realm.executeTransaction { realm1 ->
            realm1.copyToRealmOrUpdate(obj)
        }
    }

    fun saveNewData(obj: History) {
        realm.executeTransaction { realm1 ->
            realm1.where<History>()
                .findAll()
                .deleteAllFromRealm()
            realm1.insertOrUpdate(obj)
        }
    }

    fun getGroups(): Flowable<List<Transaction>> {
        return realm.where<Transaction>()
            .findAllAsync()
            .asFlowable()
            .map { it.unmanaged() }
    }
}