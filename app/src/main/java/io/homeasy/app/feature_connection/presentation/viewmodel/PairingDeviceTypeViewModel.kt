package io.homeasy.app.feature_connection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_connection.domain.model.DeviceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PairingDeviceTypeViewModel @Inject constructor() : ViewModel() {
    private val _selectedDeviceType = MutableStateFlow<DeviceType?>(null)
    val selectedDeviceType = _selectedDeviceType.asStateFlow()

    fun setSelectedDeviceType(deviceType : DeviceType) {
        _selectedDeviceType.value = deviceType
    }
}