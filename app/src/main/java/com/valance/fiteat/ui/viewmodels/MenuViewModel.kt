package com.valance.fiteat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valance.fiteat.db.sharedPreferences.UserMetrics
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val app: AppRepository) : ViewModel() {
    suspend fun getMealById(mealId: Int): Meal {
        return app.getMealById(mealId)
    }
}
