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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HuffmanScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: HuffmanViewModel = viewModel()
    var inputValue by remember { mutableStateOf("") }
    var showExplanations by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Huffman Coding") },
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
                    IconButton(onClick = { viewModel.clear() }) {
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
                    // Huffman Kodlaması Hakkında Bilgi
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                "💡 Huffman Kodlaması Nedir?",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Huffman kodlaması, veri sıkıştırma için kullanılan optimal bir algoritma. " +
                                "Her karakter için frekansına göre değişken uzunlukta binary kod oluşturur:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "• Sık geçen karakterler → Kısa kodlar (örn: 'e' → '10')",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                "• Az geçen karakterler → Uzun kodlar (örn: 'z' → '11010')",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "📊 Ağaçta: Sol çocuk = '0', Sağ çocuk = '1'",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            label = { Text("Metin girin") },
                            placeholder = { Text("Örnek: 'hello world'") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (inputValue.isNotEmpty()) {
                                    viewModel.buildTree(inputValue)
                                }
                            },
                            modifier = Modifier.align(Alignment.Bottom)
                        ) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Oluştur")
                        }

                        if (viewModel.encodedText.isNotEmpty()) {
                            OutlinedButton(
                                onClick = { viewModel.decode() },
                                modifier = Modifier.align(Alignment.Bottom)
                            ) {
                                Text("Çöz")
                            }
                        }
                    }

                    // Huffman kodları ve sonuçlar
                    if (viewModel.huffmanCodes.isNotEmpty()) {
                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Kodlar
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        "📋 Huffman Kodları",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        "Frekansa göre üretildi:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    )
                                    Spacer(Modifier.height(8.dp))

                                    // Kodları frekansa göre sırala (çok → az)
                                    viewModel.huffmanCodes.entries
                                        .sortedByDescending { viewModel.frequencyMap[it.key] ?: 0 }
                                        .forEach { (char, code) ->
                                            val freq = viewModel.frequencyMap[char] ?: 0
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 2.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            "'$char'",
                                                            fontWeight = FontWeight.Bold,
                                                            style = MaterialTheme.typography.bodyMedium
                                                        )
                                                        Text(
                                                            "×$freq",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                                                        )
                                                    }
                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            code,
                                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.primary,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        Text(
                                                            "(${code.length}bit)",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                                                        )
                                                    }
                                                }
                                                val explanation = when {
                                                    freq >= viewModel.frequencyMap.values.maxOrNull() ?: 0 ->
                                                        "→ En sık geçen, kısa kod aldı!"
                                                    code.length <= 2 ->
                                                        "→ Sık kullanılıyor, verimli kod"
                                                    code.length >= 5 ->
                                                        "→ Az kullanılıyor, uzun kod"
                                                    else ->
                                                        "→ Orta sıklık, orta uzunluk"
                                                }
                                                Text(
                                                    explanation,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = when {
                                                        code.length <= 2 -> MaterialTheme.colorScheme.tertiary
                                                        code.length >= 5 -> MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                                                        else -> MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                                                    },
                                                    fontWeight = if (freq >= viewModel.frequencyMap.values.maxOrNull() ?: 0)
                                                        FontWeight.Bold else FontWeight.Normal,
                                                    modifier = Modifier.padding(start = 8.dp)
                                                )
                                            }
                                        }
                                }
                            }

                            // Kodlanmış/Çözülmüş
                            Column(modifier = Modifier.weight(1f)) {
                                if (viewModel.encodedText.isNotEmpty()) {
                                    Card(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                "Kodlanmış",
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            Spacer(Modifier.height(4.dp))
                                            Text(
                                                viewModel.encodedText.take(100) +
                                                if (viewModel.encodedText.length > 100) "..." else "",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                            )
                                        }
                                    }
                                }

                                if (viewModel.decodedText.isNotEmpty()) {
                                    Spacer(Modifier.height(8.dp))
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                "Çözülmüş",
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            Spacer(Modifier.height(4.dp))
                                            Text(viewModel.decodedText)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Açıklamalar
                    if (viewModel.explanations.isNotEmpty()) {
                        Spacer(Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "📖 Adım Adım Açıklama",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Switch(
                                        checked = showExplanations,
                                        onCheckedChange = { showExplanations = it }
                                    )
                                }

                                AnimatedVisibility(visible = showExplanations) {
                                    Column(
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .heightIn(max = 200.dp)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        viewModel.explanations.forEach { step ->
                                            Text(
                                                step,
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(vertical = 1.dp),
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Canvas alanı
            HuffmanCanvas(
                rootNode = viewModel.rootNode,
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
