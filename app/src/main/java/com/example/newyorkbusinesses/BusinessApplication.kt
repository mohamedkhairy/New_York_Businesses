package com.example.newyorkbusinesses

import android.app.Application
import com.example.newyorkbusinesses.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BusinessApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

     fun configureDi() =
        startKoin{
            androidContext(this@BusinessApplication)
            modules (provideComponent())
        }

     fun provideComponent() = appComponent
}

