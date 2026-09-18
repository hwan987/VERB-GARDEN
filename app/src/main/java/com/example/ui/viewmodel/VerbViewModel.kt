package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundEffectManager
import com.example.data.local.AppDatabase
import com.example.data.local.MistakeEntity
import com.example.data.local.VerbPlantEntity
import com.example.data.model.Verb
import com.example.data.repository.VerbRepository
import com.example.ui.components.PlantStage
import com.example.util.TtsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    GAME,
    RESULT,
    GARDEN,
    REVIEW
}

data class GameSessionState(
    val verbs: List<Verb> = emptyList(),
    val currentIndex: Int = 0,
    val stage: PlantStage = PlantStage.SPROUT, // In a verb question: starts by testing Past (Sprout), then Participle (Flower)
    val score: Int = 0,
    val correctCount: Int = 0,
    val currentChoices: List<String> = emptyList(),
    val selectedChoice: String? = null,
    val isAnswerCorrect: Boolean? = null,
    val choicesEnabled: Boolean = true, // Always immediately active and responsive
    val hadMistakeOnCurrentVerb: Boolean = false,
    val isSessionComplete: Boolean = false,
    val completedVerbs: List<Verb> = emptyList(),
    val incorrectVerbs: List<Verb> = emptyList(),
    val isReviewSession: Boolean = false,
    val isPastRevealed: Boolean = false,
    val isParticipleRevealed: Boolean = false,
    val isWaitingForNext: Boolean = false
) {
    val currentVerb: Verb?
        get() = verbs.getOrNull(currentIndex)
    val totalCount: Int
        get() = verbs.size
}

class VerbViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VerbRepository
    val ttsManager: TtsManager = TtsManager(application)
    val soundEffectManager: SoundEffectManager = SoundEffectManager()

    init {
        val db = AppDatabase.getInstance(application)
        repository = VerbRepository(db.verbDao())
    }

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _sessionState = MutableStateFlow(GameSessionState())
    val sessionState: StateFlow<GameSessionState> = _sessionState.asStateFlow()

    val allPlants: StateFlow<List<VerbPlantEntity>> = repository.allPlants
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val plantCount: StateFlow<Int> = repository.plantCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val allMistakes: StateFlow<List<MistakeEntity>> = repository.allMistakes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val mistakeCount: StateFlow<Int> = repository.mistakeCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // For garden details dialog
    private val _selectedGardenPlant = MutableStateFlow<VerbPlantEntity?>(null)
    val selectedGardenPlant: StateFlow<VerbPlantEntity?> = _selectedGardenPlant.asStateFlow()

    private var autoAdvanceJob: Job? = null

    fun selectGardenPlant(plant: VerbPlantEntity?) {
        _selectedGardenPlant.value = plant
    }

    fun navigateTo(screen: AppScreen) {
        autoAdvanceJob?.cancel()
        ttsManager.stop()
        _currentScreen.value = screen
    }

    /**
     * Starts a standard 10-question game
     */
    fun startNewGame() {
        val verbs = repository.getVerbsForSession(10)
        initGameWithVerbs(verbs, isReview = false)
    }

    /**
     * Starts a review game using the current mistakes list
     */
    fun startReviewGame() {
        val mistakes = allMistakes.value
        val verbs = repository.resolveMistakesToVerbs(mistakes).ifEmpty {
            // Fallback to sample verbs if no mistakes
            repository.getVerbsForSession(5)
        }
        initGameWithVerbs(verbs, isReview = true)
    }

    /**
     * Practice a single verb from the garden
     */
    fun practiceSingleVerb(verbBase: String) {
        val verb = Verb.DEFAULT_VERBS.find { it.base == verbBase } ?: return
        initGameWithVerbs(listOf(verb), isReview = false)
    }

    private fun initGameWithVerbs(verbs: List<Verb>, isReview: Boolean) {
        if (verbs.isEmpty()) return
        autoAdvanceJob?.cancel()
        val firstVerb = verbs[0]
        val pastChoices = firstVerb.generatePastOptions()

        _sessionState.value = GameSessionState(
            verbs = verbs,
            currentIndex = 0,
            stage = PlantStage.SPROUT, // First challenge: Past tense (Seed -> Sprout)
            score = 0,
            correctCount = 0,
            currentChoices = pastChoices,
            selectedChoice = null,
            isAnswerCorrect = null,
            choicesEnabled = true, // Always immediately active and clickable
            hadMistakeOnCurrentVerb = false,
            isSessionComplete = false,
            completedVerbs = emptyList(),
            incorrectVerbs = emptyList(),
            isReviewSession = isReview,
            isPastRevealed = false,
            isParticipleRevealed = false,
            isWaitingForNext = false
        )

        _currentScreen.value = AppScreen.GAME
        playPronunciationForCurrentStage()
    }

    /**
     * Pronunciation playback:
     * Plays the target word audio in the background without freezing button interactivity.
     */
    fun playPronunciationForCurrentStage() {
        val current = _sessionState.value.currentVerb ?: return
        val wordToSpeak = when (_sessionState.value.stage) {
            PlantStage.SEED -> current.base
            PlantStage.SPROUT -> current.past
            PlantStage.FLOWER -> current.pastParticiple
        }

        ttsManager.speak(word = wordToSpeak)
    }

    /**
     * [한 번 더 듣기] button
     */
    fun replayCurrentPronunciation() {
        playPronunciationForCurrentStage()
    }

    /**
     * User selects one of the 3 choices.
     * Evaluates the choice, provides immediate audio/visual feedback, and smoothly advances.
     */
    fun selectChoice(choice: String) {
        val state = _sessionState.value
        // Prevent multiple conflicting taps while an answer is being evaluated
        if (state.isAnswerCorrect == true) return

        val current = state.currentVerb ?: return
        val targetAnswer = when (state.stage) {
            PlantStage.SPROUT -> current.past
            PlantStage.FLOWER -> current.pastParticiple
            else -> current.base
        }

        val isCorrect = choice.equals(targetAnswer, ignoreCase = true)

        if (isCorrect) {
            autoAdvanceJob?.cancel()
            val nextScore = state.score + 10

            if (state.stage == PlantStage.SPROUT) {
                // Correct past form! Play cute chime, reveal past form, growth to sprout
                soundEffectManager.playCorrect()
                _sessionState.value = state.copy(
                    selectedChoice = choice,
                    isAnswerCorrect = true,
                    score = nextScore,
                    isPastRevealed = true,
                    isWaitingForNext = true
                )

                // Smooth auto-advance after 1.1s so the user sees the sprout grow!
                autoAdvanceJob = viewModelScope.launch {
                    delay(1150)
                    advanceToNextStep()
                }
            } else {
                // Correct past participle form! Play bloom fanfare, flower blooms!
                soundEffectManager.playTadaBloom()
                val newlyCompleted = state.completedVerbs + current
                val newCorrectCount = if (!state.hadMistakeOnCurrentVerb) state.correctCount + 1 else state.correctCount

                viewModelScope.launch {
                    repository.saveCompletedPlant(current)
                }

                _sessionState.value = state.copy(
                    selectedChoice = choice,
                    isAnswerCorrect = true,
                    score = nextScore,
                    correctCount = newCorrectCount,
                    completedVerbs = newlyCompleted,
                    isParticipleRevealed = true,
                    isWaitingForNext = true
                )

                // Smooth auto-advance to next verb after 1.4s so user can enjoy full bloom!
                autoAdvanceJob = viewModelScope.launch {
                    delay(1400)
                    advanceToNextStep()
                }
            }
        } else {
            // Incorrect answer! Play gentle wrong boing sound
            soundEffectManager.playWrong()
            val stageName = if (state.stage == PlantStage.SPROUT) "PAST" else "PARTICIPLE"
            val newIncorrect = (state.incorrectVerbs + current).distinctBy { it.id }

            _sessionState.value = state.copy(
                selectedChoice = choice,
                isAnswerCorrect = false,
                hadMistakeOnCurrentVerb = true,
                incorrectVerbs = newIncorrect
            )

            viewModelScope.launch {
                // Save mistake to Room
                repository.recordMistake(current, stageName)

                // Show correction feedback for 1.3s, then allow another try
                delay(1300)
                if (_sessionState.value.isAnswerCorrect == false) {
                    _sessionState.value = _sessionState.value.copy(
                        selectedChoice = null,
                        isAnswerCorrect = null,
                        choicesEnabled = true
                    )
                }
            }
        }
    }

    /**
     * Advances to the next step:
     * Either moves from Sprout (Past) -> Flower (Participle), or moves to the next verb/result.
     * Can be invoked automatically by the timer or immediately by user tapping "다음".
     */
    fun advanceToNextStep() {
        autoAdvanceJob?.cancel()
        val state = _sessionState.value
        val current = state.currentVerb ?: return

        soundEffectManager.playPop()

        if (state.stage == PlantStage.SPROUT) {
            // Advance to Flower stage (Past Participle quiz)
            val participleChoices = current.generateParticipleOptions()
            _sessionState.value = state.copy(
                stage = PlantStage.FLOWER,
                currentChoices = participleChoices,
                selectedChoice = null,
                isAnswerCorrect = null,
                choicesEnabled = true,
                isPastRevealed = true,
                isWaitingForNext = false
            )
            playPronunciationForCurrentStage()
        } else {
            // Flower stage completed, move to next verb question or finish game
            val nextIndex = state.currentIndex + 1
            if (nextIndex < state.verbs.size) {
                val nextVerb = state.verbs[nextIndex]
                val nextPastChoices = nextVerb.generatePastOptions()
                _sessionState.value = state.copy(
                    currentIndex = nextIndex,
                    stage = PlantStage.SPROUT,
                    currentChoices = nextPastChoices,
                    selectedChoice = null,
                    isAnswerCorrect = null,
                    choicesEnabled = true,
                    hadMistakeOnCurrentVerb = false,
                    isPastRevealed = false,
                    isParticipleRevealed = false,
                    isWaitingForNext = false
                )
                playPronunciationForCurrentStage()
            } else {
                // Game finished!
                _sessionState.value = state.copy(
                    isSessionComplete = true,
                    isWaitingForNext = false
                )
                _currentScreen.value = AppScreen.RESULT
            }
        }
    }

    /**
     * Skip or force advance if the student is having trouble with a question
     */
    fun skipOrForceAdvance() {
        autoAdvanceJob?.cancel()
        advanceToNextStep()
    }

    /**
     * Delete/Clear a mistake from review list
     */
    fun removeMistake(verbId: String) {
        viewModelScope.launch {
            repository.removeMistake(verbId)
        }
    }

    /**
     * Clear all mistakes
     */
    fun clearAllMistakes() {
        viewModelScope.launch {
            repository.clearMistakes()
        }
    }

    /**
     * Speak arbitrary word (e.g. from Garden or Review list)
     */
    fun speakWord(word: String) {
        ttsManager.speak(word)
    }

    /**
     * Plays the full 3-step sequence: Base -> Past -> Past Participle
     * e.g., "go" -> "went" -> "gone"
     */
    fun speakAllThreeForms() {
        val current = _sessionState.value.currentVerb ?: return
        viewModelScope.launch {
            ttsManager.speak(current.base)
            delay(1000)
            ttsManager.speak(current.past)
            delay(1000)
            ttsManager.speak(current.pastParticiple)
        }
    }

    fun playPopSound() {
        soundEffectManager.playPop()
    }

    fun playTapSound() {
        soundEffectManager.playTap()
    }

    override fun onCleared() {
        super.onCleared()
        autoAdvanceJob?.cancel()
        ttsManager.destroy()
    }
}
