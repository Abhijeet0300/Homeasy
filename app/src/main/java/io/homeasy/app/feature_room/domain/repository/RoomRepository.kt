package io.homeasy.app.feature_room.domain.repository

import com.thingclips.smart.home.sdk.bean.RoomBean

interface RoomRepository {
    suspend fun addRoom(homeId : Long, name : String) : Result<RoomBean?>
    suspend fun addDevice(roomId : Long, deviceId : String) : Result<Unit>
    suspend fun getRoomDetails(homeId : Long, roomId : Long) : Result<RoomBean>
}