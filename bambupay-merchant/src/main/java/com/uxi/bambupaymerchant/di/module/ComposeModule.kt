package com.uxi.bambupaymerchant.di.module

import com.uxi.bambupaymerchant.view.activity.BaseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Eraño Payawal on 7/27/20.
 * hunterxer31@gmail.com
 */
@Suppress("unused")
@Module
abstract class ComposeModule {

    @ContributesAndroidInjector
    internal abstract fun contributeViewModelActivity(): BaseActivity

}