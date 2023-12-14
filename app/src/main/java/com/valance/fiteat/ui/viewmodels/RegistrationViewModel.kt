package com.valance.fiteat.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.valance.fiteat.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val app: AppRepository) : ViewModel() {

}

