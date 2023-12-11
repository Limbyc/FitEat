package com.valance.fiteat.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.valance.fiteat.db.UserMetrics
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.db.entity.User
import com.valance.fiteat.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {


    suspend fun getWeight(userId: Int): UserMetrics? {
        return appRepository.getWeight(userId)
    }

    suspend fun getHeight(userId: Int): UserMetrics? {
        return appRepository.getHeight(userId)
    }

    suspend fun getMealById(mealId: Int): Meal {
        return appRepository.getMealById(mealId)
    }
}
