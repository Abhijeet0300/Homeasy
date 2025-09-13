package io.homeasy.app.feature_home.domain.usecase

import com.thingclips.smart.home.sdk.bean.RoomBean
import io.homeasy.app.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class AddRoomUseCase @Inject constructor(
    private val homeRepository : HomeRepository
) {
    suspend operator fun invoke(homeId : Long, name : String) : Result<RoomBean?> {
        return homeRepository.addRoom(homeId, name)
    }
}