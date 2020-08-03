package com.uxi.bambupaymerchant.di.module

import com.uxi.bambupaymerchant.view.activity.CashInActivity
import com.uxi.bambupaymerchant.view.activity.LoginActivity
import com.uxi.bambupaymerchant.view.activity.MainActivity
import com.uxi.bambupaymerchant.view.activity.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Eraño Payawal on 7/27/20.
 * hunterxer31@gmail.com
 */
@Suppress("unused")
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    internal abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [MainActivityFragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeCashInActivity(): CashInActivity

}