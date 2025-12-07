package com.veriyapilari.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Graph algoritmaları görselleştirme ViewModel
 */
class GraphViewModel : ViewModel() {

    private var graph = Graph(isDirected = false)

    var currentStep: GraphAlgorithms.AlgorithmStep? by mutableStateOf(null)
        private set

    var allSteps: List<GraphAlgorithms.AlgorithmStep> by mutableStateOf(emptyList())
        private set

    var currentStepIndex: Int by mutableStateOf(0)
        private set

    var zoomLevel: Float by mutableStateOf(1.0f)
        private set

    var offsetX: Float by mutableStateOf(400f)
        private set

    var offsetY: Float by mutableStateOf(300f)
        private set

    var isDirected: Boolean by mutableStateOf(false)
        private set

    /**
     * Graf oluşturur (örnek)
     */
    fun createSampleGraph() {
        // Yeni graf oluştur
        graph = Graph(isDirected = isDirected)

        // Örnek graf
        graph.addEdge(0, 1, 4)
        graph.addEdge(0, 2, 3)
        graph.addEdge(1, 2, 1)
        graph.addEdge(1, 3, 2)
        graph.addEdge(2, 3, 4)
        graph.addEdge(3, 4, 2)
        graph.addEdge(4, 5, 6)

        graph.autoLayoutCircular(centerX = 0f, centerY = 0f, radius = 200f)

        // Offseti sıfırla
        offsetX = 400f
        offsetY = 300f
    }

    /**
     * Kenar ekle
     */
    fun addEdge(from: Int, to: Int, weight: Int = 1) {
        graph.addEdge(from, to, weight)
        graph.autoLayoutCircular()
    }

    /**
     * Grafı temizle
     */
    fun clearGraph() {
        graph = Graph(isDirected = isDirected)
        allSteps = emptyList()
        currentStep = null
        currentStepIndex = 0
    }

    /**
     * BFS çalıştır
     */
    fun runBFS(startNode: Int) {
        allSteps = GraphAlgorithms.bfs(graph, startNode)
        currentStepIndex = 0
        currentStep = if (allSteps.isNotEmpty()) allSteps[0] else null
    }

    /**
     * DFS çalıştır
     */
    fun runDFS(startNode: Int) {
        allSteps = GraphAlgorithms.dfs(graph, startNode)
        currentStepIndex = 0
        currentStep = if (allSteps.isNotEmpty()) allSteps[0] else null
    }

    /**
     * Dijkstra çalıştır
     */
    fun runDijkstra(startNode: Int) {
        allSteps = GraphAlgorithms.dijkstra(graph, startNode)
        currentStepIndex = 0
        currentStep = if (allSteps.isNotEmpty()) allSteps[0] else null
    }

    /**
     * Prim çalıştır
     */
    fun runPrim(startNode: Int) {
        allSteps = GraphAlgorithms.prim(graph, startNode)
        currentStepIndex = 0
        currentStep = if (allSteps.isNotEmpty()) allSteps[0] else null
    }

    /**
     * Kruskal çalıştır
     */
    fun runKruskal() {
        allSteps = GraphAlgorithms.kruskal(graph)
        currentStepIndex = 0
        currentStep = if (allSteps.isNotEmpty()) allSteps[0] else null
    }

    /**
     * Bellman-Ford çalıştır
     */
    fun runBellmanFord(startNode: Int) {
        allSteps = GraphAlgorithms.bellmanFord(graph, startNode)
        currentStepIndex = 0
        currentStep = if (allSteps.isNotEmpty()) allSteps[0] else null
    }

    /**
     * Floyd-Warshall çalıştır
     */
    fun runFloydWarshall() {
        allSteps = GraphAlgorithms.floydWarshall(graph)
        currentStepIndex = 0
        currentStep = if (allSteps.isNotEmpty()) allSteps[0] else null
    }

    /**
     * Sonraki adıma geç
     */
    fun nextStep() {
        if (currentStepIndex < allSteps.size - 1) {
            currentStepIndex++
            currentStep = allSteps[currentStepIndex]
        }
    }

    /**
     * Önceki adıma geç
     */
    fun previousStep() {
        if (currentStepIndex > 0) {
            currentStepIndex--
            currentStep = allSteps[currentStepIndex]
        }
    }

    /**
     * Belirli adıma git
     */
    fun goToStep(index: Int) {
        if (index in allSteps.indices) {
            currentStepIndex = index
            currentStep = allSteps[currentStepIndex]
        }
    }

    fun getGraph(): Graph = graph

    fun zoomIn() {
        zoomLevel = (zoomLevel * 1.2f).coerceAtMost(3.0f)
    }

    fun zoomOut() {
        zoomLevel = (zoomLevel / 1.2f).coerceAtLeast(0.5f)
    }

    fun resetZoom() {
        zoomLevel = 1.0f
        offsetX = 200f
        offsetY = 200f
    }

    fun updateOffset(deltaX: Float, deltaY: Float) {
        offsetX += deltaX
        offsetY += deltaY
    }
}
