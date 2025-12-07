package com.veriyapilari.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun HashCanvas(table: Array<Int?>, deleted: BooleanArray, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val errorContainerColor = MaterialTheme.colorScheme.errorContainer
    val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val outlineColor = MaterialTheme.colorScheme.outline
    val onSurfaceVariantColor = MaterialTheme.colorScheme.onSurfaceVariant
    val onPrimaryContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
    val onErrorContainerColor = MaterialTheme.colorScheme.onErrorContainer
    val cellWidth = 80f
    val cellHeight = 60f
    val spacing = 10f

    Box(modifier = modifier.fillMaxSize().background(surfaceColor)) {
        Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            val startX = (size.width - (cellWidth + spacing) * table.size) / 2
            val startY = size.height / 2 - cellHeight / 2

            table.forEachIndexed { index, value ->
                val x = startX + index * (cellWidth + spacing)
                val y = startY

                // İndeksi kutunun üstünde göster
                val indexLayout = textMeasurer.measure(index.toString())
                drawText(
                    indexLayout,
                    color = onSurfaceVariantColor,
                    topLeft = Offset(
                        x + cellWidth / 2 - indexLayout.size.width / 2,
                        y - indexLayout.size.height - 5f
                    )
                )

                val cellColor = when {
                    deleted[index] -> errorContainerColor
                    value != null -> primaryContainerColor
                    else -> surfaceVariantColor
                }

                drawRect(
                    color = cellColor,
                    topLeft = Offset(x, y),
                    size = Size(cellWidth, cellHeight)
                )

                drawRect(
                    color = outlineColor,
                    topLeft = Offset(x, y),
                    size = Size(cellWidth, cellHeight),
                    style = Stroke(width = 2f)
                )

                if (value != null) {
                    val valueLayout = textMeasurer.measure(value.toString())
                    drawText(
                        valueLayout,
                        color = onPrimaryContainerColor,
                        topLeft = Offset(
                            x + cellWidth / 2 - valueLayout.size.width / 2,
                            y + cellHeight / 2 - valueLayout.size.height / 2
                        )
                    )
                } else if (deleted[index]) {
                    val deletedLayout = textMeasurer.measure("DEL")
                    drawText(
                        deletedLayout,
                        color = onErrorContainerColor,
                        topLeft = Offset(
                            x + cellWidth / 2 - deletedLayout.size.width / 2,
                            y + cellHeight / 2 - deletedLayout.size.height / 2
                        )
                    )
                }
            }
        }
    }
}
