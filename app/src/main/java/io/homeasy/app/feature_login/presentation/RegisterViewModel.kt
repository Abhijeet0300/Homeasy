package io.homeasy.app.feature_login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thingclips.smart.android.user.bean.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.homeasy.app.feature_login.data.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository : AuthRepositoryImpl
) : ViewModel() {

    private val _isCodeSent = MutableStateFlow<Boolean?>(false)
    val isCodeSent = _isCodeSent.asStateFlow()

    private val _registrationMessage = MutableStateFlow<String?>(null)
    val registrationMessage = _registrationMessage.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow<String>("")
    val password = _password.asStateFlow()

    fun sendVerificationCode() {
        viewModelScope.launch {
            authRepository.sendVerificationCode(_email.value, "91")
                .onSuccess {
                    _isCodeSent.value = true
                    Log.i("RegisterViewModel", "Verification code sent successfully")
                }
                .onFailure {
                    _isCodeSent.value = false
                    Log.e("RegisterViewModel", "Failed to send verification code: ${it.message}")
                }
        }
    }

    fun registerWithEmail(
        email: String,
        countryCode: String = "91",
        password: String,
        verificationCode: String,
    ) {
        viewModelScope.launch {
            authRepository.registerWithEmail(email, countryCode, password, verificationCode)
                .onSuccess { newUser ->
                    _user.value = newUser
                    _registrationMessage.value = "Registration successful"
                }
                .onFailure {
                    // Handle registration failure, e.g., show error message
                    _registrationMessage.value = it.message
                }
        }
    }

    fun setEmail(email : String) {
        _email.value = email
    }

    fun setPassword(password : String) {
        _password.value = password
    }
}