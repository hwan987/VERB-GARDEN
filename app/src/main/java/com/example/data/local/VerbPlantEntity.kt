package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verb_plants")
data class VerbPlantEntity(
    @PrimaryKey val id: String, // verb base, e.g. "go"
    val base: String,
    val past: String,
    val pastParticiple: String,
    val meaningKo: String,
    val flowerTypeName: String,
    val growthStage: Int = 3, // 1: seed, 2: sprout, 3: blooming flower
    val timesCompleted: Int = 1,
    val lastCompletedAt: Long = System.currentTimeMillis()
)
