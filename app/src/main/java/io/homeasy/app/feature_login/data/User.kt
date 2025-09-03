package io.homeasy.app.feature_login.data

data class User (
    val id : String,
    val username : String? = null,
    val email : String? = null,
    val phone : String? = null
)
