package io.homeasy.app.feature_home.domain.model

data class ActivatorParams(
    val homeId : Long,
    val ssid : String,
    val password : String,
    val timeout : Long = 120
)