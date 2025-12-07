package com.veriyapilari.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.VeriYapilari.ui.theme.DataStructerTheme
import androidx.compose.ui.res.painterResource
import com.example.datastructer.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataStructerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DataStructureApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun DataStructureApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var selectedStructure by remember { mutableStateOf<DataStructureType?>(null) }
    var showWelcomeTutorial by remember {
        mutableStateOf(TutorialPreferences.isFirstLaunch(context))
    }

    // Welcome tutorial for first launch
    if (showWelcomeTutorial) {
        WelcomeTutorial(
            onComplete = {
                TutorialPreferences.setFirstLaunchCompleted(context)
                showWelcomeTutorial = false
            }
        )
    }

    Crossfade(targetState = selectedStructure, label = "screen_crossfade") { screen ->
        when (screen) {
            null -> StructureSelectionScreen(
                onStructureSelected = { selectedStructure = it },
                modifier = modifier
            )
            else -> {
                val onBack = { selectedStructure = null }
                when (screen) {
                    DataStructureType.RED_BLACK -> RedBlackTreeScreen(onBack, modifier)
                    DataStructureType.MAX_HEAP -> HeapScreen(onBack, modifier)
                    DataStructureType.AVL -> AVLScreen(onBack, modifier)
                    DataStructureType.SPLAY -> SplayScreen(onBack, modifier)
                    DataStructureType.HASH_TABLE -> HashTableScreen(onBack, modifier)
                    DataStructureType.HUFFMAN -> HuffmanScreen(onBack, modifier)
                    DataStructureType.GRAPH_ALGORITHMS -> GraphScreen(onBack, modifier)
                }
            }
        }
    }
}

enum class DataStructureType {
    RED_BLACK, MAX_HEAP, AVL, SPLAY, HASH_TABLE, HUFFMAN, GRAPH_ALGORITHMS
}

