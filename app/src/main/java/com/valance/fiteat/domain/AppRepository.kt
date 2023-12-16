package com.valance.fiteat.domain

import com.valance.fiteat.db.entity.Meal


interface AppRepository {
    //Получение отдельно списка еды
    suspend fun getAllMeals() : List<Meal>

    //Получение отдельно свойств продуктов
    suspend fun getMealById(mealId: Int): Meal

}