/*
 * Assignment 6 - Mobile Treasure Hun
 * Name: Anshu Avinash
 * Course: CS 492
 * Date: 08/11/25
 */

package com.example.treasurehunt.model

data class Clue(
    val id: Int,
    val text: String,
    val hint: String,
    val lat: Double,
    val lon: Double,
    val info: String
)

data class Hunt(
    val huntName: String,
    val clues: List<Clue>
)