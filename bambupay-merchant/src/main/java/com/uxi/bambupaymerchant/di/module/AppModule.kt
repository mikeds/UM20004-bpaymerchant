package com.uxi.bambupaymerchant.di.module

import android.app.Application
import androidx.annotation.NonNull
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.uxi.bambupaymerchant.BuildConfig
import com.uxi.bambupaymerchant.BuildConfig.API_BASE_URL
import com.uxi.bambupaymerchant.api.AuthenticationInterceptor
import com.uxi.bambupaymerchant.api.WebService
import com.uxi.bambupaymerchant.utils.Utils
import dagger.Module
import dagger.Provides
import io.realm.RealmObject
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Eraño Payawal on 7/27/20.
 * hunterxer31@gmail.com
 */
@Module(includes = [
    ViewModelModule::class
])
class AppModule {

    @Provides
    @Singleton
    fun providesUtils(application: Application): Utils = Utils(application)

    @Provides
    @Singleton
    fun provideInterceptor(utils: Utils): AuthenticationInterceptor {
        return AuthenticationInterceptor(utils)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthenticationInterceptor): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(3, 10, TimeUnit.MINUTES))
            .addInterceptor(interceptor)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.networkInterceptors().add(logging)
        }

        return httpClientBuilder.build()
    }

    @Provides
    @Singleton
    @NonNull
    fun provideWebService(httpClient: OkHttpClient): WebService {
        //add logger
        val gson = GsonBuilder().setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.declaringClass == RealmObject::class.java
            }

            override fun shouldSkipClass(clazz: Class<*>): Boolean {
                return false
            }
        }).create()

//        Log.e("DEBUG", "baseUrl:: ".plus(baseUrl))
        Timber.tag("DEBUG").e(API_BASE_URL)

        //add retro builder
        val retroBuilder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        retroBuilder.client(httpClient)

        //create retrofit - only this instance would be used in the entire application
        return retroBuilder.build().create(WebService::class.java)
    }

}