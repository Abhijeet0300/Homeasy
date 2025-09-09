package io.homeasy.app.feature_home.domain.usecase

import com.thingclips.smart.home.sdk.bean.HomeBean
import io.homeasy.app.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class CreateHomeUseCase @Inject constructor(
    private val homeRepo : HomeRepository
){
    suspend operator fun invoke(
    name : String,
    lon : Double,
    lat : Double,
    geoName : String,
    rooms: List<String>
    ) : Result<HomeBean?> {
        return homeRepo.createHome(
            name,
            lon,
            lat,
            geoName,
            rooms
        )
    }
}