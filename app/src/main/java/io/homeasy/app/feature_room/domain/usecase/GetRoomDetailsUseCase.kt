package io.homeasy.app.feature_room.domain.usecase

import io.homeasy.app.feature_room.domain.repository.RoomRepository
import javax.inject.Inject

class GetRoomDetailsUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(homeId: Long, roomId: Long) = roomRepository.getRoomDetails(homeId, roomId)
}