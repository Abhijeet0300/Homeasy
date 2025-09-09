package io.homeasy.app.feature_home.domain.model

import com.thingclips.smart.sdk.bean.DeviceBean
import com.thingclips.smart.sdk.bean.GroupBean

sealed class HomeChangeEvent {
    data class HomeInvite(val homeId: Long, val homeName: String) : HomeChangeEvent()
    data class HomeRemoved(val homeId: Long) : HomeChangeEvent()
    data class HomeInfoChanged(val homeId: Long) : HomeChangeEvent()
    data class SharedDeviceList(val devices: List<DeviceBean?>?) : HomeChangeEvent()
    data class SharedGroupList(val groups: List<GroupBean?>?) : HomeChangeEvent()
    object ServerConnectSuccess : HomeChangeEvent()
    data class HomeAdded(val homeId: Long) : HomeChangeEvent()
}