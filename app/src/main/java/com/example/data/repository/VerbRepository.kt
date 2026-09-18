package com.example.data.repository

import com.example.data.local.MistakeEntity
import com.example.data.local.VerbDao
import com.example.data.local.VerbPlantEntity
import com.example.data.model.Verb
import kotlinx.coroutines.flow.Flow

class VerbRepository(private val verbDao: VerbDao) {

    val allPlants: Flow<List<VerbPlantEntity>> = verbDao.getAllPlants()
    val plantCount: Flow<Int> = verbDao.getPlantCount()

    val allMistakes: Flow<List<MistakeEntity>> = verbDao.getAllMistakes()
    val mistakeCount: Flow<Int> = verbDao.getMistakeCount()

    suspend fun saveCompletedPlant(verb: Verb) {
        val existing = verbDao.getPlantById(verb.id)
        val completedCount = (existing?.timesCompleted ?: 0) + 1
        val entity = VerbPlantEntity(
            id = verb.id,
            base = verb.base,
            past = verb.past,
            pastParticiple = verb.pastParticiple,
            meaningKo = verb.meaningKo,
            flowerTypeName = verb.flowerType.name,
            growthStage = 3,
            timesCompleted = completedCount,
            lastCompletedAt = System.currentTimeMillis()
        )
        verbDao.insertPlant(entity)
        // If it was in mistakes, remove it upon mastery
        verbDao.deleteMistake(verb.id)
    }

    suspend fun recordMistake(verb: Verb, stage: String) {
        val entity = MistakeEntity(
            verbId = verb.id,
            base = verb.base,
            past = verb.past,
            pastParticiple = verb.pastParticiple,
            meaningKo = verb.meaningKo,
            mistakeStage = stage,
            lastMistakeAt = System.currentTimeMillis()
        )
        verbDao.insertMistake(entity)
    }

    suspend fun removeMistake(verbId: String) {
        verbDao.deleteMistake(verbId)
    }

    suspend fun clearMistakes() {
        verbDao.clearAllMistakes()
    }

    /**
     * Prepares a session of 10 verbs.
     */
    fun getVerbsForSession(count: Int = 10): List<Verb> {
        return Verb.DEFAULT_VERBS.shuffled().take(count)
    }

    /**
     * Resolves mistake entities to full Verb objects for review sessions
     */
    fun resolveMistakesToVerbs(mistakes: List<MistakeEntity>): List<Verb> {
        val allMap = Verb.DEFAULT_VERBS.associateBy { it.id }
        return mistakes.mapNotNull { allMap[it.verbId] }
    }
}
