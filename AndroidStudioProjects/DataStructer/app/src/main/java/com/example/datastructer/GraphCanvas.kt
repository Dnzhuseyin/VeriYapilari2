package com.example.datastructer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GraphCanvas(
    graph: Graph,
    currentStep: GraphAlgorithms.AlgorithmStep?,
    zoomLevel: Float,
    offsetX: Float,
    offsetY: Float,
    onDrag: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        if (graph.getNodeCount() == 0) {
            Text(
                text = "Graf oluşturmak için 'Örnek Graf' butonuna tıklayın",
                style = MaterialTheme.typography.bodyLarge,
                color = onSurfaceColor,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            onDrag(dragAmount.x, dragAmount.y)
                        }
                    }
            ) {
                clipRect {
                    val nodes = graph.getNodes()
                    val edges = graph.getEdges()
                    val visitedNodes = currentStep?.visitedNodes ?: emptySet()
                    val activeEdges = currentStep?.activeEdges ?: emptySet()
                    val distances = currentStep?.distances ?: emptyMap()

                    val radius = 50f * zoomLevel

                    // Kenarları çiz
                    edges.forEach { edge ->
                    val fromNode = nodes.find { it.id == edge.from }
                    val toNode = nodes.find { it.id == edge.to }

                    if (fromNode != null && toNode != null) {
                        val startX = fromNode.x * zoomLevel + offsetX
                        val startY = fromNode.y * zoomLevel + offsetY
                        val endX = toNode.x * zoomLevel + offsetX
                        val endY = toNode.y * zoomLevel + offsetY

                        val isActive = activeEdges.contains(Pair(edge.from, edge.to))
                        val edgeColor = if (isActive) Color(0xFF3B82F6) else Color.Gray.copy(alpha = 0.4f)
                        val strokeWidth = if (isActive) 5f * zoomLevel else 2.5f * zoomLevel

                        // Çizgiyi düğüm kenarlarından başlat/bitir
                        val angle = atan2((endY - startY).toDouble(), (endX - startX).toDouble())
                        val adjustedStartX = startX + (radius * cos(angle)).toFloat()
                        val adjustedStartY = startY + (radius * sin(angle)).toFloat()
                        val adjustedEndX = endX - (radius * cos(angle)).toFloat()
                        val adjustedEndY = endY - (radius * sin(angle)).toFloat()

                        drawLine(
                            color = edgeColor,
                            start = Offset(adjustedStartX, adjustedStartY),
                            end = Offset(adjustedEndX, adjustedEndY),
                            strokeWidth = strokeWidth
                        )

                        // Yönlü graf için ok başı
                        if (graph.isDirected) {
                            val arrowSize = 12f * zoomLevel
                            val arrowAngle = 0.5

                            val arrowX1 = adjustedEndX - arrowSize * cos(angle - arrowAngle).toFloat()
                            val arrowY1 = adjustedEndY - arrowSize * sin(angle - arrowAngle).toFloat()
                            val arrowX2 = adjustedEndX - arrowSize * cos(angle + arrowAngle).toFloat()
                            val arrowY2 = adjustedEndY - arrowSize * sin(angle + arrowAngle).toFloat()

                            drawLine(edgeColor, Offset(adjustedEndX, adjustedEndY), Offset(arrowX1, arrowY1), strokeWidth)
                            drawLine(edgeColor, Offset(adjustedEndX, adjustedEndY), Offset(arrowX2, arrowY2), strokeWidth)
                        }

                        // Ağırlık etiketi
                        if (edge.weight > 1) {
                            val midX = (startX + endX) / 2
                            val midY = (startY + endY) / 2

                            val weightLayout = textMeasurer.measure(
                                edge.weight.toString(),
                                style = TextStyle(
                                    fontSize = (14 * zoomLevel).sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            val badgeRadius = 20f * zoomLevel

                            drawCircle(
                                color = surfaceColor,
                                radius = badgeRadius,
                                center = Offset(midX, midY)
                            )

                            drawCircle(
                                color = if (isActive) Color(0xFF3B82F6) else Color.Gray,
                                radius = badgeRadius,
                                center = Offset(midX, midY),
                                style = Stroke(width = 2f * zoomLevel)
                            )

                            drawText(
                                weightLayout,
                                color = if (isActive) Color(0xFF3B82F6) else Color.Gray,
                                topLeft = Offset(
                                    midX - weightLayout.size.width / 2,
                                    midY - weightLayout.size.height / 2
                                )
                            )
                        }
                    }
                }

                // Düğümleri çiz
                nodes.forEach { node ->
                    val x = node.x * zoomLevel + offsetX
                    val y = node.y * zoomLevel + offsetY

                    val isVisited = visitedNodes.contains(node.id)
                    val nodeColor = if (isVisited) {
                        Color(0xFF10B981) // Yeşil
                    } else {
                        Color(0xFF6B7280) // Gri
                    }

                    // Shadow
                    drawCircle(
                        color = nodeColor.copy(alpha = 0.3f),
                        radius = radius + 3f * zoomLevel,
                        center = Offset(x, y)
                    )

                    // Ana daire
                    drawCircle(
                        color = nodeColor,
                        radius = radius,
                        center = Offset(x, y)
                    )

                    // Border
                    drawCircle(
                        color = Color.White,
                        radius = radius,
                        center = Offset(x, y),
                        style = Stroke(width = 3f * zoomLevel)
                    )

                    // Düğüm ID
                    val idLayout = textMeasurer.measure(
                        node.id.toString(),
                        style = TextStyle(fontSize = (24 * zoomLevel).sp, fontWeight = FontWeight.Bold, color = Color.White)
                    )
                    drawText(
                        idLayout,
                        topLeft = Offset(
                            x - idLayout.size.width / 2,
                            y - idLayout.size.height / 2
                        )
                    )

                    // Mesafe düğümün altında (Dijkstra, Bellman-Ford için)
                    if (distances.containsKey(node.id)) {
                        val dist = distances[node.id]
                        val distStr = if (dist == Int.MAX_VALUE) "∞" else dist.toString()

                        val distLayout = textMeasurer.measure(
                            distStr,
                            style = TextStyle(
                                fontSize = (16 * zoomLevel).sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )

                        val badgeRadius = 20f * zoomLevel
                        val badgeY = y + radius + 26f * zoomLevel

                        drawCircle(
                            color = Color(0xFFEF4444),
                            radius = badgeRadius,
                            center = Offset(x, badgeY)
                        )

                        drawCircle(
                            color = Color.White,
                            radius = badgeRadius,
                            center = Offset(x, badgeY),
                            style = Stroke(width = 2f * zoomLevel)
                        )

                        drawText(
                            distLayout,
                            topLeft = Offset(
                                x - distLayout.size.width / 2,
                                badgeY - distLayout.size.height / 2
                            )
                        )
                    }
                }
                }
            }
        }
    }
}
