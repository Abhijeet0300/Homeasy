package io.homeasy.app.feature_connection.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.android.ble.api.ScanDeviceBean
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.ConfigProductInfoBean
import com.thingclips.smart.sdk.api.IThingDataCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_connection.domain.usecases.ScanDevicesBleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanDevicesBleViewModel @Inject constructor(
    private val scanDevicesBleUseCase: ScanDevicesBleUseCase
) : ViewModel() {
    private val _devices = MutableStateFlow<List<ScanDeviceBean>>(emptyList())
    val devices = _devices.asStateFlow()

    private val _deviceName = MutableStateFlow<String>("")
    val deviceName = _deviceName.asStateFlow()

    fun startScan() {
        viewModelScope.launch {
            scanDevicesBleUseCase().collect { device ->
                _devices.value = _devices.value + device
            }
        }
    }

    fun stopScan() {
        _devices.value = emptyList<ScanDeviceBean>()
    }

    fun setDeviceName(bean : ScanDeviceBean) {
        ThingHomeSdk.getActivatorInstance().getActivatorDeviceInfo(
            bean.productId,
            bean.uuid,
            bean.mac,
            object : IThingDataCallback<ConfigProductInfoBean> {
                override fun onSuccess(result: ConfigProductInfoBean?) {
                    _deviceName.value = result?.name ?: ""
                }
                override fun onError(errorCode: String?, errorMessage: String?) {
                    Log.e("ScanDevicesBleViewModel", "Error getting device name: $errorMessage")
                }
            }
        )
    }

    fun getDeviceName() : String {
        return _deviceName.value
    }
}