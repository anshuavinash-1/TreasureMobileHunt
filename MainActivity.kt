/*
 * Assignment 6 - Mobile Treasure Hun
 * Name: Anshu Avinash
 * Course: CS 492
 * Date: 08/11/25
 */
package com.example.treasurehunt

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.treasurehunt.ui.theme.*
import com.example.treasurehunt.viewmodel.HuntViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {

    private val vm: HuntViewModel by viewModels()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Flow to emit live location updates
        fun getLiveLocationFlow() = callbackFlow<Pair<Double, Double>?> {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                2000 // update every 2 seconds
            ).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val loc = locationResult.lastLocation
                    if (loc != null) {
                        trySend(Pair(loc.latitude, loc.longitude))
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }

        setContent {
            val nav = rememberNavController()

            Surface(color = MaterialTheme.colorScheme.background) {
                NavHost(navController = nav, startDestination = "permissions") {

                    // Permissions screen
                    composable("permissions") {
                        PermissionsScreen {
                            nav.navigate("start") {
                                popUpTo("permissions") { inclusive = true }
                            }
                        }
                    }

                    // Start screen
                    composable("start") {
                        StartScreen(vm = vm, onStart = {
                            nav.navigate("clue")
                        })
                    }

                    // Clue screen
                    composable("clue") {
                        ClueScreen(
                            vm = vm,
                            onClueSolved = { nav.navigate("clueSolved") }
                        )

                    }

                    // Clue solved screen
                    composable("clueSolved") {
                        ClueSolvedScreen(vm = vm,navController = nav, onNext = {
                            if (vm.uiState.value.currentIndex >= vm.uiState.value.clues.size ) {
                                nav.navigate("completed")
                            } else {

                                nav.navigate("clue")
                            }
                        })
                    }

                    // Completed screen
                    composable("completed") {
                        CompletedScreen(vm = vm, onRestart = {
                            nav.navigate("start") {
                                popUpTo("start") { inclusive = true }
                            }
                        })
                    }
                }
            }
        }
    }
}
