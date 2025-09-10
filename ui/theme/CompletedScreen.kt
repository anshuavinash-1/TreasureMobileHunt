/*
 * Assignment 6 - Mobile Treasure Hun
 * Name: Anshu Avinash
 * Course: CS 492
 * Date: 08/11/25
 */

package com.example.treasurehunt.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.treasurehunt.viewmodel.HuntViewModel

@Composable
fun CompletedScreen(vm: HuntViewModel, onRestart: () -> Unit) {
    val ui by vm.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hunt Completed!", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        Text("Time Taken: ${ui.elapsedMillis / 1000} seconds")
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRestart) {
            Text("Play Again")
        }
    }
}