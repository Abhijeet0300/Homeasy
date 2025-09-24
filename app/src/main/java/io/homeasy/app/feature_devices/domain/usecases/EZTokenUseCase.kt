package io.homeasy.app.feature_devices.domain.usecases

import io.homeasy.app.feature_devices.domain.repository.EZConnectRepository
import javax.inject.Inject

class EZTokenUseCase @Inject constructor(
    private val repo : EZConnectRepository
) {
    suspend operator fun invoke(homeId : Long) : Result<String> = repo.getToken(homeId)
}