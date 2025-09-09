package io.homeasy.app.feature_home.domain.usecase

import io.homeasy.app.feature_home.domain.model.HomeChangeEvent
import io.homeasy.app.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHomeChangeListenerUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() : Flow<HomeChangeEvent> {
        return homeRepository.observeHomeChanges()
    }
}