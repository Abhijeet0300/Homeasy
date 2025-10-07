package io.homeasy.app.feature_room.presentation

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.home.sdk.bean.RoomBean
import com.thingclips.smart.sdk.bean.DeviceBean
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_room.domain.usecase.AddDeviceToRoomUseCase
import io.homeasy.app.feature_room.domain.usecase.GetRoomDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val addRoomUseCase : AddDeviceToRoomUseCase,
    private val getRoomDetailsUseCase : GetRoomDetailsUseCase
) : ViewModel(){
    private val _selectedRoom = MutableStateFlow<RoomBean?>(null)
    val selectedRoom = _selectedRoom.asStateFlow()

    private val _isDeviceAdded = MutableStateFlow<Boolean?>(null)
    val isDeviceAdded = _isDeviceAdded.asStateFlow()

    private val _addedDeviceBean = MutableStateFlow<DeviceBean?>(null)
    val addedDeviceBean = _addedDeviceBean.asStateFlow()

    private val _selectedDevice = MutableStateFlow<DeviceBean?>(null)
    val selectedDevice = _selectedDevice.asStateFlow()

    fun addDevice(
        roomId : Long,
        deviceId : String
    ) {
        viewModelScope.launch {
            addRoomUseCase(roomId = roomId, deviceId = deviceId)
                .onSuccess {
                    _isDeviceAdded.value = true
                    Log.i("RoomViewModel", "Device added successfully")
                }
                .onFailure {
                    _isDeviceAdded.value = false
                    Log.e("RoomViewModel", "Failed to add device: ${it.message}")
                }
        }
    }

    fun getRoomDetails(homeId : Long, roomId : Long) {
        viewModelScope.launch {
            getRoomDetailsUseCase(homeId = homeId, roomId = roomId)
                .onSuccess {
                    Log.e("RoomViewModel", "Successfully got room details")
                    _selectedRoom.value = it
                }
                .onFailure {
                    Log.e("RoomViewModel", "Failed to get room details: ${it.message}")
                }
        }
    }

    fun setSelectedRoom(roomBean : RoomBean) {
        _selectedRoom.value = roomBean
    }

    fun setAddedDevice(device : DeviceBean) {
        //_addedDeviceBean.value = device
        if(_selectedRoom.value != null) {
            Log.i("RoomViewModel", "Adding device to room: ${_selectedRoom.value!!.roomId}")
            addDevice(roomId = _selectedRoom.value!!.roomId, deviceId = device.devId)
        }
    }

    fun setSelectedDevice(deviceBean : DeviceBean) {
        _selectedDevice.value = deviceBean
    }
}