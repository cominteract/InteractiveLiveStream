package com.ainsigne.interactivelivestreaming

import android.app.Application
import com.ainsigne.interactivelivestreaming.di.databaseModule
import com.ainsigne.interactivelivestreaming.di.networkModule
import com.ainsigne.interactivelivestreaming.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class LiveStreamApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@LiveStreamApp)
            modules(listOf(databaseModule, networkModule,  viewModelModule))
        }


    }
}