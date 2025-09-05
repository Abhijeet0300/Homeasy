package io.homeasy.app.feature_login_register.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.android.user.bean.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_login_register.data.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _loginMessage = MutableStateFlow<String?>(null)
    val loginMessage = _loginMessage.asStateFlow()

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow<String>("")
    val password = _password.asStateFlow()

    private val _isLoginSuccessful = MutableStateFlow<Boolean>(false)
    val isSLoginSuccessful = _isLoginSuccessful.asStateFlow()

    fun loginWithEmail(
        countryCode: String = "91",
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            authRepository.loginWithEmail(countryCode, email, password)
                .onSuccess { currentUser->
                    _user.value = currentUser
                    _isLoginSuccessful.value = true
                    _loginMessage.value = "${currentUser!!.email} logged in successfully"
                }
                .onFailure {
                    _loginMessage.value = "Login failed."
                    Log.e("LoginViewModel", "Login error: ${it.message}")
                }
        }
    }
}