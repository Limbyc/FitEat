package com.valance.fiteat.domain

import com.valance.fiteat.db.UserMetrics
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.db.entity.User
import dagger.Provides


interface AppRepository {
    //Получение отдельно списка еды
    suspend fun getAllMeals() : List<Meal>

    //Получение отдельно веса пользователя
    suspend fun getWeight(userId: Int): UserMetrics?
    //Получение отдельно роста пользователя
    suspend fun getHeight(userId: Int): UserMetrics?
    //Получение отдельно свойств продуктов
    suspend fun getMealById(mealId: Int): Meal
    suspend fun insertUser(user: User)
}