package com.valance.fiteat.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel@Inject constructor(private val appRepository: AppRepository) : ViewModel() {

    private val _id = MutableStateFlow(-1)
    val id = _id.asStateFlow()

    fun setId(it: Int) {
        _id.value = it
    }
}