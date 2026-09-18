package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.GameScreen
import com.example.ui.screens.GardenScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.ReviewScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.VerbViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: VerbViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GrowingVerbsApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun GrowingVerbsApp(viewModel: VerbViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val sessionState by viewModel.sessionState.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsStateWithLifecycle()
    val plantCount by viewModel.plantCount.collectAsStateWithLifecycle()
    val mistakeCount by viewModel.mistakeCount.collectAsStateWithLifecycle()
    val allPlants by viewModel.allPlants.collectAsStateWithLifecycle()
    val allMistakes by viewModel.allMistakes.collectAsStateWithLifecycle()
    val selectedGardenPlant by viewModel.selectedGardenPlant.collectAsStateWithLifecycle()

    var showExitDialog by remember { mutableStateOf(false) }

    // Handle Android System Back button gracefully
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        when (currentScreen) {
            AppScreen.GAME -> showExitDialog = true
            AppScreen.RESULT -> viewModel.navigateTo(AppScreen.HOME)
            AppScreen.GARDEN -> viewModel.navigateTo(AppScreen.HOME)
            AppScreen.REVIEW -> viewModel.navigateTo(AppScreen.HOME)
            AppScreen.HOME -> { /* Exit app */ }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(text = "학습을 중단하시겠어요?") },
            text = { Text(text = "지금 나가면 현재 진행 중인 게임 기록이 저장되지 않습니다.") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        viewModel.navigateTo(AppScreen.HOME)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("나가기")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showExitDialog = false }) {
                    Text("계속하기")
                }
            }
        )
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screenTransition"
    ) { screen ->
        when (screen) {
            AppScreen.HOME -> {
                HomeScreen(
                    plantCount = plantCount,
                    mistakeCount = mistakeCount,
                    plants = allPlants,
                    onStartGame = { viewModel.startNewGame() },
                    onOpenGarden = { viewModel.navigateTo(AppScreen.GARDEN) },
                    onOpenReview = { viewModel.navigateTo(AppScreen.REVIEW) },
                    onPlayPopSound = { viewModel.playPopSound() }
                )
            }

            AppScreen.GAME -> {
                GameScreen(
                    state = sessionState,
                    isSpeaking = isSpeaking,
                    onReplayAudio = { viewModel.replayCurrentPronunciation() },
                    onSelectChoice = { choice -> viewModel.selectChoice(choice) },
                    onAdvanceStep = { viewModel.advanceToNextStep() },
                    onSpeakWord = { word -> viewModel.speakWord(word) },
                    onSpeakAllThreeForms = { viewModel.speakAllThreeForms() },
                    onPlayPopSound = { viewModel.playPopSound() },
                    onExitGame = { showExitDialog = true }
                )
            }

            AppScreen.RESULT -> {
                ResultScreen(
                    state = sessionState,
                    onReviewMistakes = { viewModel.startReviewGame() },
                    onOpenGarden = { viewModel.navigateTo(AppScreen.GARDEN) },
                    onPlayAgain = { viewModel.startNewGame() },
                    onSpeakWord = { word -> viewModel.speakWord(word) }
                )
            }

            AppScreen.GARDEN -> {
                GardenScreen(
                    plants = allPlants,
                    totalCount = plantCount,
                    selectedPlant = selectedGardenPlant,
                    onSelectPlant = { plant -> viewModel.selectGardenPlant(plant) },
                    onSpeakWord = { word -> viewModel.speakWord(word) },
                    onPracticeVerb = { verbBase -> viewModel.practiceSingleVerb(verbBase) },
                    onBack = { viewModel.navigateTo(AppScreen.HOME) }
                )
            }

            AppScreen.REVIEW -> {
                ReviewScreen(
                    mistakes = allMistakes,
                    onStartReviewGame = { viewModel.startReviewGame() },
                    onSpeakWord = { word -> viewModel.speakWord(word) },
                    onClearAll = { viewModel.clearAllMistakes() },
                    onBack = { viewModel.navigateTo(AppScreen.HOME) }
                )
            }
        }
    }
}

