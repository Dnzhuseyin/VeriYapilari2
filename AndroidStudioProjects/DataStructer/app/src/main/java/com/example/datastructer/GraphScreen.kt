package com.example.datastructer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

enum class GraphAlgorithmType {
    BFS, DFS, DIJKSTRA, PRIM, KRUSKAL, BELLMAN_FORD, FLOYD_WARSHALL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: GraphViewModel = viewModel()
    var selectedAlgorithm by remember { mutableStateOf(GraphAlgorithmType.BFS) }
    var startNode by remember { mutableStateOf("0") }
    var fromNode by remember { mutableStateOf("") }
    var toNode by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("1") }
    var showExplanations by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.createSampleGraph()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Graph Algoritmaları") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Geri")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.zoomIn() }) {
                        Icon(Icons.Default.ZoomIn, "Yakınlaştır")
                    }
                    IconButton(onClick = { viewModel.zoomOut() }) {
                        Icon(Icons.Default.ZoomOut, "Uzaklaştır")
                    }
                    IconButton(onClick = { viewModel.resetZoom() }) {
                        Icon(Icons.Default.Refresh, "Sıfırla")
                    }
                    IconButton(onClick = { viewModel.clearGraph() }) {
                        Icon(Icons.Default.Delete, "Temizle")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Üst kontrol paneli
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Algoritma seçimi
                    Text(
                        "Algoritma Seçin",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf(
                                GraphAlgorithmType.BFS,
                                GraphAlgorithmType.DFS,
                                GraphAlgorithmType.DIJKSTRA,
                                GraphAlgorithmType.PRIM
                            ).forEach { algo ->
                                FilterChip(
                                    selected = selectedAlgorithm == algo,
                                    onClick = { selectedAlgorithm = algo },
                                    label = {
                                        Text(
                                            when (algo) {
                                                GraphAlgorithmType.BFS -> "BFS"
                                                GraphAlgorithmType.DFS -> "DFS"
                                                GraphAlgorithmType.DIJKSTRA -> "Dijkstra"
                                                GraphAlgorithmType.PRIM -> "Prim"
                                                else -> ""
                                            }
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf(
                                GraphAlgorithmType.KRUSKAL,
                                GraphAlgorithmType.BELLMAN_FORD,
                                GraphAlgorithmType.FLOYD_WARSHALL
                            ).forEach { algo ->
                                FilterChip(
                                    selected = selectedAlgorithm == algo,
                                    onClick = { selectedAlgorithm = algo },
                                    label = {
                                        Text(
                                            when (algo) {
                                                GraphAlgorithmType.KRUSKAL -> "Kruskal"
                                                GraphAlgorithmType.BELLMAN_FORD -> "Bellman-Ford"
                                                GraphAlgorithmType.FLOYD_WARSHALL -> "Floyd-Warshall"
                                                else -> ""
                                            }
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Kontroller
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Graf işlemleri
                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { viewModel.createSampleGraph() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Örnek Graf")
                            }

                            if (selectedAlgorithm != GraphAlgorithmType.KRUSKAL &&
                                selectedAlgorithm != GraphAlgorithmType.FLOYD_WARSHALL
                            ) {
                                Spacer(Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = startNode,
                                    onValueChange = { startNode = it },
                                    label = { Text("Başlangıç") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            }

                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    val start = startNode.toIntOrNull() ?: 0
                                    when (selectedAlgorithm) {
                                        GraphAlgorithmType.BFS -> viewModel.runBFS(start)
                                        GraphAlgorithmType.DFS -> viewModel.runDFS(start)
                                        GraphAlgorithmType.DIJKSTRA -> viewModel.runDijkstra(start)
                                        GraphAlgorithmType.PRIM -> viewModel.runPrim(start)
                                        GraphAlgorithmType.KRUSKAL -> viewModel.runKruskal()
                                        GraphAlgorithmType.BELLMAN_FORD -> viewModel.runBellmanFord(start)
                                        GraphAlgorithmType.FLOYD_WARSHALL -> viewModel.runFloydWarshall()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Başlat")
                            }
                        }

                        // Kenar ekleme
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Kenar Ekle", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                            Spacer(Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Baş.", style = MaterialTheme.typography.labelSmall)
                                    OutlinedTextField(
                                        value = fromNode,
                                        onValueChange = { fromNode = it },
                                        placeholder = { Text("0", style = MaterialTheme.typography.bodySmall) },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        textStyle = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Bit.", style = MaterialTheme.typography.labelSmall)
                                    OutlinedTextField(
                                        value = toNode,
                                        onValueChange = { toNode = it },
                                        placeholder = { Text("1", style = MaterialTheme.typography.bodySmall) },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        textStyle = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Ağır.", style = MaterialTheme.typography.labelSmall)
                                    OutlinedTextField(
                                        value = weight,
                                        onValueChange = { weight = it },
                                        placeholder = { Text("1", style = MaterialTheme.typography.bodySmall) },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        textStyle = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    val from = fromNode.toIntOrNull()
                                    val to = toNode.toIntOrNull()
                                    val w = weight.toIntOrNull()
                                    if (from != null && to != null && w != null) {
                                        viewModel.addEdge(from, to, w)
                                        fromNode = ""
                                        toNode = ""
                                        weight = "1"
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Ekle")
                            }
                        }
                    }

                    // Adım kontrolleri
                    if (viewModel.allSteps.isNotEmpty()) {
                        Spacer(Modifier.height(12.dp))

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { viewModel.goToStep(0) },
                                    enabled = viewModel.currentStepIndex > 0
                                ) {
                                    Icon(Icons.Default.SkipPrevious, "İlk")
                                }

                                IconButton(
                                    onClick = { viewModel.previousStep() },
                                    enabled = viewModel.currentStepIndex > 0
                                ) {
                                    Icon(Icons.Default.NavigateBefore, "Önceki")
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Adım: ${viewModel.currentStepIndex + 1} / ${viewModel.allSteps.size}",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Slider(
                                        value = viewModel.currentStepIndex.toFloat(),
                                        onValueChange = { viewModel.goToStep(it.toInt()) },
                                        valueRange = 0f..(viewModel.allSteps.size - 1).toFloat(),
                                        steps = viewModel.allSteps.size - 2,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.nextStep() },
                                    enabled = viewModel.currentStepIndex < viewModel.allSteps.size - 1
                                ) {
                                    Icon(Icons.Default.NavigateNext, "Sonraki")
                                }

                                IconButton(
                                    onClick = { viewModel.goToStep(viewModel.allSteps.size - 1) },
                                    enabled = viewModel.currentStepIndex < viewModel.allSteps.size - 1
                                ) {
                                    Icon(Icons.Default.SkipNext, "Son")
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Açıklamalar", fontWeight = FontWeight.Bold)
                            Switch(
                                checked = showExplanations,
                                onCheckedChange = { showExplanations = it }
                            )
                        }

                        AnimatedVisibility(visible = showExplanations) {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .heightIn(max = 150.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    viewModel.allSteps.take(viewModel.currentStepIndex + 1).forEach { step ->
                                        Text(
                                            step.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Canvas alanı
            GraphCanvas(
                graph = viewModel.getGraph(),
                currentStep = viewModel.currentStep,
                zoomLevel = viewModel.zoomLevel,
                offsetX = viewModel.offsetX,
                offsetY = viewModel.offsetY,
                onDrag = { dx, dy -> viewModel.updateOffset(dx, dy) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }
    }
}
