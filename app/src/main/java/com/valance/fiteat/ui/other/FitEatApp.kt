package com.valance.fiteat.ui.other

import android.app.Application
import com.valance.fiteat.db.MainDB

class FitEatApp : Application()  {
    lateinit var database: MainDB

    override fun onCreate() {
        super.onCreate()
        database = MainDB.getDB(applicationContext)
        }
    }