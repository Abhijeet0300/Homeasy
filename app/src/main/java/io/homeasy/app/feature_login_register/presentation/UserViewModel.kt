package io.homeasy.app.feature_login_register.presentation

import androidx.lifecycle.ViewModel
import com.thingclips.smart.sdk.api.IThingUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.thingclips.smart.android.user.bean.User

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userInstance : IThingUser
) : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun setCurrentUser(user: User?) {
        _currentUser.value = user
    }
}