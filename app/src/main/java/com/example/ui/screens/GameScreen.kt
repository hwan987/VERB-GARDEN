package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PlantGrowthGraphic
import com.example.ui.components.PlantStage
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.JuaFontFamily
import com.example.ui.viewmodel.GameSessionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    state: GameSessionState,
    isSpeaking: Boolean,
    onReplayAudio: () -> Unit,
    onSelectChoice: (String) -> Unit,
    onAdvanceStep: () -> Unit,
    onSpeakWord: (String) -> Unit,
    onSpeakAllThreeForms: () -> Unit,
    onPlayPopSound: () -> Unit = {},
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentVerb = state.currentVerb
    if (currentVerb == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF1F8E9)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Hearing,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "학습 세션을 준비하고 있습니다... 🌱",
                    fontFamily = FredokaFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onExitGame,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("홈으로 돌아가기")
                }
            }
        }
        return
    }

    val verb = currentVerb
    val scrollState = rememberScrollState()

    val isCelebrating = state.isWaitingForNext || state.isAnswerCorrect == true

    // 새 문제 또는 단계(과거/과거분사)로 넘어왔을 때 목표 단어 발음 자동 1회 재생
    androidx.compose.runtime.LaunchedEffect(state.currentIndex, state.stage) {
        kotlinx.coroutines.delay(250)
        onReplayAudio()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFE8F5E9),
                        Color(0xFFF9FBE7),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단 바: 문제 번호 (왼쪽), 학습 종료 (중앙), 점수 (오른쪽)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFC8E6C9),
                    tonalElevation = 1.dp,
                    modifier = Modifier.testTag("question_number_badge")
                ) {
                    Text(
                        text = "${state.currentIndex + 1} / ${state.totalCount}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1B5E20),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                IconButton(
                    onClick = onExitGame,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("exit_game_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "학습 종료",
                        tint = Color(0xFF558B2F),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFFFECB3),
                    tonalElevation = 1.dp,
                    modifier = Modifier.testTag("score_badge")
                ) {
                    Text(
                        text = "점수: ${state.score}점",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE65100),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 상단 동사 3단 변화 세 칸 (동사원형, 과거형, 과거분사형)
            // 정답을 맞혔을 때 각 카드가 "짜잔!" 하고 탄성 바운스로 자라나며 공개!
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("verb_three_forms_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "동사 3단 변화: ${verb.meaningKo}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242),
                            modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = Color(0xFF388E3C),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "터치 시 발음",
                                fontSize = 10.sp,
                                color = Color(0xFF388E3C),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // 1. 동사원형 칸 (항상 공개, 터치하면 원형 발음)
                        VerbStageBox(
                            title = "동사원형",
                            content = verb.base,
                            subtext = verb.meaningKo,
                            isRevealed = true,
                            isCurrentTarget = false,
                            accentColor = Color(0xFF2E7D32),
                            onClick = { onSpeakWord(verb.base) },
                            modifier = Modifier.weight(1f)
                        )

                        // 2. 과거형 칸 (새싹 단계에서 정답 맞히면 짜잔! 하고 공개)
                        val isPastTarget = state.stage == PlantStage.SPROUT && !state.isPastRevealed
                        VerbStageBox(
                            title = "과거형",
                            content = if (state.isPastRevealed) verb.past else "?",
                            subtext = if (state.isPastRevealed) "✓ 정답!" else if (isPastTarget) "🌱 키우는 중" else "대기",
                            isRevealed = state.isPastRevealed,
                            isCurrentTarget = isPastTarget,
                            accentColor = Color(0xFFE65100),
                            onClick = {
                                if (state.isPastRevealed) onSpeakWord(verb.past)
                                else onReplayAudio()
                            },
                            modifier = Modifier.weight(1f)
                        )

                        // 3. 과거분사형 칸 (꽃 단계에서 정답 맞히면 짜잔! 하고 공개)
                        val isParticipleTarget = state.stage == PlantStage.FLOWER && !state.isParticipleRevealed
                        VerbStageBox(
                            title = "과거분사형",
                            content = if (state.isParticipleRevealed) verb.pastParticiple else "?",
                            subtext = if (state.isParticipleRevealed) "🌸 만개!" else if (isParticipleTarget) "🌸 키우는 중" else "대기",
                            isRevealed = state.isParticipleRevealed,
                            isCurrentTarget = isParticipleTarget,
                            accentColor = Color(0xFFC2185B),
                            onClick = {
                                if (state.isParticipleRevealed) onSpeakWord(verb.pastParticiple)
                                else if (state.stage == PlantStage.FLOWER) onReplayAudio()
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 식물 성장 그래픽 및 1/3 → 2/3 → 3/3 성장 단계
            PlantGrowthGraphic(
                stage = state.stage,
                flowerType = verb.flowerType,
                isSpeaking = isSpeaking,
                isCelebrating = isCelebrating,
                modifier = Modifier.padding(vertical = 0.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 발음 안내 및 [한 번 더 듣기] 버튼 카드
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("audio_prompt_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSpeaking) Color(0xFFFFF9C4) else Color(0xFFF1F8E9)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isSpeaking) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = Color(0xFFF57F17),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🎧 발음 청취 중...",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF57F17)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Hearing,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val targetFormName = if (state.stage == PlantStage.SPROUT) "과거형" else "과거분사형"
                            Text(
                                text = if (state.isWaitingForNext) "발음을 다시 듣거나 다음으로 가세요!" else "$targetFormName 발음의 단어를 고르세요!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1B5E20)
                            )
                        }
                    }

                    // [한 번 더 듣기] 버튼
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF4CAF50),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onPlayPopSound()
                                onReplayAudio()
                            }
                            .testTag("replay_audio_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "한 번 더 듣기",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "한 번 더 듣기",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 3개의 단어 선택지 (동사 형태 퀴즈)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("word_choices_group"),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                state.currentChoices.forEachIndexed { index, option ->
                    val isSelected = state.selectedChoice == option
                    val targetWord = when (state.stage) {
                        PlantStage.SPROUT -> verb.past
                        PlantStage.FLOWER -> verb.pastParticiple
                        else -> verb.base
                    }

                    val cardColor by animateColorAsState(
                        targetValue = when {
                            isSelected && state.isAnswerCorrect == true -> Color(0xFFA5D6A7)
                            isSelected && state.isAnswerCorrect == false -> Color(0xFFEF9A9A)
                            state.isWaitingForNext && option.equals(targetWord, ignoreCase = true) -> Color(0xFFA5D6A7)
                            state.isWaitingForNext -> Color(0xFFF5F5F5)
                            else -> Color.White
                        },
                        label = "choiceColor"
                    )

                    val textColor = when {
                        isSelected && state.isAnswerCorrect == true -> Color(0xFF1B5E20)
                        isSelected && state.isAnswerCorrect == false -> Color(0xFFB71C1C)
                        state.isWaitingForNext && option.equals(targetWord, ignoreCase = true) -> Color(0xFF1B5E20)
                        state.isWaitingForNext -> Color(0xFF757575)
                        else -> Color(0xFF212121)
                    }

                    val canClickChoice = state.isAnswerCorrect != true && !state.isWaitingForNext

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(
                                enabled = canClickChoice,
                                onClick = {
                                    onPlayPopSound()
                                    onSelectChoice(option)
                                }
                            )
                            .testTag("choice_button_$index"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (canClickChoice) 2.dp else 0.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (canClickChoice) Color(0xFFE8F5E9) else Color(0xFFE0E0E0),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            fontFamily = FredokaFontFamily,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (canClickChoice) Color(0xFF2E7D32) else Color(0xFF757575)
                                        )
                                    }
                                }

                                Text(
                                    text = option,
                                    fontFamily = FredokaFontFamily,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )

                                Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                                    if ((isSelected && state.isAnswerCorrect == true) || (state.isWaitingForNext && option.equals(targetWord, ignoreCase = true))) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color(0xFF2E7D32),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else if (isSelected && state.isAnswerCorrect == false) {
                                        Icon(
                                            imageVector = Icons.Default.Error,
                                            contentDescription = null,
                                            tint = Color(0xFFC62828),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 피드백 배너 및 자동/수동 다음 이동 영역
            val targetAnswerWord = when (state.stage) {
                PlantStage.SPROUT -> verb.past
                PlantStage.FLOWER -> verb.pastParticiple
                else -> verb.base
            }

            if (state.isWaitingForNext || state.isAnswerCorrect == true) {
                val isLastVerbInSession = state.currentIndex + 1 >= state.totalCount
                val nextButtonText = when {
                    state.stage == PlantStage.SPROUT -> "다음 단계: 과거분사(꽃) 피우기 🌸 →"
                    isLastVerbInSession -> "학습 결과 확인하기 🏆 →"
                    else -> "다음 문제로 이동 (${state.currentIndex + 2}/${state.totalCount}) 🌿 →"
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 축하 피드백 배너
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFC8E6C9),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (state.stage == PlantStage.SPROUT) {
                                    "🎉 정답! 과거형 (${verb.past})이 자라났어요! 🌱"
                                } else {
                                    "🎉 대단해요! 과거분사 (${verb.pastParticiple})로 만개! 🌸"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // 꽃까지 완성했을 때 3단 변화 전체를 연달아 들을 수 있는 버튼
                    if (state.stage == PlantStage.FLOWER) {
                        OutlinedButton(
                            onClick = {
                                onPlayPopSound()
                                onSpeakAllThreeForms()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .padding(bottom = 4.dp)
                                .testTag("speak_all_three_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.2.dp, Color(0xFF388E3C)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1B5E20)),
                            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QueueMusic,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF2E7D32)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🎶 3단 변화 전체 듣기 (${verb.base} → ${verb.past} → ${verb.pastParticiple})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // 다음 단계로 이동하는 메인 버튼 (사용자가 직접 확인 후 클릭)
                    Button(
                        onClick = onAdvanceStep,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("advance_step_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.stage == PlantStage.SPROUT) Color(0xFF2E7D32) else Color(0xFF1B5E20),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isLastVerbInSession && state.stage == PlantStage.FLOWER) Icons.Default.EmojiEvents else Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = nextButtonText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "👆 발음을 확인한 후 다음 버튼을 누르세요.",
                        fontSize = 11.sp,
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 3.dp, bottom = 4.dp)
                    )
                }
            } else if (state.isAnswerCorrect == false) {
                // 오답 발생 시: 안내 및 다음으로 바로 넘어갈 수 있는 스킵 버튼 제공
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFCDD2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = Color(0xFFC62828),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "💡 정답은 '$targetAnswerWord' 예요!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB71C1C),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onAdvanceStep,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("skip_question_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.2.dp, Color(0xFF388E3C)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1B5E20))
                    ) {
                        Text(
                            text = "정답 확인 후 다음으로 넘어가기 →",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * 상단 3단 변화 슬롯 박스 (동사원형, 과거형, 과거분사형)
 * 정답을 맞혀 새로 공개될 때 "짜잔!" 탄성 바운스 스케일과 반짝이 효과 발동!
 */
@Composable
private fun VerbStageBox(
    title: String,
    content: String,
    subtext: String,
    isRevealed: Boolean,
    isCurrentTarget: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 공개될 때 "짜잔!" 탄성 바운스 애니메이션
    val tadaCardScale by animateFloatAsState(
        targetValue = if (isRevealed) 1.0f else 0.96f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tadaCard"
    )

    val backgroundColor = when {
        isRevealed -> accentColor.copy(alpha = 0.12f)
        isCurrentTarget -> Color(0xFFFFFDE7)
        else -> Color(0xFFF5F5F5)
    }

    val border = if (isCurrentTarget) {
        BorderStroke(2.dp, accentColor)
    } else if (isRevealed) {
        BorderStroke(1.2.dp, accentColor.copy(alpha = 0.45f))
    } else {
        BorderStroke(1.dp, Color(0xFFEEEEEE))
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor,
        border = border,
        modifier = modifier
            .scale(tadaCardScale)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isRevealed || isCurrentTarget) accentColor else Color(0xFF757575)
                )
                if (isRevealed) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = content,
                fontFamily = FredokaFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isRevealed) accentColor else if (isCurrentTarget) Color(0xFF212121) else Color(0xFF9E9E9E),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(1.dp))

            Text(
                text = subtext,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = if (isRevealed) accentColor else Color(0xFF757575),
                textAlign = TextAlign.Center
            )
        }
    }
}
