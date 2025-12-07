package com.veriyapilari.app

import java.util.*

/**
 * Graph algoritmaları koleksiyonu
 */
object GraphAlgorithms {

    data class AlgorithmStep(
        val description: String,
        val visitedNodes: Set<Int> = emptySet(),
        val activeEdges: Set<Pair<Int, Int>> = emptySet(),
        val distances: Map<Int, Int> = emptyMap(),
        val parents: Map<Int, Int> = emptyMap()
    )

    /**
     * BFS (Breadth-First Search) - Genişlik Öncelikli Arama
     */
    fun bfs(graph: Graph, startNode: Int): List<AlgorithmStep> {
        val steps = mutableListOf<AlgorithmStep>()
        val visited = mutableSetOf<Int>()
        val queue: Queue<Int> = LinkedList()
        val activeEdges = mutableSetOf<Pair<Int, Int>>()

        steps.add(AlgorithmStep("🎯 BFS Algoritması Başlatılıyor"))
        steps.add(AlgorithmStep("Başlangıç düğümü: $startNode"))
        steps.add(AlgorithmStep(""))

        queue.offer(startNode)
        visited.add(startNode)

        steps.add(AlgorithmStep(
            "Queue'ya eklendi: $startNode",
            visitedNodes = visited.toSet()
        ))

        var stepCount = 1

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            steps.add(AlgorithmStep(""))
            steps.add(AlgorithmStep("--- Adım $stepCount ---"))
            steps.add(AlgorithmStep(
                "Queue'dan çıkarıldı: $current",
                visitedNodes = visited.toSet(),
                activeEdges = activeEdges.toSet()
            ))

            val neighbors = graph.getNeighbors(current)
            steps.add(AlgorithmStep(
                "Komşular: ${neighbors.map { it.to }.joinToString(", ")}",
                visitedNodes = visited.toSet(),
                activeEdges = activeEdges.toSet()
            ))

            for (edge in neighbors) {
                val neighbor = edge.to
                if (neighbor !in visited) {
                    visited.add(neighbor)
                    queue.offer(neighbor)
                    activeEdges.add(Pair(current, neighbor))

                    steps.add(AlgorithmStep(
                        "✓ Yeni düğüm keşfedildi: $neighbor (Queue'ya eklendi)",
                        visitedNodes = visited.toSet(),
                        activeEdges = activeEdges.toSet()
                    ))
                } else {
                    steps.add(AlgorithmStep(
                        "× Düğüm $neighbor zaten ziyaret edilmiş",
                        visitedNodes = visited.toSet(),
                        activeEdges = activeEdges.toSet()
                    ))
                }
            }

            steps.add(AlgorithmStep(
                "Queue durumu: ${queue.toList()}",
                visitedNodes = visited.toSet(),
                activeEdges = activeEdges.toSet()
            ))

            stepCount++
        }

        steps.add(AlgorithmStep(""))
        steps.add(AlgorithmStep(
            "✅ BFS Tamamlandı!",
            visitedNodes = visited.toSet(),
            activeEdges = activeEdges.toSet()
        ))
        steps.add(AlgorithmStep(
            "Ziyaret edilen düğümler: ${visited.sorted()}",
            visitedNodes = visited.toSet(),
            activeEdges = activeEdges.toSet()
        ))

