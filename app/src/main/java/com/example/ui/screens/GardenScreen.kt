package com.example.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VerbPlantEntity
import com.example.data.model.FlowerType
import com.example.ui.components.FlowerPlantBadge
import com.example.ui.components.PlantGrowthGraphic
import com.example.ui.components.PlantStage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenScreen(
    plants: List<VerbPlantEntity>,
    totalCount: Int,
    selectedPlant: VerbPlantEntity?,
    onSelectPlant: (VerbPlantEntity?) -> Unit,
    onSpeakWord: (String) -> Unit,
    onPracticeVerb: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "나만의 동사 정원",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("garden_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE8F5E9),
                    titleContentColor = Color(0xFF1B5E20),
                    navigationIconContentColor = Color(0xFF1B5E20)
                )
            )
        },
        containerColor = Color(0xFFF7FAF7)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 완성한 식물의 전체 개수 배너
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("garden_total_count_banner")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFC8E6C9),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LocalFlorist,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "완성한 동사 식물",
                                fontSize = 13.sp,
                                color = Color(0xFF757575),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "총 ${totalCount}그루의 꽃",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1B5E20)
                            )
                        }
                    }

                    Text(
                        text = "꽃을 눌러 복습하세요 🌸",
                        fontSize = 12.sp,
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 식물 그리드
            if (plants.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            tint = Color(0xFF81C784),
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "아직 정원에 핀 꽃이 없어요",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF388E3C)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "학습을 완료하면 아름다운 꽃이 정원에 심어집니다!",
                            fontSize = 13.sp,
                            color = Color(0xFF757575),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("garden_plants_grid")
                ) {
                    items(plants, key = { it.id }) { plant ->
                        val flowerType = runCatching {
                            FlowerType.valueOf(plant.flowerTypeName)
                        }.getOrDefault(FlowerType.SUNFLOWER)

                        FlowerPlantBadge(
                            flowerType = flowerType,
                            verbBase = plant.base,
                            meaningKo = plant.meaningKo,
                            completedCount = plant.timesCompleted,
                            onClick = { onSelectPlant(plant) },
                            modifier = Modifier.testTag("plant_item_${plant.id}")
                        )
                    }
                }
            }
        }
    }

    // 식물을 선택하면 해당 동사의 원형 - 과거형 - 과거분사형 표시 & 발음 듣기 다이얼로그
    if (selectedPlant != null) {
        val flowerType = runCatching {
            FlowerType.valueOf(selectedPlant.flowerTypeName)
        }.getOrDefault(FlowerType.SUNFLOWER)

        AlertDialog(
            onDismissRequest = { onSelectPlant(null) },
            confirmButton = {
                Button(
                    onClick = {
                        val base = selectedPlant.base
                        onSelectPlant(null)
                        onPracticeVerb(base)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("practice_verb_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "복습하기")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { onSelectPlant(null) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "닫기")
                }
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFlorist,
                        contentDescription = null,
                        tint = flowerType.primaryColor
                    )
                    Text(
                        text = "${selectedPlant.base} (${selectedPlant.meaningKo})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1B5E20)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "${flowerType.koreanName} (${flowerType.englishName}) · 학습 완료 ${selectedPlant.timesCompleted}회",
                        fontSize = 12.sp,
                        color = Color(0xFF558B2F),
                        fontWeight = FontWeight.SemiBold
                    )

                    // 초등 5~6학년 과학 교과 연계 정보 카드
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF1F8E9),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "🌱 ${flowerType.scienceCurriculum}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = flowerType.scienceDetail,
                                fontSize = 11.sp,
                                color = Color(0xFF388E3C),
                                lineHeight = 14.sp
                            )
                        }
                    }

                    // 3단 변화 카드 (원형, 과거형, 과거분사형)
                    VerbFormCard(
                        title = "원형 (Base Form)",
                        word = selectedPlant.base,
                        tagColor = Color(0xFF2E7D32),
                        onSpeak = { onSpeakWord(selectedPlant.base) }
                    )

                    VerbFormCard(
                        title = "과거형 (Past Form)",
                        word = selectedPlant.past,
                        tagColor = Color(0xFFE65100),
                        onSpeak = { onSpeakWord(selectedPlant.past) }
                    )

                    VerbFormCard(
                        title = "과거분사형 (Past Participle)",
                        word = selectedPlant.pastParticiple,
                        tagColor = Color(0xFFC2185B),
                        onSpeak = { onSpeakWord(selectedPlant.pastParticiple) }
                    )
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}

@Composable
private fun VerbFormCard(
    title: String,
    word: String,
    tagColor: Color,
    onSpeak: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF1F8E9),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = tagColor
                )
                Text(
                    text = word,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            }

            IconButton(
                onClick = onSpeak,
                modifier = Modifier.testTag("speak_button_$word")
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "발음 듣기",
                    tint = tagColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
