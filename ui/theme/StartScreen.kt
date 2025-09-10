/*
 * Assignment 6 - Mobile Treasure Hun
 * Name: Anshu Avinash
 * Course: CS 492
 * Date: 08/11/25
 */
package com.example.treasurehunt.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.treasurehunt.R
import com.example.treasurehunt.viewmodel.HuntViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun StartScreen(vm: HuntViewModel, onStart: () -> Unit) {
    val ui by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Image(
            painter = painterResource(id = R.drawable.treasure),
            contentDescription = "Treasure Map",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        // Hunt name
        Text(
            text = ui.huntName.ifEmpty { "Mobile Treasure Hunt" },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Rules
        Box(
            modifier = Modifier
                .height(120.dp)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                "Rules: Reach each clue location and press 'Found It!'. Timer runs during hunt."
            )
        }

        // Start button
        Button(
            onClick = {
                vm.startHunt()
                onStart()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start")
        }
    }
}