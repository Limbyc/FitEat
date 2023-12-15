package com.valance.fiteat.db.repository

import com.valance.fiteat.db.dao.MealDao
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.domain.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val mealDao: MealDao,
) : AppRepository {
    override suspend fun getAllMeals(): List<Meal> {
        return mealDao.getAllMeals()
    }

    override suspend fun getMealById(mealId: Int): Meal{
        return mealDao.getMealById(mealId)
    }
}

