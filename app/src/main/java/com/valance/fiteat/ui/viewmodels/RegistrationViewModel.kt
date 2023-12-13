package com.valance.fiteat.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.valance.fiteat.domain.AppRepository
import com.valance.fiteat.db.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val app: AppRepository) : ViewModel() {

    suspend fun addUser(user: User) {
        app.insertUser(user)
    }
}

