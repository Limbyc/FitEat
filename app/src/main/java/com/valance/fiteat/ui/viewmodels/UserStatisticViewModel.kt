package com.valance.fiteat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valance.fiteat.domain.AppRepository
import com.valance.fiteat.db.entity.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserStatisticViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {

    fun getAllMeals(): List<Meal> {
        viewModelScope.launch {
            val meals = appRepository.getAllMeals()
        }
        return emptyList()
    }
}
