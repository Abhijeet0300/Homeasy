package io.homeasy.app.feature_home.domain.usecase

import com.thingclips.smart.home.sdk.bean.HomeBean
import io.homeasy.app.feature_home.domain.repository.HomeRepository
import javax.inject.Inject

class QueryHomeListUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke() : Result<List<HomeBean?>?> {
        return homeRepository.queryHomeList()
    }
}