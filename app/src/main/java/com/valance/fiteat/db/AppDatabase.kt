package com.valance.fiteat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.valance.fiteat.db.dao.MealDao
import com.valance.fiteat.db.entity.Meal

@Database(entities = [Meal::class],  version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun mealDao(): MealDao

}