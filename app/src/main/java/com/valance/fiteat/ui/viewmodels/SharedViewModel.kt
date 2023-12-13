package com.valance.fiteat.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {

    private val _mealId = MutableStateFlow(-1)
    val mealId = _mealId.asStateFlow()

    private val _userId = MutableStateFlow(-1)
    val userId = _userId.asStateFlow()

    fun setMealId(it: Int) {
        _mealId.value = it
    }

    fun setUserId(it: Int) {
        _userId.value = it
    }
}