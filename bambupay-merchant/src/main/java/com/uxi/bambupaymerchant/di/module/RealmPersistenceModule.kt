package com.uxi.bambupaymerchant.di.module

import com.uxi.bambupaymerchant.db.UserDao
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 7/27/20.
 * hunterxer31@gmail.com
 */
@Module
class RealmPersistenceModule {

    @Provides
    @Singleton
    fun getDatabase(): Realm {
        return Realm.getDefaultInstance()
    }

    @Provides
    @Singleton
    fun provideUserDao(realm: Realm): UserDao {
        return UserDao(realm)
    }

//    @Provides
//    @Singleton
//    fun provideHistoryDao(realm: Realm): HistoryDao {
//        return HistoryDao(realm)
//    }

}