package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FlowerType
import kotlin.math.cos
import kotlin.math.sin

enum class PlantStage(val step: Int, val label: String, val subtitle: String) {
    SEED(1, "씨앗", "1/3"),
    SPROUT(2, "새싹", "2/3"),
    FLOWER(3, "꽃", "3/3")
}

@Composable
fun PlantGrowthGraphic(
    stage: PlantStage,
    flowerType: FlowerType,
    isSpeaking: Boolean = false,
    isCelebrating: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "plantAnimation")

    // 바람에 살랑거리는 자연스러운 움직임
    val swayOffset by infiniteTransition.animateFloat(
        initialValue = -3.5f,
        targetValue = 3.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sway"
    )

    // 말할 때/성장할 때 리듬감 있는 펄스
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isSpeaking) 1.08f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSpeaking) 420 else 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // 반짝이는 축하 파티클 회전
    val sparkleAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkleRotation"
    )

    // 정답을 맞혔을 때 "짜잔!" 하고 자라나는 탄성 바운스 스케일 (Spring Animation)
    var triggerBounce by remember(stage, isCelebrating) { mutableStateOf(false) }
    val tadaBounceScale by animateFloatAsState(
        targetValue = if (isCelebrating) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tadaBounce"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("plant_growth_graphic"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Graphic Canvas Box
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(tadaBounceScale * pulseScale),
            contentAlignment = Alignment.Center
        ) {
            // 1. 따스한 햇살 & 오라 효과 (Background Aura)
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val center = Offset(size.width / 2, size.height * 0.52f)
                val auraColors = if (stage == PlantStage.FLOWER) {
                    listOf(
                        flowerType.primaryColor.copy(alpha = 0.28f),
                        Color(0x33FFE082),
                        Color.Transparent
                    )
                } else if (stage == PlantStage.SPROUT) {
                    listOf(
                        Color(0x40A5D6A7),
                        Color(0x20FFF59D),
                        Color.Transparent
                    )
                } else {
                    listOf(
                        Color(0x30FFE082),
                        Color(0x15A5D6A7),
                        Color.Transparent
                    )
                }

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = auraColors,
                        center = center,
                        radius = size.width * 0.52f
                    ),
                    radius = size.width * 0.52f,
                    center = center
                )

                // 짜잔 축하 이펙트: 꽃가루/별빛 파티클 8개 방사
                if (isCelebrating || stage == PlantStage.FLOWER) {
                    val particleCount = 10
                    for (i in 0 until particleCount) {
                        val baseRad = Math.toRadians((i * (360.0 / particleCount) + sparkleAngle).toDouble())
                        val dist = size.width * 0.42f
                        val px = center.x + (cos(baseRad) * dist).toFloat()
                        val py = center.y + (sin(baseRad) * dist).toFloat()

                        drawCircle(
                            color = if (i % 2 == 0) Color(0xFFFFD54F) else flowerType.primaryColor,
                            radius = if (i % 3 == 0) 5f else 3.5f,
                            center = Offset(px, py)
                        )
                    }
                }
            }

            // 2. 메인 식물 렌더링 캔버스 (화분, 줄기, 잎, 꽃)
            Canvas(
                modifier = Modifier.size(190.dp)
            ) {
                val w = size.width
                val h = size.height

                val potTopY = h * 0.72f
                val potBottomY = h * 0.94f
                val potWidthTop = w * 0.54f
                val potWidthBottom = w * 0.40f

                // 화분 받침대 / 테두리
                drawRoundRect(
                    color = Color(0xFF8D6E63),
                    topLeft = Offset((w - potWidthTop * 1.08f) / 2, potTopY - 8f),
                    size = Size(potWidthTop * 1.08f, 16f),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                // 화분 본체
                val potPath = Path().apply {
                    moveTo((w - potWidthTop) / 2, potTopY)
                    lineTo((w + potWidthTop) / 2, potTopY)
                    lineTo((w + potWidthBottom) / 2, potBottomY)
                    lineTo((w - potWidthBottom) / 2, potBottomY)
                    close()
                }
                drawPath(potPath, color = Color(0xFFA1887F))

                // 비옥한 토양 (검붉은 흙)
                drawOval(
                    color = Color(0xFF4E342E),
                    topLeft = Offset((w - potWidthTop * 0.92f) / 2, potTopY - 6f),
                    size = Size(potWidthTop * 0.92f, 12f)
                )

                when (stage) {
                    PlantStage.SEED -> {
                        // 1단계: 씨앗 (발아 준비 중인 영양 가득한 씨앗)
                        val seedX = w / 2
                        val seedY = potTopY - 8f

                        // 싹트는 연둣빛 어린 눈
                        val curlPath = Path().apply {
                            moveTo(seedX, seedY)
                            cubicTo(
                                seedX - 8f, seedY - 14f,
                                seedX + 5f, seedY - 22f,
                                seedX + 8f, seedY - 26f
                            )
                        }
                        drawPath(
                            path = curlPath,
                            color = Color(0xFF81C784),
                            style = Stroke(width = 5.5f, cap = StrokeCap.Round)
                        )

                        // 씨앗 본체 (강낭콩 모양)
                        drawOval(
                            color = Color(0xFFD7CCC8),
                            topLeft = Offset(seedX - 17f, seedY - 11f),
                            size = Size(34f, 23f)
                        )
                        drawOval(
                            color = Color(0xFF795548),
                            topLeft = Offset(seedX - 15f, seedY - 9f),
                            size = Size(30f, 19f)
                        )

                        // 반짝이는 생명의 물방울 2개
                        drawCircle(
                            color = Color(0xFF42A5F5),
                            radius = 5.5f,
                            center = Offset(seedX - 28f, seedY - 26f)
                        )
                        drawCircle(
                            color = Color(0xFF80D8FF),
                            radius = 4f,
                            center = Offset(seedX + 32f, seedY - 28f)
                        )
                    }

                    PlantStage.SPROUT -> {
                        // 2단계: 새싹 (쑥쑥 자라난 줄기와 마주 보는 떡잎/본잎)
                        val stemBottomX = w / 2
                        val stemBottomY = potTopY - 4f
                        val stemTopX = w / 2 + swayOffset
                        val stemTopY = h * 0.40f

                        // 튼튼한 연녹색 줄기
                        val stemPath = Path().apply {
                            moveTo(stemBottomX, stemBottomY)
                            cubicTo(
                                stemBottomX - 4f, (stemBottomY + stemTopY) / 2,
                                stemTopX + 4f, (stemBottomY + stemTopY) / 2,
                                stemTopX, stemTopY
                            )
                        }
                        drawPath(
                            path = stemPath,
                            color = Color(0xFF4CAF50),
                            style = Stroke(width = 8f, cap = StrokeCap.Round)
                        )

                        // 떡잎 1 (왼쪽) - 과학 수업에서 배우는 영양 저장 잎
                        val leftLeaf = Path().apply {
                            moveTo(stemTopX, stemTopY + 20f)
                            cubicTo(
                                stemTopX - 38f, stemTopY + 12f,
                                stemTopX - 44f, stemTopY - 16f,
                                stemTopX - 14f, stemTopY - 10f
                            )
                            cubicTo(
                                stemTopX - 4f, stemTopY - 4f,
                                stemTopX, stemTopY + 12f,
                                stemTopX, stemTopY + 20f
                            )
                        }
                        drawPath(leftLeaf, color = Color(0xFF81C784))
                        drawPath(leftLeaf, color = Color(0xFF2E7D32), style = Stroke(width = 2.5f))

                        // 떡잎 2 (오른쪽)
                        val rightLeaf = Path().apply {
                            moveTo(stemTopX, stemTopY + 12f)
                            cubicTo(
                                stemTopX + 38f, stemTopY + 4f,
                                stemTopX + 46f, stemTopY - 24f,
                                stemTopX + 16f, stemTopY - 16f
                            )
                            cubicTo(
                                stemTopX + 4f, stemTopY - 8f,
                                stemTopX, stemTopY + 4f,
                                stemTopX, stemTopY + 12f
                            )
                        }
                        drawPath(rightLeaf, color = Color(0xFF66BB6A))
                        drawPath(rightLeaf, color = Color(0xFF2E7D32), style = Stroke(width = 2.5f))

                        // 줄기 끝에서 새로 돋아나는 어린 본잎 눈
                        drawCircle(
                            color = Color(0xFFAED581),
                            radius = 7f,
                            center = Offset(stemTopX, stemTopY)
                        )
                    }

                    PlantStage.FLOWER -> {
                        // 3단계: 만개한 꽃 (초등 과학 연계 식물의 고유한 아름다운 꽃)
                        val stemBottomX = w / 2
                        val stemBottomY = potTopY - 4f
                        val stemTopX = w / 2 + swayOffset * 0.7f
                        val flowerCenterY = h * 0.28f

                        // 높은 줄기
                        val stemPath = Path().apply {
                            moveTo(stemBottomX, stemBottomY)
                            cubicTo(
                                stemBottomX - 6f, (stemBottomY + flowerCenterY) / 2,
                                stemTopX + 6f, (stemBottomY + flowerCenterY) / 2 + 10f,
                                stemTopX, flowerCenterY + 18f
                            )
                        }
                        drawPath(
                            path = stemPath,
                            color = Color(0xFF388E3C),
                            style = Stroke(width = 9f, cap = StrokeCap.Round)
                        )

                        // 큼직한 본잎들 (광합성 잎)
                        val leafY = (stemBottomY + flowerCenterY) / 2 + 15f
                        val leftLeaf = Path().apply {
                            moveTo(stemBottomX, leafY)
                            cubicTo(
                                stemBottomX - 42f, leafY - 10f,
                                stemBottomX - 48f, leafY - 38f,
                                stemBottomX - 18f, leafY - 24f
                            )
                            close()
                        }
                        drawPath(leftLeaf, color = Color(0xFF4CAF50))

                        val rightLeaf = Path().apply {
                            moveTo(stemBottomX, leafY - 14f)
                            cubicTo(
                                stemBottomX + 44f, leafY - 24f,
                                stemBottomX + 50f, leafY - 54f,
                                stemBottomX + 20f, leafY - 38f
                            )
                            close()
                        }
                        drawPath(rightLeaf, color = Color(0xFF66BB6A))

                        // 꽃잎 (Flower Petals - 식물별 고유한 꽃잎 렌더링)
                        val petalCount = flowerType.petalCount
                        val petalDistance = 33f

                        for (i in 0 until petalCount) {
                            val angle = (2 * Math.PI * i / petalCount).toFloat()
                            val px = stemTopX + cos(angle) * petalDistance
                            val py = flowerCenterY + sin(angle) * petalDistance

                            // 외측 꽃잎 레이어
                            drawCircle(
                                color = flowerType.secondaryColor,
                                radius = 17f,
                                center = Offset(px, py)
                            )
                            // 내측 꽃잎 레이어
                            drawCircle(
                                color = flowerType.primaryColor,
                                radius = 14f,
                                center = Offset(px, py)
                            )
                        }

                        // 꽃의 중심부 (수술과 암술이 위치하는 꽃심)
                        drawCircle(
                            color = flowerType.centerColor,
                            radius = 21f,
                            center = Offset(stemTopX, flowerCenterY)
                        )
                        drawCircle(
                            color = Color(0x33000000),
                            radius = 21f,
                            center = Offset(stemTopX, flowerCenterY),
                            style = Stroke(width = 2.5f)
                        )

                        // 무궁화인 경우 긴 암술/수술 기둥 표현
                        if (flowerType == FlowerType.HIBISCUS) {
                            drawRoundRect(
                                color = Color(0xFFFFD54F),
                                topLeft = Offset(stemTopX - 3f, flowerCenterY - 28f),
                                size = Size(6f, 26f),
                                cornerRadius = CornerRadius(3f, 3f)
                            )
                            drawCircle(
                                color = Color(0xFFD81B60),
                                radius = 4f,
                                center = Offset(stemTopX, flowerCenterY - 29f)
                            )
                        }

                        // 황금빛 꽃가루와 반짝이
                        drawCircle(
                            color = Color(0xFFFFD54F),
                            radius = 4.5f,
                            center = Offset(stemTopX - 44f, flowerCenterY - 32f)
                        )
                        drawCircle(
                            color = Color(0xFFFFE082),
                            radius = 5.5f,
                            center = Offset(stemTopX + 46f, flowerCenterY - 26f)
                        )
                        drawCircle(
                            color = Color(0xFFFFCA28),
                            radius = 4f,
                            center = Offset(stemTopX + 38f, flowerCenterY + 38f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 성장 단계 인디케이터: 씨앗 1/3 → 새싹 2/3 → 꽃 3/3
        GrowthStageIndicator(currentStage = stage)

        Spacer(modifier = Modifier.height(6.dp))

        // 초등학교 5~6학년 과학 교과서 연계 정보 칩 (과학 수업 연계)
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFF1F8E9),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .testTag("science_curriculum_badge")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.size(22.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "과학 교과 연계",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "과학 연계: ${flowerType.koreanName} (${flowerType.englishName})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = flowerType.plantCategory,
                                fontSize = 10.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }

                    Text(
                        text = flowerType.scienceCurriculum + " · " + flowerType.scienceDetail,
                        fontSize = 11.sp,
                        color = Color(0xFF558B2F),
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GrowthStageIndicator(
    currentStage: PlantStage,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .testTag("growth_stage_indicator"),
        color = Color(0xFFE8F5E9),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StageStepItem(
                label = "씨앗",
                step = "1/3",
                icon = Icons.Default.Grass,
                isActive = currentStage == PlantStage.SEED,
                isCompleted = currentStage.step >= 1
            )

            Text("→", color = Color(0xFF81C784), fontWeight = FontWeight.Bold, fontSize = 13.sp)

            StageStepItem(
                label = "새싹",
                step = "2/3",
                icon = Icons.Default.Eco,
                isActive = currentStage == PlantStage.SPROUT,
                isCompleted = currentStage.step >= 2
            )

            Text("→", color = Color(0xFF81C784), fontWeight = FontWeight.Bold, fontSize = 13.sp)

            StageStepItem(
                label = "꽃",
                step = "3/3",
                icon = Icons.Default.LocalFlorist,
                isActive = currentStage == PlantStage.FLOWER,
                isCompleted = currentStage.step >= 3
            )
        }
    }
}

@Composable
private fun StageStepItem(
    label: String,
    step: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    isCompleted: Boolean
) {
    val bgColor = when {
        isActive -> Color(0xFF2E7D32)
        isCompleted -> Color(0xFFC8E6C9)
        else -> Color.Transparent
    }

    val textColor = when {
        isActive -> Color.White
        isCompleted -> Color(0xFF1B5E20)
        else -> Color(0xFF9E9E9E)
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = textColor,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = "$label $step",
            color = textColor,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
        )
    }
}
