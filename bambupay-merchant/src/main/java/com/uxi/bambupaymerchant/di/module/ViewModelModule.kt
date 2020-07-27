package com.uxi.bambupaymerchant.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uxi.bambupay.di.factory.ViewModelKey
import com.uxi.bambupaymerchant.di.factory.ViewModelFactory
import com.uxi.bambupaymerchant.viewmodel.LoginViewModel
import com.uxi.bambupaymerchant.viewmodel.MainViewModel
import com.uxi.bambupaymerchant.viewmodel.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Eraño Payawal on 7/27/20.
 * hunterxer31@gmail.com
 */
@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(loginViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    internal abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(UserTokenViewModel::class)
//    internal abstract fun bindUserTokenViewModel(userTokenViewModel: UserTokenViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}