        return steps
    }

    /**
     * DFS (Depth-First Search) - Derinlik Öncelikli Arama
     */
    fun dfs(graph: Graph, startNode: Int): List<AlgorithmStep> {
        val steps = mutableListOf<AlgorithmStep>()
        val visited = mutableSetOf<Int>()
        val activeEdges = mutableSetOf<Pair<Int, Int>>()

        steps.add(AlgorithmStep("🎯 DFS Algoritması Başlatılıyor"))
        steps.add(AlgorithmStep("Başlangıç düğümü: $startNode"))
        steps.add(AlgorithmStep(""))

        var stepCount = 1

        fun dfsRecursive(node: Int, depth: Int = 0) {
            visited.add(node)
            val indent = "  ".repeat(depth)

            steps.add(AlgorithmStep(""))
            steps.add(AlgorithmStep("--- Adım $stepCount ---"))
            steps.add(AlgorithmStep(
                "${indent}Ziyaret ediliyor: $node (Derinlik: $depth)",
                visitedNodes = visited.toSet(),
                activeEdges = activeEdges.toSet()
            ))

            val neighbors = graph.getNeighbors(node)
            steps.add(AlgorithmStep(
                "${indent}Komşular: ${neighbors.map { it.to }.joinToString(", ")}",
                visitedNodes = visited.toSet(),
                activeEdges = activeEdges.toSet()
            ))

            for (edge in neighbors) {
                val neighbor = edge.to
                if (neighbor !in visited) {
                    activeEdges.add(Pair(node, neighbor))
                    steps.add(AlgorithmStep(
                        "${indent}✓ Yeni düğüm keşfedildi: $neighbor (Derinliğe dalınıyor)",
                        visitedNodes = visited.toSet(),
                        activeEdges = activeEdges.toSet()
                    ))
                    stepCount++
                    dfsRecursive(neighbor, depth + 1)
                } else {
                    steps.add(AlgorithmStep(
                        "${indent}× Düğüm $neighbor zaten ziyaret edilmiş",
                        visitedNodes = visited.toSet(),
                        activeEdges = activeEdges.toSet()
                    ))
                }
            }

            steps.add(AlgorithmStep(
                "${indent}↩ Geri dönülüyor: $node",
                visitedNodes = visited.toSet(),
                activeEdges = activeEdges.toSet()
            ))
        }

        dfsRecursive(startNode)

        steps.add(AlgorithmStep(""))
        steps.add(AlgorithmStep(
            "✅ DFS Tamamlandı!",
            visitedNodes = visited.toSet(),
            activeEdges = activeEdges.toSet()
        ))
        steps.add(AlgorithmStep(
            "Ziyaret edilen düğümler: ${visited.sorted()}",
            visitedNodes = visited.toSet(),
            activeEdges = activeEdges.toSet()
        ))

        return steps
    }

    /**
     * Dijkstra - En Kısa Yol Algoritması
     */
    fun dijkstra(graph: Graph, startNode: Int): List<AlgorithmStep> {
        val steps = mutableListOf<AlgorithmStep>()
        val distances = mutableMapOf<Int, Int>()
        val parents = mutableMapOf<Int, Int>()
        val visited = mutableSetOf<Int>()
        val activeEdges = mutableSetOf<Pair<Int, Int>>()

        // Tüm düğümlere sonsuz mesafe ata
        graph.getNodes().forEach { node ->
            distances[node.id] = Int.MAX_VALUE
        }
        distances[startNode] = 0

        steps.add(AlgorithmStep("🎯 Dijkstra Algoritması Başlatılıyor"))
        steps.add(AlgorithmStep("Başlangıç düğümü: $startNode"))
        steps.add(AlgorithmStep(""))
        steps.add(AlgorithmStep("İlk durumlar:"))
        graph.getNodes().forEach { node ->
            val dist = if (node.id == startNode) "0" else "∞"
            steps.add(AlgorithmStep("  Düğüm ${node.id}: mesafe = $dist"))
        }
        steps.add(AlgorithmStep(""))

        val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
        pq.offer(Pair(startNode, 0))

        var stepCount = 1

        while (pq.isNotEmpty()) {
            val (current, currentDist) = pq.poll()

            if (current in visited) continue

            visited.add(current)

            steps.add(AlgorithmStep(""))
            steps.add(AlgorithmStep("--- Adım $stepCount ---"))
            steps.add(AlgorithmStep(
                "En kısa mesafeli düğüm seçildi: $current (mesafe: $currentDist)",
                visitedNodes = visited.toSet(),
                activeEdges = activeEdges.toSet(),
                distances = distances.toMap()
            ))

            for (edge in graph.getNeighbors(current)) {
                val neighbor = edge.to
                val newDist = currentDist + edge.weight

                steps.add(AlgorithmStep(
                    "Komşu kontrol ediliyor: $neighbor",
                    visitedNodes = visited.toSet(),
                    activeEdges = activeEdges.toSet(),
                    distances = distances.toMap()
                ))

                if (newDist < (distances[neighbor] ?: Int.MAX_VALUE)) {
                    distances[neighbor] = newDist
                    parents[neighbor] = current
                    pq.offer(Pair(neighbor, newDist))
                    activeEdges.add(Pair(current, neighbor))

                    steps.add(AlgorithmStep(
                        "✓ Mesafe güncellendi: $current → $neighbor",
                        visitedNodes = visited.toSet(),
                        activeEdges = activeEdges.toSet(),
                        distances = distances.toMap(),
                        parents = parents.toMap()
                    ))
                    steps.add(AlgorithmStep(
                        "  Formül: dist[$current] + weight($current,$neighbor) = $currentDist + ${edge.weight} = $newDist",
                        visitedNodes = visited.toSet(),
                        activeEdges = activeEdges.toSet(),
                        distances = distances.toMap()
                    ))
                } else {
                    steps.add(AlgorithmStep(
                        "× Güncelleme yapılmadı: $newDist ≥ ${distances[neighbor]}",
                        visitedNodes = visited.toSet(),
                        activeEdges = activeEdges.toSet(),
                        distances = distances.toMap()
                    ))
                }
            }

            stepCount++
        }

        steps.add(AlgorithmStep(""))
        steps.add(AlgorithmStep(
            "✅ Dijkstra Tamamlandı!",
            visitedNodes = visited.toSet(),
            activeEdges = activeEdges.toSet(),
            distances = distances.toMap(),
            parents = parents.toMap()
        ))
        steps.add(AlgorithmStep(""))
        steps.add(AlgorithmStep("En kısa mesafeler:"))
        distances.forEach { (node, dist) ->
            val distStr = if (dist == Int.MAX_VALUE) "∞" else dist.toString()
            steps.add(AlgorithmStep(
                "  Düğüm $node: $distStr",
                visitedNodes = visited.toSet(),
                activeEdges = activeEdges.toSet(),
                distances = distances.toMap()
            ))
        }

        return steps
    }

    /**
     * Prim - Minimum Spanning Tree Algoritması
     */
    fun prim(graph: Graph, startNode: Int): List<AlgorithmStep> {
        val steps = mutableListOf<AlgorithmStep>()
        val visited = mutableSetOf<Int>()
        val mstEdges = mutableSetOf<Pair<Int, Int>>()
        var totalWeight = 0

        steps.add(AlgorithmStep("🎯 Prim MST Algoritması Başlatılıyor"))
        steps.add(AlgorithmStep("Başlangıç düğümü: $startNode"))
        steps.add(AlgorithmStep(""))

        val pq = PriorityQueue<Triple<Int, Int, Int>>(compareBy { it.third })

        visited.add(startNode)
        steps.add(AlgorithmStep(
            "Başlangıç düğümü MST'ye eklendi: $startNode",
            visitedNodes = visited.toSet()
        ))

        graph.getNeighbors(startNode).forEach { edge ->
            pq.offer(Triple(startNode, edge.to, edge.weight))
        }

        var stepCount = 1

        while (pq.isNotEmpty() && visited.size < graph.getNodeCount()) {
            val (from, to, weight) = pq.poll()

            if (to in visited) continue

            visited.add(to)
            mstEdges.add(Pair(from, to))
            totalWeight += weight

            steps.add(AlgorithmStep(""))
            steps.add(AlgorithmStep("--- Adım $stepCount ---"))
            steps.add(AlgorithmStep(
                "En düşük ağırlıklı kenar seçildi: $from → $to (ağırlık: $weight)",
                visitedNodes = visited.toSet(),
                activeEdges = mstEdges.toSet()
            ))
            steps.add(AlgorithmStep(
                "Toplam ağırlık: $totalWeight",
                visitedNodes = visited.toSet(),
                activeEdges = mstEdges.toSet()
            ))

            graph.getNeighbors(to).forEach { edge ->
                if (edge.to !in visited) {
                    pq.offer(Triple(to, edge.to, edge.weight))
                }
            }

            stepCount++
        }

        steps.add(AlgorithmStep(""))
        steps.add(AlgorithmStep(
            "✅ Prim MST Tamamlandı!",
            visitedNodes = visited.toSet(),
            activeEdges = mstEdges.toSet()
        ))
        steps.add(AlgorithmStep(
            "MST Toplam Ağırlık: $totalWeight",
            visitedNodes = visited.toSet(),
            activeEdges = mstEdges.toSet()
        ))
        steps.add(AlgorithmStep(
            "MST Kenar sayısı: ${mstEdges.size}",
            visitedNodes = visited.toSet(),
            activeEdges = mstEdges.toSet()
        ))

        return steps
    }

    /**
     * Kruskal - Minimum Spanning Tree Algoritması (Union-Find ile)
     */
    fun kruskal(graph: Graph): List<AlgorithmStep> {
        val steps = mutableListOf<AlgorithmStep>()
        val mstEdges = mutableSetOf<Pair<Int, Int>>()
        var totalWeight = 0

        steps.add(AlgorithmStep("🎯 Kruskal MST Algoritması Başlatılıyor"))
        steps.add(AlgorithmStep(""))

        // Tüm kenarları ağırlığa göre sırala
        val sortedEdges = graph.getEdges().sortedBy { it.weight }

        steps.add(AlgorithmStep("1️⃣ Kenarlar ağırlığa göre sıralandı:"))
        sortedEdges.forEach { edge ->
            steps.add(AlgorithmStep("  ${edge.from} → ${edge.to} (ağırlık: ${edge.weight})"))
        }
        steps.add(AlgorithmStep(""))

        // Union-Find veri yapısı
        val parent = mutableMapOf<Int, Int>()
        val rank = mutableMapOf<Int, Int>()

        fun find(x: Int): Int {
            if (parent[x] != x) {
                parent[x] = find(parent[x]!!)
            }
            return parent[x]!!
        }

        fun union(x: Int, y: Int): Boolean {
            val rootX = find(x)
            val rootY = find(y)

            if (rootX == rootY) return false

            when {
                rank[rootX]!! < rank[rootY]!! -> parent[rootX] = rootY
                rank[rootX]!! > rank[rootY]!! -> parent[rootY] = rootX
                else -> {
                    parent[rootY] = rootX
                    rank[rootX] = rank[rootX]!! + 1
                }
            }
            return true
        }

        // Initialize
        graph.getNodes().forEach { node ->
            parent[node.id] = node.id
            rank[node.id] = 0
        }

        steps.add(AlgorithmStep("2️⃣ Her düğüm kendi kümesinde başlatıldı"))
        steps.add(AlgorithmStep(""))

        var stepCount = 1

        for (edge in sortedEdges) {
            steps.add(AlgorithmStep("--- Adım $stepCount ---"))
            steps.add(AlgorithmStep(
                "Kenar inceleniyor: ${edge.from} → ${edge.to} (ağırlık: ${edge.weight})",
                activeEdges = mstEdges.toSet()
            ))

            val rootFrom = find(edge.from)
            val rootTo = find(edge.to)

            if (rootFrom != rootTo) {
                union(edge.from, edge.to)
                mstEdges.add(Pair(edge.from, edge.to))
                totalWeight += edge.weight

                steps.add(AlgorithmStep(
                    "✓ Kenar MST'ye eklendi (döngü oluşturmaz)",
                    activeEdges = mstEdges.toSet()
                ))
                steps.add(AlgorithmStep(
                    "  Kümeler birleştirildi: $rootFrom ∪ $rootTo",
                    activeEdges = mstEdges.toSet()
                ))
                steps.add(AlgorithmStep(
                    "  Toplam ağırlık: $totalWeight",
                    activeEdges = mstEdges.toSet()
                ))
            } else {
                steps.add(AlgorithmStep(
                    "× Kenar atlandı (döngü oluşturur)",
                    activeEdges = mstEdges.toSet()
                ))
                steps.add(AlgorithmStep(
                    "  ${edge.from} ve ${edge.to} zaten aynı kümede",
                    activeEdges = mstEdges.toSet()
                ))
            }

            steps.add(AlgorithmStep(""))
            stepCount++

            if (mstEdges.size == graph.getNodeCount() - 1) break
        }

        steps.add(AlgorithmStep(
            "✅ Kruskal MST Tamamlandı!",
            activeEdges = mstEdges.toSet()
        ))
        steps.add(AlgorithmStep(
            "MST Toplam Ağırlık: $totalWeight",
            activeEdges = mstEdges.toSet()
        ))
        steps.add(AlgorithmStep(
            "MST Kenar sayısı: ${mstEdges.size}",
            activeEdges = mstEdges.toSet()
        ))

        return steps
    }

    /**
     * Bellman-Ford - En Kısa Yol (Negatif ağırlıkları destekler)
     */
    fun bellmanFord(graph: Graph, startNode: Int): List<AlgorithmStep> {
        val steps = mutableListOf<AlgorithmStep>()
        val distances = mutableMapOf<Int, Int>()
        val parents = mutableMapOf<Int, Int>()
        val activeEdges = mutableSetOf<Pair<Int, Int>>()

        steps.add(AlgorithmStep("🎯 Bellman-Ford Algoritması Başlatılıyor"))
        steps.add(AlgorithmStep("Başlangıç düğümü: $startNode"))
        steps.add(AlgorithmStep(""))

        // Initialize
        graph.getNodes().forEach { node ->
            distances[node.id] = Int.MAX_VALUE
        }
        distances[startNode] = 0

        steps.add(AlgorithmStep("İlk durumlar:"))
        distances.forEach { (node, dist) ->
            val distStr = if (dist == Int.MAX_VALUE) "∞" else dist.toString()
            steps.add(AlgorithmStep("  Düğüm $node: $distStr"))
        }
        steps.add(AlgorithmStep(""))

        val V = graph.getNodeCount()
        val edges = graph.getEdges()

        // V-1 iterasyon
        for (i in 0 until V - 1) {
            steps.add(AlgorithmStep(""))
            steps.add(AlgorithmStep("=== İterasyon ${i + 1} ==="))
            var updated = false

            for (edge in edges) {
                val u = edge.from
                val v = edge.to
                val w = edge.weight

                if (distances[u] != Int.MAX_VALUE && distances[u]!! + w < distances[v]!!) {
                    distances[v] = distances[u]!! + w
                    parents[v] = u
                    activeEdges.add(Pair(u, v))
                    updated = true

                    steps.add(AlgorithmStep(
                        "✓ Mesafe güncellendi: $u → $v",
                        activeEdges = activeEdges.toSet(),
                        distances = distances.toMap()
                    ))
                    steps.add(AlgorithmStep(
                        "  dist[$v] = dist[$u] + weight = ${distances[u]} + $w = ${distances[v]}",
                        activeEdges = activeEdges.toSet(),
                        distances = distances.toMap()
                    ))
                }
            }

            if (!updated) {
                steps.add(AlgorithmStep("Güncelleme yapılmadı, erken sonlandırılıyor"))
                break
            }
        }

        // Negatif döngü kontrolü
        steps.add(AlgorithmStep(""))
        steps.add(AlgorithmStep("Negatif döngü kontrolü yapılıyor..."))
        var hasNegativeCycle = false

        for (edge in edges) {
            val u = edge.from
            val v = edge.to
            val w = edge.weight

            if (distances[u] != Int.MAX_VALUE && distances[u]!! + w < distances[v]!!) {
                hasNegativeCycle = true
                steps.add(AlgorithmStep("❌ Negatif döngü tespit edildi!"))
                break
            }
        }

        if (!hasNegativeCycle) {
            steps.add(AlgorithmStep("✓ Negatif döngü yok"))
        }

        steps.add(AlgorithmStep(""))
        steps.add(AlgorithmStep(
            "✅ Bellman-Ford Tamamlandı!",
            activeEdges = activeEdges.toSet(),
            distances = distances.toMap()
        ))

        if (!hasNegativeCycle) {
            steps.add(AlgorithmStep(""))
            steps.add(AlgorithmStep("En kısa mesafeler:"))
            distances.forEach { (node, dist) ->
                val distStr = if (dist == Int.MAX_VALUE) "∞" else dist.toString()
                steps.add(AlgorithmStep("  Düğüm $node: $distStr"))
            }
        }

        return steps
    }

    /**
     * Floyd-Warshall - Tüm Çiftler Arası En Kısa Yol
     */
    fun floydWarshall(graph: Graph): List<AlgorithmStep> {
        val steps = mutableListOf<AlgorithmStep>()
        val nodes = graph.getNodes().map { it.id }.sorted()
        val n = nodes.size

        steps.add(AlgorithmStep("🎯 Floyd-Warshall Algoritması Başlatılıyor"))
        steps.add(AlgorithmStep("Tüm düğüm çiftleri arası en kısa yolları bulur"))
        steps.add(AlgorithmStep(""))

        // Distance matrix initialize
        val dist = Array(n) { IntArray(n) { Int.MAX_VALUE / 2 } }
        val next = Array(n) { IntArray(n) { -1 } }

        // Kendine mesafe 0
        for (i in 0 until n) {
            dist[i][i] = 0
        }

        // Kenarları ekle
        graph.getEdges().forEach { edge ->
            val i = nodes.indexOf(edge.from)
            val j = nodes.indexOf(edge.to)
            dist[i][j] = edge.weight
            next[i][j] = j
        }

        steps.add(AlgorithmStep("İlk mesafe matrisi oluşturuldu"))
        steps.add(AlgorithmStep(""))

        // Floyd-Warshall algoritması
        for (k in 0 until n) {
            steps.add(AlgorithmStep(""))
            steps.add(AlgorithmStep("=== Ara düğüm: ${nodes[k]} ==="))

            for (i in 0 until n) {
                for (j in 0 until n) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        val oldDist = dist[i][j]
                        dist[i][j] = dist[i][k] + dist[k][j]
                        next[i][j] = next[i][k]

                        val oldStr = if (oldDist >= Int.MAX_VALUE / 2) "∞" else oldDist.toString()
                        steps.add(AlgorithmStep(
                            "✓ Güncellendi: ${nodes[i]} → ${nodes[j]} = ${dist[i][j]} (önceki: $oldStr)"
                        ))
                        steps.add(AlgorithmStep(
                            "  Yol: ${nodes[i]} → ${nodes[k]} → ${nodes[j]}"
                        ))
                    }
                }
            }
        }

        steps.add(AlgorithmStep(""))
        steps.add(AlgorithmStep("✅ Floyd-Warshall Tamamlandı!"))
        steps.add(AlgorithmStep(""))
        steps.add(AlgorithmStep("Son mesafe matrisi:"))

        // Sonuç matrisi
        val header = "     " + nodes.joinToString("   ") { it.toString().padStart(3) }
        steps.add(AlgorithmStep(header))

        for (i in 0 until n) {
            val row = buildString {
                append(nodes[i].toString().padStart(3))
                append("  ")
                for (j in 0 until n) {
                    val distStr = if (dist[i][j] >= Int.MAX_VALUE / 2) "∞" else dist[i][j].toString()
                    append(distStr.padStart(3))
                    append("  ")
                }
            }
            steps.add(AlgorithmStep(row))
        }

        return steps
    }
}
