package com.valance.fiteat.di

import androidx.room.Room
import com.valance.fiteat.FitEatApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainDB {

    /*
            Db
    */
//    @Provides
//    @Singleton
//    fun provideItemDatabase(app: FitEatApp): MainDB {
//        return Room.databaseBuilder(
//            app, MainDB::class.java, "FitEat"
//        ).createFromAsset("data/FitEat")
//            .fallbackToDestructiveMigration()
//            .build()
//    }
}