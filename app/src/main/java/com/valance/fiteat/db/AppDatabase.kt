package com.valance.fiteat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.valance.fiteat.db.dao.MealDao
import com.valance.fiteat.db.dao.UserDao
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.db.entity.User

@Database(entities = [User::class, Meal::class],  version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao

}