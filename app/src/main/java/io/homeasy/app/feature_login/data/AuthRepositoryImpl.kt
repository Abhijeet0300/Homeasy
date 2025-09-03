package io.homeasy.app.feature_login.data

import com.thingclips.smart.android.user.bean.User
import io.homeasy.app.feature_login.domain.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: TuyaAuthDataSource
) : AuthRepository {
    override suspend fun sendVerificationCode(
        email: String,
        countryCode: String
    ): Result<Unit> {
        return remoteDataSource.sendVerificationCode(email,countryCode)
    }

    override suspend fun registerWithEmail(
        email: String,
        countryCode: String,
        password: String,
        verificationCode: String
    ): Result<User> {
        return remoteDataSource.registerWithEmail(email, countryCode, password, verificationCode)
    }

    override suspend fun loginWithEmail(
        countryCode: String,
        email: String,
        password: String
    ): Result<User?> {
        return remoteDataSource.loginWithEmail(countryCode, email, password)
    }

    override suspend fun verifyCode(
        email: String,
        countryCode: String,
        verificationCode: String
    ): Result<Boolean> {
        return remoteDataSource.verifyCode(email, countryCode, verificationCode)
    }


}