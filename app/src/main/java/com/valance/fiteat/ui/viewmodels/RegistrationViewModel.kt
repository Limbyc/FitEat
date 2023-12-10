package com.valance.fiteat.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.valance.fiteat.domain.AppRepository
import com.valance.fiteat.db.entity.User
import com.valance.fiteat.db.UserMetrics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {

    suspend fun addUser(user: User) {
        appRepository.insertUser(user)
    }
}

