package io.homeasy.app.feature_room.domain.usecase

import com.thingclips.smart.home.sdk.bean.RoomBean
import io.homeasy.app.feature_room.domain.repository.RoomRepository
import javax.inject.Inject

class RoomRepositoryUseCase @Inject constructor(
    private val roomRepository : RoomRepository
) {

}