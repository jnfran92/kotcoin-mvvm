package com.jnfran92.kotcoin.di.component

import com.jnfran92.kotcoin.controller.CryptoController
import com.jnfran92.kotcoin.di.PerActivity
import com.jnfran92.kotcoin.di.module.ActivityModule
import com.jnfran92.kotcoin.di.module.CryptoModule
import com.jnfran92.kotcoin.view.activity.MainActivity
import com.jnfran92.model.CryptoModel
import dagger.Component

@PerActivity
@Component(dependencies = [ApplicationComponent::class],
    modules =[CryptoModule::class, ActivityModule::class])
interface CryptoComponent: ActivityComponent {

    // Inject Main Activity
    fun inject(mainActivity: MainActivity)
}