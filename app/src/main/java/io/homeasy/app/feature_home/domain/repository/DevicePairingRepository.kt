package io.homeasy.app.feature_home.domain.repository

import io.homeasy.app.feature_home.domain.model.ActivatorParams
import io.homeasy.app.feature_home.domain.model.ActivatorResult
import kotlinx.coroutines.flow.Flow

interface DevicePairingRepository {
    suspend fun getActivatorToken(homeId : Long) : Result<String>
    fun startEZPairing(params: ActivatorParams) : Flow<ActivatorResult>
}