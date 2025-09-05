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

    private val _otp = MutableStateFlow<String>("")
    val otp = _otp.asStateFlow()

    private val _otpVerificationMessage = MutableStateFlow<String?>(null)
    val otpVerificationMessage = _otpVerificationMessage.asStateFlow()

    private val _isValidOtp = MutableStateFlow<Boolean?>(null)
    val isValidOtp = _isValidOtp.asStateFlow()

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

    fun verifyCode(
        email: String,
        countryCode: String = "91",
        verificationCode: String
    ) {
        viewModelScope.launch {
            authRepository.verifyCode(email, countryCode, verificationCode)
                .onSuccess { isValid ->
                    if (isValid) {
                        _isValidOtp.value = true
                        _otpVerificationMessage.value = "Verification successful"
                    } else {
                        _isValidOtp.value = false
                        _otpVerificationMessage.value = "Invalid verification code"
                    }
                }
                .onFailure {
                    _isValidOtp.value = false
                    _otpVerificationMessage.value = it.message
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