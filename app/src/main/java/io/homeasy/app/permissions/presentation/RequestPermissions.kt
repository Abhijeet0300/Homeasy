package io.homeasy.app.permissions.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import io.homeasy.app.permissions.domain.Permissions
import io.homeasy.app.permissions.domain.openAppSettings

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestAllPermissions(
    onAllGranted : @Composable () -> Unit,
    onDenied : @Composable () -> Unit,
    onShowRational : @Composable () -> Unit
) {
    val context = LocalContext.current
    val permissionsState = rememberMultiplePermissionsState(
        Permissions.requiredPermissions.toList()
    )

    when {
        permissionsState.allPermissionsGranted -> {
            onAllGranted()
        }
        permissionsState.shouldShowRationale -> {
            onShowRational()
            LaunchedEffect(Unit) {
                permissionsState.launchMultiplePermissionRequest()
            }
        }
        permissionsState.permissions.any { !it.status.isGranted && !it.status.shouldShowRationale } -> {
            // Permissions permanently denied
            Column {
                Text("Permissions are permanently denied. Please enable them from settings.")
                Button(
                    onClick = { openAppSettings(context) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Open Settings")
                }
            }
        }

        else -> {
            LaunchedEffect(Unit) {
                permissionsState.launchMultiplePermissionRequest()
            }
            onDenied()
        }
    }
}