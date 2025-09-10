/*
 * Assignment 6 - Mobile Treasure Hun
 * Name: Anshu Avinash
 * Course: CS 492
 * Date: 08/11/25
 */

package com.example.treasurehunt.ui.theme

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.treasurehunt.viewmodel.HuntViewModel
import kotlinx.coroutines.delay
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import com.example.treasurehunt.R
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import kotlin.math.*

@SuppressLint("MissingPermission")
@Composable
fun ClueScreen(
    vm: HuntViewModel,
    onClueSolved: () -> Unit
) {
    val ui by vm.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showHint by remember { mutableStateOf(false) }
    var elapsed by remember { mutableStateOf(ui.elapsedMillis) }

    var currentLat by remember { mutableStateOf<Double?>(null) }
    var currentLon by remember { mutableStateOf<Double?>(null) }
    var distance by remember { mutableStateOf<Double?>(null) }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val locationRequest = remember {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(1000)
            .build()
    }

    val currentClue = ui.clues.getOrNull(ui.currentIndex) ?: return

    // Ask for permission if not granted
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback(
                    onLocationUpdate = { lat, lon ->
                        currentLat = lat
                        currentLon = lon
                        distance = haversine(lat, lon, currentClue.lat, currentClue.lon)
                    }
                ),
                Looper.getMainLooper()
            )
        }
    }

    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback(
                    onLocationUpdate = { lat, lon ->
                        currentLat = lat
                        currentLon = lon
                        distance = haversine(lat, lon, currentClue.lat, currentClue.lon)
                    }
                ),
                Looper.getMainLooper()
            )
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Timer loop
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            elapsed += 1000
            vm.setElapsed(elapsed)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SnackbarHost(hostState = snackbarHostState)

        Image(
            painter = painterResource(id = R.drawable.treasure),
            contentDescription = "Treasure Map",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Column {
            Text("Clue ${ui.currentIndex + 1} / ${ui.clues.size}", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(currentClue.text, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            if (showHint) {
                Text("Hint: ${currentClue.hint}")
            }
            Spacer(Modifier.height(12.dp))
            //Text(" Target: ${currentClue.lat}, ${currentClue.lon}")
//            currentLat?.let { lat ->
//                currentLon?.let { lon ->
//                    Text(" Current: $lat, $lon")
//                }
//            }
//            distance?.let {
//                val isClose = it <= 50
//                Text(
//                    " Distance: ${"%.1f".format(it)} meters",
//                    color = if (isClose) Color(0xFF388E3C) else Color.Red,
//                    fontWeight = FontWeight.Bold
//                )
//            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color(0xFF1976D2), shape = RoundedCornerShape(8.dp))
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⏱  ${elapsed / 1000} seconds",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Column {
            Button(onClick = { showHint = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Show Hint")
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    if (distance != null && distance!! <= 50) {
                        onClueSolved()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("You are too far from the place!")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Found It!")
            }
        }
    }
}

private fun locationCallback(onLocationUpdate: (Double, Double) -> Unit) =
    object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            for (location in result.locations) {
                onLocationUpdate(location.latitude, location.longitude)
            }
        }
    }

// Haversine formula — distance in meters
fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371000.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2.0) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c
}
