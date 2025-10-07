package io.homeasy.app.feature_connection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WifiInfoViewModel @Inject constructor() : ViewModel() {
    private val _ssid = MutableStateFlow<String?>(null)
    val ssid= _ssid.asStateFlow()

    private val _password = MutableStateFlow<String?>(null)
    val password = _password.asStateFlow()

    fun setWifiCredentials(ssid: String, password: String) {
        _ssid.value = ssid
        _password.value = password
    }
}