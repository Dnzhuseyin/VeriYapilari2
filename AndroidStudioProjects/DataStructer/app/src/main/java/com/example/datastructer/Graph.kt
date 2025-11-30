package com.example.datastructer

import java.util.*
import kotlin.math.sqrt

/**
 * Graph temel sınıfı
 * Yönlendirilmiş ve yönsüz grafları destekler
 */
class Graph(val isDirected: Boolean = false) {

    data class Edge(
        val from: Int,
        val to: Int,
        val weight: Int = 1
    ) {
        override fun toString() = if (weight == 1) "$from→$to" else "$from→$to($weight)"
    }

    data class Node(
        val id: Int,
        var x: Float = 0f,
        var y: Float = 0f,
        var label: String = id.toString()
    )

    private val adjacencyList = mutableMapOf<Int, MutableList<Edge>>()
    private val nodes = mutableMapOf<Int, Node>()
    private val edges = mutableListOf<Edge>()

    /**
     * Düğüm ekler
     */
    fun addNode(id: Int, x: Float = 0f, y: Float = 0f) {
        if (!nodes.containsKey(id)) {
            nodes[id] = Node(id, x, y)
            adjacencyList[id] = mutableListOf()
        }
    }

    /**
     * Kenar ekler
     */
    fun addEdge(from: Int, to: Int, weight: Int = 1) {
        addNode(from)
        addNode(to)

        adjacencyList[from]?.add(Edge(from, to, weight))
        edges.add(Edge(from, to, weight))

        if (!isDirected) {
            adjacencyList[to]?.add(Edge(to, from, weight))
        }
    }

    /**
     * Düğüm pozisyonunu ayarlar
     */
    fun setNodePosition(id: Int, x: Float, y: Float) {
        nodes[id]?.apply {
            this.x = x
            this.y = y
        }
    }

    /**
     * Otomatik layout - dairesel yerleşim
     */
    fun autoLayoutCircular(centerX: Float = 400f, centerY: Float = 400f, radius: Float = 250f) {
        val nodeCount = nodes.size
        nodes.values.forEachIndexed { index, node ->
            val angle = 2 * Math.PI * index / nodeCount
            node.x = centerX + (radius * kotlin.math.cos(angle)).toFloat()
            node.y = centerY + (radius * kotlin.math.sin(angle)).toFloat()
        }
    }

    /**
     * Grid layout
     */
    fun autoLayoutGrid(startX: Float = 100f, startY: Float = 100f, spacing: Float = 150f) {
        val cols = kotlin.math.ceil(sqrt(nodes.size.toDouble())).toInt()
        nodes.values.forEachIndexed { index, node ->
            val row = index / cols
            val col = index % cols
            node.x = startX + col * spacing
            node.y = startY + row * spacing
        }
    }

    fun getNodes(): List<Node> = nodes.values.toList()
    fun getEdges(): List<Edge> = edges.toList()
    fun getNeighbors(nodeId: Int): List<Edge> = adjacencyList[nodeId] ?: emptyList()
    fun getNodeCount(): Int = nodes.size
    fun getEdgeCount(): Int = edges.size

    /**
     * Graf bilgilerini döndürür
     */
    fun getInfo(): String {
        return buildString {
            appendLine("Düğüm sayısı: ${nodes.size}")
            appendLine("Kenar sayısı: ${edges.size}")
            appendLine("Yönlü: ${if (isDirected) "Evet" else "Hayır"}")
        }
    }

    fun clear() {
        adjacencyList.clear()
        nodes.clear()
        edges.clear()
    }
}
