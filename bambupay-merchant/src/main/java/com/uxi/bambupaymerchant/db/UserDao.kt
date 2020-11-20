package com.uxi.bambupaymerchant.db

import com.uxi.bambupaymerchant.model.Balance
import com.uxi.bambupaymerchant.model.User
import io.realm.Realm
import io.realm.kotlin.where

/**
 * Created by Eraño Payawal on 6/28/20.
 * hunterxer31@gmail.com
 */
class UserDao(val realm: Realm) {

    fun copyOrUpdate(obj: User) {
        realm.executeTransaction { realm1 ->
            realm1.copyToRealmOrUpdate(obj)
        }
    }

    fun getCurrentUser(): User? {
        val currentUser = realm.where(User::class.java).findFirst()
        currentUser?.let {
            return realm.copyFromRealm(currentUser)
        }
        return null
    }

    fun deleteAll() {
        realm.executeTransaction {
            it.deleteAll()
        }
    }

    fun copyOrUpdate(obj: Balance) {
        realm.executeTransaction { realm1 ->
            realm1.copyToRealmOrUpdate(obj)
        }
    }

    fun saveNewBalance(obj: Balance) {
        realm.executeTransaction { realm1 ->
            realm1.where<Balance>()
                .findAll()
                .deleteAllFromRealm()
            realm1.insertOrUpdate(obj)
        }
    }

    /*fun deleteBalance() {
        realm.executeTransaction {
            it.delete(Balance::class.java)
        }
    }

    fun queryBalance(): RealmResults<Balance> {
        return realm.where(Balance::class.java).findAllAsync()
    }*/

}