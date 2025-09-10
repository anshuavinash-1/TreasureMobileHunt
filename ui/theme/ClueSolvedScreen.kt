/*
 * Assignment 6 - Mobile Treasure Hun
 * Name: Anshu Avinash
 * Course: CS 492
 * Date: 08/11/25
 */
package com.example.treasurehunt.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.treasurehunt.viewmodel.HuntViewModel


@Composable
fun ClueSolvedScreen(
    vm: HuntViewModel,
    navController: NavController,
    onNext: () -> Unit
) {
    val ui by vm.uiState.collectAsState()
    val currentClue = ui.clues.getOrNull(ui.currentIndex) ?: return

    // For fade-in animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Text(
                        text = " Clue Solved!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = currentClue.info,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (ui.currentIndex == ui.clues.lastIndex) {
                                // Last clue → navigate to completed
                                navController.navigate("completed")
                            } else {
                                // Not last clue → go to next clue
                                vm.advanceToNextClue()
                                onNext()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = if (ui.currentIndex == ui.clues.lastIndex) "Finish" else "➡ Next Clue",
                            fontSize = 18.sp
                        )
                    }

                }
            }
        }
    }
}
