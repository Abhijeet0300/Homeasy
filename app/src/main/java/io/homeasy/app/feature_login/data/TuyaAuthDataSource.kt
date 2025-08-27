package io.homeasy.app.feature_login.data

import com.thingclips.smart.android.user.api.ILoginCallback
import com.thingclips.smart.android.user.api.IRegisterCallback
import com.thingclips.smart.android.user.bean.User
import com.thingclips.smart.sdk.api.IResultCallback
import com.thingclips.smart.sdk.api.IThingUser
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class TuyaAuthDataSource @Inject constructor(
    private val userInstance : IThingUser
) {
    suspend fun sendVerificationCode(email : String, countryCode : String) : Result<Unit> {
        return suspendCancellableCoroutine { continuation ->
            userInstance.sendVerifyCodeWithUserName(email, "", "91", 1, object : IResultCallback{
                override fun onError(code: String?, error: String?) {
                    continuation.resume(Result.failure(Exception("Error code: $code, error: $error")), null)
                }

                override fun onSuccess() {
                    continuation.resume(Result.success(Unit), null)
                }
            })
        }
    }

    suspend fun registerWithEmail(
        email: String,
        countryCode: String,
        password: String,
        verificationCode: String
    ): Result<User> {
        return suspendCancellableCoroutine { continuation ->
            userInstance.registerAccountWithEmail(
                countryCode,
                email,
                password,
                verificationCode,
                object : IRegisterCallback {
                    override fun onSuccess(user: User) {
                        continuation.resume(Result.success(user), null)
                    }

                    override fun onError(code: String, error: String) {
                        continuation.resume(Result.failure(Exception("[$code] $error")), null)
                    }
                }
            )
        }
    }

    suspend fun loginWithEmail(
        countryCode : String = "91",
        email : String,
        password: String
    ) : Result<User?> {
        return suspendCancellableCoroutine { continuation ->
            userInstance.loginWithEmail(countryCode, email, password, object : ILoginCallback {
                override fun onSuccess(user: User?) {
                    continuation.resume(Result.success(user), null)
                }

                override fun onError(code: String?, error: String?) {
                    continuation.resume(Result.failure(Exception("Error code: $code, error: $error")), null)
                }

            })

        }
    }
}