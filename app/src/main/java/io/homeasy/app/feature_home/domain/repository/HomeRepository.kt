package io.homeasy.app.feature_home.domain.repository

import com.thingclips.smart.home.sdk.bean.HomeBean
import io.homeasy.app.feature_home.domain.model.HomeChangeEvent
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun createHome(
        name : String,
        lon : Double = 0.0,
        lat : Double = 0.0,
        geoName : String = "India",
        rooms : List<String>,
    ) : Result<HomeBean?>

    suspend fun queryHomeList() : Result<List<HomeBean?>?>

    suspend fun observeHomeChanges() : Flow<HomeChangeEvent>
}