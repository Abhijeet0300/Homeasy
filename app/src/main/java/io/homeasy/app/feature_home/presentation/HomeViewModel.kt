package io.homeasy.app.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.home.sdk.bean.HomeBean
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_home.data.HomeRepositoryImpl
import io.homeasy.app.feature_home.domain.model.HomeChangeEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepositoryImpl: HomeRepositoryImpl
) : ViewModel() {
    private val _homeBean = MutableStateFlow<HomeBean?>(null)
    val homeBean = _homeBean.asStateFlow()

    var _homeCreationMessage = MutableStateFlow<String>("")
    val homeCreationMessage = _homeCreationMessage.asStateFlow()

    val _isSuccessfullyCreated = MutableStateFlow<Boolean>(false)
    val isSuccessfullyCreated = _isSuccessfullyCreated.asStateFlow()

    private val _homeList = MutableStateFlow<List<HomeBean?>?>(null)
    val homeList = _homeList.asStateFlow()

    private val _homeEvents = MutableSharedFlow<HomeChangeEvent>()
    val homeEvents = _homeEvents.asSharedFlow()

    private val _selectedHome = MutableStateFlow<HomeBean?>(null)
    val selectedHome = _selectedHome.asStateFlow()

    init {
        viewModelScope.launch {
            homeRepositoryImpl.observeHomeChanges().collect { event ->
                _homeEvents.emit(event)
            }
        }
    }

    fun createHome(
        name : String,
        lon : Double = 0.0,
        lat : Double = 0.0,
        geoName : String,
        rooms: List<String>
    ) {
        viewModelScope.launch {
            homeRepositoryImpl.createHome(name, lon, lat, geoName, rooms)
                .onSuccess { newHome ->
                    _homeBean.value = newHome
                    _isSuccessfullyCreated.value = true
                    _homeCreationMessage.value = "${newHome?.name} is successfully created."
                }
                .onFailure {
                    _homeCreationMessage.value = "Failed to create home."
                }
        }
    }

    fun queryHomeList() {
        viewModelScope.launch {
            homeRepositoryImpl.queryHomeList()
                .onSuccess { homeList ->
                    _homeList.value = homeList
                }
                .onFailure {
                    _homeList.value = emptyList()
                }
        }
    }

    fun setSelectedHome(homeBean : HomeBean) {
        viewModelScope.launch {
            _selectedHome.value = homeBean
        }
    }
}