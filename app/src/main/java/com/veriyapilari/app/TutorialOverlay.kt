package com.veriyapilari.app

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

enum class HighlightPosition {
    TOP_LEFT, TOP_CENTER, TOP_RIGHT,
    CENTER_LEFT, CENTER, CENTER_RIGHT,
    BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
}

data class TutorialStep(
    val title: String,
    val description: String,
    val highlightArea: Rect? = null, // Vurgulanacak alan
    val arrowPosition: Offset? = null, // Ok işareti konumu
    val targetElementId: String? = null, // Hedef UI element ID
    val calloutPosition: HighlightPosition = HighlightPosition.BOTTOM_CENTER // Callout pozisyonu
)

@Composable
fun TutorialOverlay(
    steps: List<TutorialStep>,
    onComplete: () -> Unit,
    onSkip: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    val step = steps.getOrNull(currentStep)

    if (step != null) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Semi-transparent overlay
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Dark overlay
                    drawRect(
                        color = Color.Black.copy(alpha = 0.7f),
                        size = size
                    )

                    // Highlight area (cutout)
                    step.highlightArea?.let { rect ->
                        val path = Path().apply {
                            addRect(rect)
                        }
                        drawPath(
                            path = path,
                            color = Color.Transparent,
                            blendMode = BlendMode.Clear
                        )
                        // Draw border around highlighted area
                        drawPath(
                            path = path,
                            color = Color.White,
                            style = Stroke(width = 4f)
                        )
                    }

                    // Arrow pointing to highlighted area
                    step.arrowPosition?.let { pos ->
                        drawCircle(
                            color = Color.White,
                            radius = 12f,
                            center = pos
                        )
                    }
                }

                // Tutorial content card
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Header with step counter
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${currentStep + 1}/${steps.size}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = onSkip) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Skip Tutorial",
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }

                            // Progress indicator
                            LinearProgressIndicator(
                                progress = { (currentStep + 1).toFloat() / steps.size },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            )

                            // Content
                            Text(
                                text = step.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = step.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )

                            // Navigation buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (currentStep > 0) {
                                    OutlinedButton(
                                        onClick = { currentStep-- },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Geri")
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (currentStep < steps.size - 1) {
                                            currentStep++
                                        } else {
                                            onComplete()
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(if (currentStep < steps.size - 1) "Sonraki" else "Başla")
                                    if (currentStep < steps.size - 1) {
                                        Spacer(Modifier.width(8.dp))
                                        Icon(
                                            Icons.Default.ArrowForward,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Tutorial preference manager
object TutorialPreferences {
    private const val PREFS_NAME = "tutorial_prefs"
    private const val KEY_FIRST_LAUNCH = "first_launch"
    private const val KEY_TUTORIAL_COMPLETED = "tutorial_completed_"

    fun isFirstLaunch(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun setFirstLaunchCompleted(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
    }

    fun isTutorialCompleted(context: Context, screenName: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_TUTORIAL_COMPLETED + screenName, false)
    }

    fun setTutorialCompleted(context: Context, screenName: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_TUTORIAL_COMPLETED + screenName, true).apply()
    }

    fun resetAllTutorials(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}

// Welcome tutorial for first launch
@Composable
fun WelcomeTutorial(onComplete: () -> Unit) {
    val steps = listOf(
        TutorialStep(
            title = "Hoş Geldiniz! 👋",
            description = "Veri Yapıları Görselleştirme uygulamasına hoş geldiniz! Bu uygulama ile veri yapılarını interaktif olarak öğrenebilirsiniz."
        ),
        TutorialStep(
            title = "7 Farklı Veri Yapısı 📚",
            description = "Red-Black Tree, AVL Tree, Splay Tree, Max Heap, Hash Table, Huffman Coding ve Graph Algoritmaları ile çalışabilirsiniz."
        ),
        TutorialStep(
            title = "İnteraktif Görselleştirme 🎨",
            description = "Her veri yapısını görsel olarak inceleyebilir, değer ekleyip çıkarabilir, yakınlaştırıp uzaklaştırabilirsiniz."
        ),
        TutorialStep(
            title = "Adım Adım Açıklamalar 💡",
            description = "Her işlem için detaylı açıklamalar göreceksiniz. Böylece algoritmaların nasıl çalıştığını daha iyi anlayacaksınız."
        ),
        TutorialStep(
            title = "Hadi Başlayalım! 🚀",
            description = "Bir veri yapısı seçerek başlayabilirsiniz. Her ekranda size rehberlik edecek ipuçları bulacaksınız."
        )
    )

    TutorialOverlay(
        steps = steps,
        onComplete = onComplete,
        onSkip = onComplete
    )
}
