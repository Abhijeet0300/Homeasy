package io.homeasy.app.permissions.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor() : ViewModel() {
    private val _hasAllPermissions = MutableStateFlow<Boolean>(false)
    val hasAllPermissions = _hasAllPermissions.asStateFlow()

    fun updatePermissionsState(granted : Boolean) {
        _hasAllPermissions.value = granted
    }
}