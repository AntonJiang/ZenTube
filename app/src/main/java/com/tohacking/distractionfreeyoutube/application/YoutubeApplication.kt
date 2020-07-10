package com.tohacking.distractionfreeyoutube.application

import android.app.Application
import timber.log.Timber

class YoutubeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}