@Composable
fun StructureSelectionScreen(
    onStructureSelected: (DataStructureType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Modern Header with Gradient
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            // App Logo
            Icon(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "DataStructer Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
                tint = Color.Unspecified
            )

            Text(
                text = "🎯 Veri Yapıları",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Görselleştirme Platformu",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Öğrenmek istediğiniz veri yapısını seçin",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        val structures = listOf(
                Triple(
                    DataStructureType.RED_BLACK,
                    "Red-Black Tree",
                    "    ⬤\n   / \\\n  ⬤   ○\n / \\\n○   ○"
                ),
                Triple(
                    DataStructureType.AVL,
                    "AVL Tree",
                    "    ○\n   / \\\n  ○   ○\n / \\\n○   ○"
                ),
                Triple(
                    DataStructureType.SPLAY,
                    "Splay Tree",
                    "○\n \\\n  ○\n   \\\n    ○"
                ),
                Triple(
                    DataStructureType.MAX_HEAP,
                    "Max Heap",
                    "    9\n   / \\\n  7   5\n / \\\n3   2"
                ),
                Triple(
                    DataStructureType.HASH_TABLE,
                    "Hash Table",
                    "[0] → ○\n[1] → \n[2] → ○→○\n[3] → ○"
                ),
                Triple(
                    DataStructureType.HUFFMAN,
                    "Huffman Coding",
                    "     5\n   /   \\\n  A:2  B:3\n      /  \\\n    C:1  D:2"
                ),
                Triple(
                    DataStructureType.GRAPH_ALGORITHMS,
                    "Graph Algoritmaları",
                    "  ○──○\n  │\\ │\n  │ \\│\n  ○──○"
                )
            )

        // Grid of cards - using Column instead of LazyVerticalGrid for ScrollView
        structures.chunked(2).forEach { rowStructures ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowStructures.forEach { (type, displayName, visual) ->
                    ModernStructureCard(
                        title = displayName,
                        onClick = { onStructureSelected(type) },
                        visualIcon = visual,
                        modifier = Modifier.weight(1f),
                        gradientColors = when (type) {
                            DataStructureType.RED_BLACK -> listOf(
                                Color(0xFFEF4444),
                                Color(0xFFDC2626)
                            )
                            DataStructureType.AVL -> listOf(
                                Color(0xFF667eea),
                                Color(0xFF4A55C8)
                            )
                            DataStructureType.SPLAY -> listOf(
                                Color(0xFF764ba2),
                                Color(0xFF5A3585)
                            )
                            DataStructureType.MAX_HEAP -> listOf(
                                Color(0xFF10B981),
                                Color(0xFF059669)
                            )
                            DataStructureType.HASH_TABLE -> listOf(
                                Color(0xFFf093fb),
                                Color(0xFFd946ef)
                            )
                            DataStructureType.HUFFMAN -> listOf(
                                Color(0xFFf59e0b),
                                Color(0xFFd97706)
                            )
                            DataStructureType.GRAPH_ALGORITHMS -> listOf(
                                Color(0xFF06b6d4),
                                Color(0xFF0891b2)
                            )
                        }
                    )
                }
                // Fill empty space if odd number of items
                if (rowStructures.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ModernStructureCard(
    title: String,
    onClick: () -> Unit,
    gradientColors: List<androidx.compose.ui.graphics.Color>,
    modifier: Modifier = Modifier,
    visualIcon: String = ""
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors.map { it.copy(alpha = 0.15f) }
                    )
                )
        ) {
            // Decorative circles
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = gradientColors.map { it.copy(alpha = 0.2f) }
                        ),
                        shape = RoundedCornerShape(50)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Visual representation
                if (visualIcon.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = gradientColors.first().copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = visualIcon,
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = gradientColors.first(),
                            lineHeight = MaterialTheme.typography.bodySmall.fontSize * 1.2
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun StructureCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun BaseStructureScreen(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 64.dp), // Floating button için yer bırak
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }

        // Floating back button
        Card(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun RedBlackTreeScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TreeViewModel = viewModel()
) {
    var inputValue by remember { mutableStateOf("") }
    var targetPositions by remember { mutableStateOf<Map<String, androidx.compose.ui.geometry.Rect>>(emptyMap()) }

    val updatePosition: (String, androidx.compose.ui.geometry.Rect) -> Unit = { id, rect ->
        targetPositions = targetPositions + (id to rect)
    }

    // Show tutorial on first visit
    ShowScreenTutorial(
        screenName = "red_black_tree",
        targets = ScreenTutorials.getRedBlackTreeTutorial(),
        targetPositions = targetPositions,
        onComplete = { }
    )

    BaseStructureScreen("Red-Black Tree", onBackClick, modifier) {
        ControlPanel(
            inputValue = inputValue,
            onInputValueChange = { inputValue = it },
            onInsertClick = { inputValue.toIntOrNull()?.let { viewModel.insert(it); inputValue = "" } },
            onDeleteClick = { inputValue.toIntOrNull()?.let { viewModel.delete(it); inputValue = "" } },
            onZoomInClick = viewModel::zoomIn,
            onZoomOutClick = viewModel::zoomOut,
            onResetZoomClick = viewModel::resetZoom,
            onClearClick = viewModel::clearTree,
            title = "Red-Black Tree Kontrolü",
            elementIdPrefix = "rbt",
            onPositionChanged = updatePosition
        )

        CanvasCard(
            modifier = Modifier
                .weight(1f)
                .tutorialTarget("rbt_canvas", updatePosition),
            title = "Red-Black Tree Görselleştirme"
        ) {
            TreeCanvas(
                rootNode = viewModel.rootNode,
                zoomLevel = viewModel.zoomLevel,
                offsetX = viewModel.offsetX,
                offsetY = viewModel.offsetY,
                onOffsetChange = viewModel::updateOffset,
                modifier = Modifier.fillMaxSize(),
                key = viewModel.visualizationVersion // Force recomposition on tree changes
            )
        }

        ExplanationPanel(
            explanations = viewModel.explanations,
            modifier = Modifier
                .fillMaxWidth()
                .tutorialTarget("rbt_explanation", updatePosition)
        )
    }
}

@Composable
fun HeapScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HeapViewModel = viewModel()
) {
    var inputValue by remember { mutableStateOf("") }
    var targetPositions by remember { mutableStateOf<Map<String, androidx.compose.ui.geometry.Rect>>(emptyMap()) }

    val updatePosition: (String, androidx.compose.ui.geometry.Rect) -> Unit = { id, rect ->
        targetPositions = targetPositions + (id to rect)
    }

    // Show tutorial on first visit
    ShowScreenTutorial(
        screenName = "max_heap",
        targets = ScreenTutorials.getMaxHeapTutorial(),
        targetPositions = targetPositions,
        onComplete = { }
    )

    BaseStructureScreen("Max Heap", onBackClick, modifier) {
        HeapControlPanel(
            inputValue = inputValue,
            onInputValueChange = { inputValue = it },
            onInsertClick = { inputValue.toIntOrNull()?.let { viewModel.insert(it); inputValue = "" } },
            onExtractMaxClick = viewModel::extractMax,
            onDeleteClick = { inputValue.toIntOrNull()?.let { viewModel.delete(it); inputValue = "" } },
            onZoomInClick = viewModel::zoomIn,
            onZoomOutClick = viewModel::zoomOut,
            onResetZoomClick = viewModel::resetZoom,
            onClearClick = viewModel::clearHeap,
            onPositionChanged = updatePosition
        )

        CanvasCard(
            modifier = Modifier
                .weight(1f)
                .tutorialTarget("heap_canvas", updatePosition),
            title = "Max Heap Görselleştirme"
        ) {
            HeapCanvas(
                rootNode = viewModel.rootNode,
                zoomLevel = viewModel.zoomLevel,
                offsetX = viewModel.offsetX,
                offsetY = viewModel.offsetY,
                onOffsetChange = viewModel::updateOffset,
                modifier = Modifier.fillMaxSize(),
                key = viewModel.visualizationVersion // Force recomposition
            )
        }

        ExplanationPanel(
            explanations = viewModel.explanations,
            modifier = Modifier
                .fillMaxWidth()
                .tutorialTarget("heap_explanation", updatePosition)
        )
    }
}

@Composable
fun AVLScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AVLViewModel = viewModel()
) {
    var inputValue by remember { mutableStateOf("") }
    var targetPositions by remember { mutableStateOf<Map<String, androidx.compose.ui.geometry.Rect>>(emptyMap()) }

    val updatePosition: (String, androidx.compose.ui.geometry.Rect) -> Unit = { id, rect ->
        targetPositions = targetPositions + (id to rect)
    }

    // Show tutorial on first visit
    ShowScreenTutorial(
        screenName = "avl_tree",
        targets = ScreenTutorials.getAVLTreeTutorial(),
        targetPositions = targetPositions,
        onComplete = { }
    )

    BaseStructureScreen("AVL Tree", onBackClick, modifier) {
        ControlPanel(
            inputValue = inputValue,
            onInputValueChange = { inputValue = it },
            onInsertClick = { inputValue.toIntOrNull()?.let { viewModel.insert(it); inputValue = "" } },
            onDeleteClick = { inputValue.toIntOrNull()?.let { viewModel.delete(it); inputValue = "" } },
            onZoomInClick = viewModel::zoomIn,
            onZoomOutClick = viewModel::zoomOut,
            onResetZoomClick = viewModel::resetZoom,
            onClearClick = viewModel::clear,
            title = "AVL Tree Kontrolü",
            elementIdPrefix = "avl",
            onPositionChanged = updatePosition
        )

        CanvasCard(
            modifier = Modifier
                .weight(1f)
                .tutorialTarget("avl_canvas", updatePosition),
            title = "AVL Tree Görselleştirme"
        ) {
            AVLCanvas(
                root = viewModel.rootNode,
                zoom = viewModel.zoomLevel,
                offsetX = viewModel.offsetX,
                offsetY = viewModel.offsetY,
                visualizationVersion = viewModel.visualizationVersion,
                onOffsetChange = viewModel::updateOffset,
                modifier = Modifier.fillMaxSize()
            )
        }

        ExplanationPanel(
            explanations = viewModel.explanations,
            modifier = Modifier
                .fillMaxWidth()
                .tutorialTarget("avl_explanation", updatePosition)
        )
    }
}

@Composable
fun SplayScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplayViewModel = viewModel()
) {
    var inputValue by remember { mutableStateOf("") }
    var targetPositions by remember { mutableStateOf<Map<String, androidx.compose.ui.geometry.Rect>>(emptyMap()) }

    val updatePosition: (String, androidx.compose.ui.geometry.Rect) -> Unit = { id, rect ->
        targetPositions = targetPositions + (id to rect)
    }

    // Show tutorial on first visit
    ShowScreenTutorial(
        screenName = "splay_tree",
        targets = ScreenTutorials.getSplayTreeTutorial(),
        targetPositions = targetPositions,
        onComplete = { }
    )

    BaseStructureScreen("Splay Tree", onBackClick, modifier) {
        ControlPanel(
            inputValue = inputValue,
            onInputValueChange = { inputValue = it },
            onInsertClick = { inputValue.toIntOrNull()?.let { viewModel.insert(it); inputValue = "" } },
            onDeleteClick = { inputValue.toIntOrNull()?.let { viewModel.delete(it); inputValue = "" } },
            onZoomInClick = viewModel::zoomIn,
            onZoomOutClick = viewModel::zoomOut,
            onResetZoomClick = viewModel::resetZoom,
            onClearClick = viewModel::clear,
            title = "Splay Tree Kontrolü",
            elementIdPrefix = "splay",
            onPositionChanged = updatePosition
        )

        CanvasCard(
            modifier = Modifier
                .weight(1f)
                .tutorialTarget("splay_canvas", updatePosition),
            title = "Splay Tree Görselleştirme"
        ) {
            SplayCanvas(
                root = viewModel.rootNode,
                zoom = viewModel.zoomLevel,
                offsetX = viewModel.offsetX,
                offsetY = viewModel.offsetY,
                visualizationVersion = viewModel.visualizationVersion,
                onOffsetChange = viewModel::updateOffset,
                modifier = Modifier.fillMaxSize()
            )
        }

        ExplanationPanel(
            explanations = viewModel.explanations,
            modifier = Modifier
                .fillMaxWidth()
                .tutorialTarget("splay_explanation", updatePosition)
        )
    }
}

