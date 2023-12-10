package com.valance.fiteat.db.repository

import com.valance.fiteat.db.UserMetrics
import com.valance.fiteat.db.dao.MealDao
import com.valance.fiteat.db.dao.UserDao
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.db.entity.User
import com.valance.fiteat.domain.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val mealDao: MealDao,
    private val userDao: UserDao,
) : AppRepository {
    override suspend fun getAllMeals(): List<Meal> {
        return mealDao.getAllMeals()
    }

    override suspend fun getHeight(userId: Int): UserMetrics? {
        return userDao.getUserMetrics(userId)
    }

    override suspend fun getWeight(userId: Int): UserMetrics? {
        return userDao.getUserMetrics(userId)
    }
    override suspend fun getMealById(mealId: Int): Meal{
        return mealDao.getMealById(mealId)
    }

    override suspend fun insertUser(user: User) {
        return userDao.insertUser(user)
    }
}

