package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FlowerType
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FlowerPlantBadge(
    flowerType: FlowerType,
    verbBase: String,
    meaningKo: String,
    completedCount: Int = 1,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Cute Flower Miniature
            Box(
                modifier = Modifier.size(72.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(68.dp)) {
                    val w = size.width
                    val h = size.height

                    // Pot
                    val potTopY = h * 0.72f
                    val potW = w * 0.44f
                    drawRoundRect(
                        color = Color(0xFF8D6E63),
                        topLeft = Offset((w - potW) / 2, potTopY),
                        size = Size(potW, h * 0.22f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )

                    // Stem
                    val flowerCenterY = h * 0.36f
                    drawLine(
                        color = Color(0xFF388E3C),
                        start = Offset(w / 2, potTopY),
                        end = Offset(w / 2, flowerCenterY + 4f),
                        strokeWidth = 4f
                    )

                    // Leaves
                    drawCircle(
                        color = Color(0xFF4CAF50),
                        radius = 6f,
                        center = Offset(w / 2 - 8f, (potTopY + flowerCenterY) / 2)
                    )
                    drawCircle(
                        color = Color(0xFF66BB6A),
                        radius = 6f,
                        center = Offset(w / 2 + 8f, (potTopY + flowerCenterY) / 2 - 4f)
                    )

                    // Petals
                    val petalCount = flowerType.petalCount
                    val radius = 12f
                    for (i in 0 until petalCount) {
                        val angle = (2 * Math.PI * i / petalCount).toFloat()
                        val px = w / 2 + cos(angle) * radius
                        val py = flowerCenterY + sin(angle) * radius
                        drawCircle(
                            color = flowerType.primaryColor,
                            radius = 6.5f,
                            center = Offset(px, py)
                        )
                    }

                    // Center
                    drawCircle(
                        color = flowerType.centerColor,
                        radius = 8f,
                        center = Offset(w / 2, flowerCenterY)
                    )
                    drawCircle(
                        color = Color(0x22000000),
                        radius = 8f,
                        center = Offset(w / 2, flowerCenterY),
                        style = Stroke(width = 1.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = verbBase,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = meaningKo,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            if (completedCount > 1) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "★ x$completedCount",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}