@Composable
fun HashTableScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HashViewModel = viewModel()
) {
    var inputValue by remember { mutableStateOf("") }
    var targetPositions by remember { mutableStateOf<Map<String, androidx.compose.ui.geometry.Rect>>(emptyMap()) }

    val updatePosition: (String, androidx.compose.ui.geometry.Rect) -> Unit = { id, rect ->
        targetPositions = targetPositions + (id to rect)
    }

    // Show tutorial on first visit
    ShowScreenTutorial(
        screenName = "hash_table",
        targets = ScreenTutorials.getHashTableTutorial(),
        targetPositions = targetPositions,
        onComplete = { }
    )

    BaseStructureScreen("Hash Table", onBackClick, modifier) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        "🔢 Hash Table Kontrolü",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Collision Method Selection
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.tutorialTarget("hash_collision_method", updatePosition)
                ) {
                    Text(
                        "Çakışma Çözüm Yöntemi:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HashTable.CollisionMethod.entries.forEach { method ->
                            Button(
                                onClick = { viewModel.changeCollisionMethod(method) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (viewModel.collisionMethod == method)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = when (method) {
                                        HashTable.CollisionMethod.LINEAR -> "Linear"
                                        HashTable.CollisionMethod.QUADRATIC -> "Quadratic"
                                        HashTable.CollisionMethod.DOUBLE_HASHING -> "Double"
                                    },
                                    color = if (viewModel.collisionMethod == method)
                                        Color.White
                                    else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (viewModel.collisionMethod == method)
                                        FontWeight.Bold
                                    else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                // Input Section
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { if (it.isEmpty() || it.all { c -> c.isDigit() || c == '-' }) inputValue = it },
                            label = { Text("Değer Girin") },
                            modifier = Modifier
                                .weight(1f)
                                .tutorialTarget("hash_input_field", updatePosition),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Card(
                            modifier = Modifier
                                .size(48.dp)
                                .tutorialTarget("hash_add_button", updatePosition),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (inputValue.isNotBlank())
                                    MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            IconButton(
                                onClick = { inputValue.toIntOrNull()?.let { viewModel.insert(it); inputValue = "" } },
                                enabled = inputValue.isNotBlank(),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(Icons.Default.Add, "Insert",
                                    tint = if (inputValue.isNotBlank())
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                        }
                        Card(
                            modifier = Modifier
                                .size(48.dp)
                                .tutorialTarget("hash_delete_button", updatePosition),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (inputValue.isNotBlank())
                                    MaterialTheme.colorScheme.errorContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            IconButton(
                                onClick = { inputValue.toIntOrNull()?.let { viewModel.delete(it); inputValue = "" } },
                                enabled = inputValue.isNotBlank(),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(Icons.Default.Delete, "Delete",
                                    tint = if (inputValue.isNotBlank())
                                        MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }
                }
            }
        }

        CanvasCard(
            modifier = Modifier
                .weight(1f)
                .tutorialTarget("hash_canvas", updatePosition),
            title = "Hash Table Görselleştirme"
        ) {
            HashCanvas(viewModel.table, viewModel.deleted, Modifier.fillMaxSize())
        }

        ExplanationPanel(
            explanations = viewModel.explanations,
            modifier = Modifier
                .fillMaxWidth()
                .tutorialTarget("hash_explanation", updatePosition)
        )
    }
}

@Composable
fun ControlPanel(
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    onInsertClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onZoomInClick: () -> Unit,
    onZoomOutClick: () -> Unit,
    onResetZoomClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    elementIdPrefix: String = "",
    onPositionChanged: (String, androidx.compose.ui.geometry.Rect) -> Unit = { _, _ -> }
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with gradient background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Input Section
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { if (it.isEmpty() || it.all { c -> c.isDigit() || c == '-' }) onInputValueChange(it) },
                        label = { Text("Değer Girin") },
                        modifier = Modifier
                            .weight(1f)
                            .tutorialTarget(
                                "${elementIdPrefix}_input_field",
                                onPositionChanged
                            ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Card(
                        modifier = Modifier
                            .size(48.dp)
                            .tutorialTarget(
                                "${elementIdPrefix}_add_button",
                                onPositionChanged
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (inputValue.isNotBlank())
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        IconButton(
                            onClick = onInsertClick,
                            enabled = inputValue.isNotBlank(),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(Icons.Default.Add, "Insert",
                                tint = if (inputValue.isNotBlank())
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .size(48.dp)
                            .tutorialTarget(
                                "${elementIdPrefix}_delete_button",
                                onPositionChanged
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (inputValue.isNotBlank())
                                MaterialTheme.colorScheme.errorContainer
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        IconButton(
                            onClick = onDeleteClick,
                            enabled = inputValue.isNotBlank(),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(Icons.Default.Delete, "Delete",
                                tint = if (inputValue.isNotBlank())
                                    MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }

            // Zoom Controls
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onZoomInClick,
                    modifier = Modifier
                        .weight(1f)
                        .tutorialTarget(
                            "${elementIdPrefix}_zoom_in",
                            onPositionChanged
                        ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ZoomIn, "Zoom In", modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Yakınlaştır")
                }
                OutlinedButton(
                    onClick = onZoomOutClick,
                    modifier = Modifier
                        .weight(1f)
                        .tutorialTarget(
                            "${elementIdPrefix}_zoom_out",
                            onPositionChanged
                        ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ZoomOut, "Zoom Out", modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Uzaklaştır")
                }
            }

            // Reset and Clear Controls
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onResetZoomClick,
                    modifier = Modifier
                        .weight(1f)
                        .tutorialTarget(
                            "${elementIdPrefix}_reset_zoom",
                            onPositionChanged
                        ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, "Reset", modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Sıfırla")
                }
                Button(
                    onClick = onClearClick,
                    modifier = Modifier
                        .weight(1f)
                        .tutorialTarget(
                            "${elementIdPrefix}_clear",
                            onPositionChanged
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Delete, "Clear", modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Temizle", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CanvasCard(
    modifier: Modifier = Modifier,
    title: String = "Görselleştirme",
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Canvas content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.surface
                            ),
                            radius = 1500f
                        )
                    )
            ) {
                content()
            }
        }
    }
}