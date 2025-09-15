package io.homeasy.app.feature_home.domain.usecase

import io.homeasy.app.feature_home.domain.repository.DevicePairingRepository
import javax.inject.Inject

class GetActivatorTokenUseCase @Inject constructor(
    private val repo : DevicePairingRepository
) {
    suspend operator fun invoke(homeId : Long) : Result<String> = repo.getActivatorToken(homeId)
}