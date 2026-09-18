package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mistakes")
data class MistakeEntity(
    @PrimaryKey val verbId: String, // e.g. "go"
    val base: String,
    val past: String,
    val pastParticiple: String,
    val meaningKo: String,
    val mistakeStage: String, // "PAST" or "PARTICIPLE"
    val lastMistakeAt: Long = System.currentTimeMillis()
)
