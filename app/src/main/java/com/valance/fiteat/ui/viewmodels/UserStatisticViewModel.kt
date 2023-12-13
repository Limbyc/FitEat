package com.valance.fiteat.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valance.fiteat.domain.AppRepository
import com.valance.fiteat.db.entity.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserStatisticViewModel @Inject constructor(private val app: AppRepository) : ViewModel() {

    private val _mealsLiveData: MutableLiveData<List<Meal>> = MutableLiveData()
    val mealsLiveData: LiveData<List<Meal>> = _mealsLiveData

    fun loadAllMeals() {
        viewModelScope.launch {
            val meals = app.getAllMeals()
            _mealsLiveData.postValue(meals)
            Log.d("UserStatisticViewModel", "Number of meals loaded: ${meals.size}")
        }
    }


}

