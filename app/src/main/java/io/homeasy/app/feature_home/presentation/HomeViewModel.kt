package io.homeasy.app.feature_home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.bean.RoomBean
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_home.data.HomeRepositoryImpl
import io.homeasy.app.feature_home.domain.model.HomeChangeEvent
import io.homeasy.app.feature_home.domain.usecase.AddRoomUseCase
import io.homeasy.app.feature_home.domain.usecase.GetRoomListUseCase
import io.homeasy.app.feature_home.domain.usecase.ObserveHomeChangeListenerUseCase
import io.homeasy.app.feature_home.domain.usecase.QueryHomeListUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepositoryImpl: HomeRepositoryImpl,
    private val addRoomUseCase: AddRoomUseCase,
    private val observeHomeChangeListenerUseCase: ObserveHomeChangeListenerUseCase,
    private val queryHomeListUseCase: QueryHomeListUseCase,
    private val getRoomListUseCase: GetRoomListUseCase
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

    private val _roomAddedMessage = MutableStateFlow<String>("")
    val roomAddedMessage = _roomAddedMessage.asStateFlow()

    private val _isRoomAdded = MutableStateFlow<Boolean?>(null)
    val isRoomAdded = _isRoomAdded.asStateFlow()

    private val _roomList = MutableStateFlow<List<RoomBean?>?>(emptyList())
    val roomList = _roomList.asStateFlow()

    private val _isRoomListFetched = MutableStateFlow<Boolean?>(null)
    val isRoomListFetched = _isRoomListFetched.asStateFlow()

    init {
        observeHomeChanges()
    }

    private fun observeHomeChanges() {
        viewModelScope.launch {
            observeHomeChangeListenerUseCase().collect { event->
                when(event) {
                    is HomeChangeEvent.HomeInfoChanged -> getHomeDetails(event.homeId)
                    else -> Unit
                }
            }
        }
    }

    //Create Home
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

    //Query Room List
    fun queryHomeList() {
        viewModelScope.launch {
            queryHomeListUseCase()
                .onSuccess { homeList ->
                    _homeList.value = homeList
                }
                .onFailure {
                    _homeList.value = emptyList()
                }
        }
    }

    //Add Room
    fun addRoom(homeId : Long, name: String) {
        viewModelScope.launch {
            addRoomUseCase(homeId, name)
                .onSuccess { roomBean->
                    _roomAddedMessage.value = "${roomBean?.name} is successfully added."
                    _isRoomAdded.value = true
                }
                .onFailure {
                    _roomAddedMessage.value = "Failed to add room."
                }
        }
    }

    //Set selected home
    fun setSelectedHome(homeBean : HomeBean?) {
        _selectedHome.value = homeBean
    }

    fun setIsRoomAddedToNull() {
        _isRoomAdded.value = null
        _roomAddedMessage.value = ""
    }

    fun getHomeDetails(homeId: Long)  {
        ThingHomeSdk.newHomeInstance(homeId).getHomeDetail(object : IThingHomeResultCallback {
            override fun onSuccess(bean: HomeBean?) {
                _selectedHome.value = bean
            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                Log.e("HomeViewModel", "Error code: $errorCode, error: $errorMsg")
            }
        })
    }

    fun updateRoomListOfSelectedHome(){
        _roomList.value = _selectedHome.value?.rooms
    }

    fun roomListOfSelectedHome(homeId : Long) {
        viewModelScope.launch {
            getRoomListUseCase(homeId)
                .onSuccess{ rooms->
                    _roomList.value = rooms
                    _isRoomListFetched.value = true
                }
                .onFailure {
                    _roomList.value = emptyList()
                    _isRoomListFetched.value = false
                }
        }
    }
}