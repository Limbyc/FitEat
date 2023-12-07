package com.valance.fiteat.di

import android.content.Context
import androidx.room.Room
import com.valance.fiteat.FitEatApp
import com.valance.fiteat.db.AppDatabase
import com.valance.fiteat.db.dao.MealDao
import com.valance.fiteat.db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    /*
            Db
    */
    @Provides
    fun provideMealDao(
        database: AppDatabase
    ): MealDao = database.mealDao()

    @Provides
    fun provideUserDao(
        database: AppDatabase
    ): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideItemDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "database.db"
        ).createFromAsset("data/database")
            .fallbackToDestructiveMigration()
            .build()
    }
}