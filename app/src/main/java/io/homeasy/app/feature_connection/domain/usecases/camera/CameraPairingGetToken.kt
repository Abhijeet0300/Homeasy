package io.homeasy.app.feature_connection.domain.usecases.camera

import io.homeasy.app.feature_connection.domain.repository.CameraPairingRepository
import javax.inject.Inject

class CameraPairingGetToken @Inject constructor(
    private val repo : CameraPairingRepository
){
    suspend operator fun invoke(homeId : Long) = repo.getToken(homeId)
}