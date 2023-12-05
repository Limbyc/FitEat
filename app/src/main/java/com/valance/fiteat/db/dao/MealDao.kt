package com.valance.fiteat.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.valance.fiteat.db.entity.Meal

@Dao
interface MealDao {
    @Query("SELECT * FROM meal")
    suspend fun getAllMeals(): List<Meal>

    @Query("SELECT * FROM meal WHERE id = :mealId")
    suspend fun getMealById(mealId: Int): Meal
}

