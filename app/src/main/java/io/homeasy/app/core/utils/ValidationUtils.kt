package io.homeasy.app.core.utils

private const val EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"

fun String.isValidEmail() : Boolean {
    val emailRegex = EMAIL_REGEX.toRegex()
    return this.isNotBlank() && this.matches(emailRegex)
}

fun validateEmailAndPassword(email :String, password : String) : Boolean {
    if (email.isBlank() || password.isBlank()|| email.isEmpty() || password.isEmpty() || !email.isValidEmail()) {
        return false
    }
    return true
}