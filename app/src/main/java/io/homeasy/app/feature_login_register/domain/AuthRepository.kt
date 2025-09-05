package io.homeasy.app.feature_login_register.domain

import com.thingclips.smart.android.user.bean.User

interface AuthRepository {
    suspend fun sendVerificationCode(email: String, countryCode: String): Result<Unit>

    suspend fun registerWithEmail(
        email: String,
        countryCode: String,
        password: String,
        verificationCode: String
    ): Result<User>

    suspend fun loginWithEmail(
        countryCode : String = "91",
        email : String,
        password: String
    ) : Result<User?>

    suspend fun verifyCode(
        email: String,
        countryCode: String,
        verificationCode: String
    ): Result<Boolean>
}