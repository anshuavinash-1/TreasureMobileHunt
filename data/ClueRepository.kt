/*
 * Assignment 6 - Mobile Treasure Hun
 * Name: Anshu Avinash
 * Course: CS 492
 * Date: 08/11/25
 */
package com.example.treasurehunt.data

import android.content.Context
import com.example.treasurehunt.model.Hunt
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ClueRepository(private val context: Context) {
    fun loadHunt(): Hunt {
        val inputStream = context.resources.openRawResource(
            context.resources.getIdentifier("clues", "raw", context.packageName)
        )
        val json = inputStream.bufferedReader().use { it.readText() }
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(Hunt::class.java)
        return adapter.fromJson(json) ?: throw IllegalStateException("Invalid clues.json")
    }
}