package com.valance.fiteat

import dagger.hilt.android.HiltAndroidApp
import android.app.Application

@HiltAndroidApp
class FitEatApp : Application() {
        companion object {
                lateinit var instance: FitEatApp
                        private set
        }

        override fun onCreate() {
                super.onCreate()
                instance = this
        }
}
