package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VerbDao {
    // --- Verb Plants (Garden) ---
    @Query("SELECT * FROM verb_plants ORDER BY lastCompletedAt DESC")
    fun getAllPlants(): Flow<List<VerbPlantEntity>>

    @Query("SELECT COUNT(*) FROM verb_plants")
    fun getPlantCount(): Flow<Int>

    @Query("SELECT * FROM verb_plants WHERE id = :id LIMIT 1")
    suspend fun getPlantById(id: String): VerbPlantEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: VerbPlantEntity)

    @Query("DELETE FROM verb_plants WHERE id = :id")
    suspend fun deletePlantById(id: String)

    // --- Mistakes (Review) ---
    @Query("SELECT * FROM mistakes ORDER BY lastMistakeAt DESC")
    fun getAllMistakes(): Flow<List<MistakeEntity>>

    @Query("SELECT COUNT(*) FROM mistakes")
    fun getMistakeCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: MistakeEntity)

    @Query("DELETE FROM mistakes WHERE verbId = :verbId")
    suspend fun deleteMistake(verbId: String)

    @Query("DELETE FROM mistakes")
    suspend fun clearAllMistakes()
}
