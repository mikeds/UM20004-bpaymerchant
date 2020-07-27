package com.uxi.bambupaymerchant.di.module

import com.uxi.bambupaymerchant.view.fragment.HomeFragment
import com.uxi.bambupaymerchant.view.fragment.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Eraño Payawal on 7/12/20.
 * hunterxer31@gmail.com
 */
@Suppress("unused")
@Module
abstract class MainActivityFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

}