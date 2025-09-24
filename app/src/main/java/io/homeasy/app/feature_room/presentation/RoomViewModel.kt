package io.homeasy.app.feature_room.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.home.sdk.bean.RoomBean
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_room.domain.usecase.AddDeviceToRoomUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val addRoomUseCase : AddDeviceToRoomUseCase
) : ViewModel(){
    private val _selectedRoom = MutableStateFlow<RoomBean?>(null)
    val selectedRoom = _selectedRoom.asStateFlow()

    private val _isDeviceAdded = MutableStateFlow<Boolean?>(null)
    val isDeviceAdded = _isDeviceAdded.asStateFlow()

    fun addDevice(
        roomId : Long,
        deviceId : String
    ) {
        viewModelScope.launch {
            addRoomUseCase(roomId = roomId, deviceId = deviceId)
                .onSuccess {
                    _isDeviceAdded.value = true
                }
                .onFailure {
                    _isDeviceAdded.value = false
                }
        }
    }

    fun selectRoom(roomBean : RoomBean) {
        _selectedRoom.value = roomBean
    }
}