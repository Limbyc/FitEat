package com.valance.fiteat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valance.fiteat.db.UserMetrics
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.db.entity.User
import com.valance.fiteat.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val app: AppRepository) : ViewModel() {

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()
    suspend fun getWeight(userId: Int): UserMetrics? {
        return app.getWeight(userId)
    }

    suspend fun getHeight(userId: Int): UserMetrics? {
        return app.getHeight(userId)
    }

    suspend fun getMealById(mealId: Int): Meal {
        return app.getMealById(mealId)
    }
    suspend fun setWeight(userId: Int, newWeight: String) {
        return app.setWeight(userId, newWeight)
    }

    fun getUserById(id: Int) {
        viewModelScope.launch {
            _user.value = app.getUserById(id)
        }
    }
}
