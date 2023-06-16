package com.sedatkavak.bitcointicker.di

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BitcoinTicker : Application() {
    companion object {
        private lateinit var instance: BitcoinTicker
        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}