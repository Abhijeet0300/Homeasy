package io.homeasy.app.feature_home.domain.usecase

import io.homeasy.app.feature_home.domain.model.ActivatorParams
import io.homeasy.app.feature_home.domain.model.ActivatorResult
import io.homeasy.app.feature_home.domain.repository.DevicePairingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartEZPairingUseCase @Inject constructor(
    private val repo : DevicePairingRepository,
) {
    operator fun invoke(params : ActivatorParams) : Flow<ActivatorResult> = repo.startEZPairing(params)
}