/*
 * Assignment 6 - Mobile Treasure Hun
 * Name: Anshu Avinash
 * Course: CS 492
 * Date: 08/11/25
 */
package com.example.treasurehunt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.treasurehunt.data.ClueRepository
import com.example.treasurehunt.model.Clue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HuntUiState(
    val huntName: String = "",
    val clues: List<Clue> = emptyList(),
    val currentIndex: Int = 0,
    val started: Boolean = false,
    val elapsedMillis: Long = 0L,
    val completed: Boolean = false // NEW FLAG
)

class HuntViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ClueRepository(application)
    private val _uiState = MutableStateFlow(HuntUiState())
    val uiState: StateFlow<HuntUiState> = _uiState

    init {
        viewModelScope.launch {
            val hunt = repo.loadHunt()
            _uiState.value = _uiState.value.copy(
                huntName = hunt.huntName,
                clues = hunt.clues
            )
        }
    }

    fun startHunt() {
        _uiState.value = _uiState.value.copy(
            started = true,
            elapsedMillis = 0L,
            currentIndex = 0,
            completed = false
        )
    }

    fun advanceToNextClue() {
        val current = _uiState.value.currentIndex
        val lastIndex = _uiState.value.clues.lastIndex
        println("Current index: ${_uiState.value.currentIndex}")
        println("Last index: ${lastIndex}")

        if (current < lastIndex) {
            // Move to next clue
            _uiState.value = _uiState.value.copy(
                currentIndex = current + 1
            )
        } else if (current == lastIndex ) {
            // Only mark complete after last clue solved
            _uiState.value = _uiState.value.copy(
                currentIndex = current, // stay on last clue index
                started = false,
                completed = true
            )
        }
    }





    fun setElapsed(ms: Long) {
        _uiState.value = _uiState.value.copy(elapsedMillis = ms)
    }
}