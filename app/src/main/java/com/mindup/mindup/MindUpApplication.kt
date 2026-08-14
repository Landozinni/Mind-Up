package com.mindup.mindup

import android.app.Application

class MindUpApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = AppContainer(this)
    }

}