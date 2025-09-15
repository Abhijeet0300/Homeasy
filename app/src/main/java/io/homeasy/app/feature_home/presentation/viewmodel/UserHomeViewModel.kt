package io.homeasy.app.feature_home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.bean.RoomBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserHomeViewModel @Inject constructor(): ViewModel() {
    private val _roomList = MutableStateFlow<List<RoomBean>>(emptyList())
    val roomList = _roomList.asStateFlow()

    fun getRoomList(selectedHome : HomeBean) {
        _roomList.value = selectedHome.rooms
    }
}