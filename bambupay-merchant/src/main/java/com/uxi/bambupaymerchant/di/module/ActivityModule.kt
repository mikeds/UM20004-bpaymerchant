package com.uxi.bambupaymerchant.di.module

import com.uxi.bambupaymerchant.ui.history.TransactionDetailsActivity
import com.uxi.bambupaymerchant.ui.history.TransactionHistoryActivity
import com.uxi.bambupaymerchant.view.activity.*
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

    @ContributesAndroidInjector()
    internal abstract fun contributeRegisterActivity(): RegisterActivity

    @ContributesAndroidInjector(modules = [MainActivityFragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeCashInActivity(): CashInActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeForgotPasswordActivity(): ForgotPasswordActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeScanPayQrCodeActivity(): ScanPayQrCodeActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeCreateQRActivity(): CreateQRActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeTransactionHistoryActivity(): TransactionHistoryActivity

    @ContributesAndroidInjector()
    internal abstract fun contributeTransactionDetailsActivity(): TransactionDetailsActivity

}