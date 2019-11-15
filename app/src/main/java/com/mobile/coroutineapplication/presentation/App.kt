package com.mobile.coroutineapplication.presentation

import android.app.Application
import com.facebook.stetho.Stetho
import com.mobile.coroutineapplication.presentation.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}