/*
 * Assignment 6 - Mobile Treasure Hun
 * Name: Anshu Avinash
 * Course: CS 492
 * Date: 08/11/25
 */
package com.example.treasurehunt.ui.theme

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PermissionsScreen(onAllGranted: () -> Unit) {
    val required = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // If you want background permission, you can request ACCESS_BACKGROUND_LOCATION separately later.
    }

    var statusText by remember { mutableStateOf("Permissions not requested") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val ok = required.all { perms[it] == true }
        statusText = if (ok) "Permissions granted" else "Permissions denied"
        if (ok) onAllGranted()
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Mobile Treasure Hunt")
        Spacer(Modifier.height(8.dp))
        Text("This app needs location permission to verify clues.")
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            launcher.launch(required.toTypedArray())
        }) {
            Text("Request Permissions")
        }
        Spacer(Modifier.height(12.dp))
        Text(statusText)
    }
}