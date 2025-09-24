package io.homeasy.app.feature_room.domain.usecase

import io.homeasy.app.feature_room.domain.repository.RoomRepository
import javax.inject.Inject

class AddDeviceToRoomUseCase @Inject constructor(
    private val repo : RoomRepository
) {
    suspend operator fun invoke(roomId : Long, deviceId : String) : Result<Unit> =
        repo.addDevice(roomId = roomId, deviceId = deviceId)
}