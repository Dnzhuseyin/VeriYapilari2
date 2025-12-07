package com.veriyapilari.app

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext

// Tutorial steps for each screen
object ScreenTutorials {

    fun getRedBlackTreeTutorial(): List<SpotlightTarget> = listOf(
        SpotlightTarget(
            elementId = "rbt_welcome",
            title = "Red-Black Tree 🔴⚫",
            description = "Red-Black Tree, dengeli bir ikili arama ağacıdır. Her düğüm kırmızı veya siyah renge sahiptir ve belirli kurallara uyar.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "rbt_input_field",
            title = "Değer Giriş Alanı ✍️",
            description = "Buraya eklemek veya silmek istediğiniz sayıyı yazın. Sadece tam sayı değerleri kabul edilir.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "rbt_add_button",
            title = "Ekleme Butonu ➕",
            description = "Bu butona tıklayarak girdiğiniz değeri ağaca ekleyin. Ağaç otomatik olarak dengelenecektir.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "rbt_delete_button",
            title = "Silme Butonu 🗑️",
            description = "Girdiğiniz değeri ağaçtan silmek için bu çöp kutusu ikonuna tıklayın. Ağaç yeniden dengelenecektir.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "rbt_zoom_in",
            title = "Yakınlaştırma 🔍",
            description = "Ağacı büyüterek düğümleri daha net görebilirsiniz.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "rbt_zoom_out",
            title = "Uzaklaştırma 🔎",
            description = "Ağacın tamamını görmek için uzaklaştırabilirsiniz.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "rbt_reset_zoom",
            title = "Zoom Sıfırlama 🔄",
            description = "Görünümü varsayılan zoom seviyesine döndürür.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "rbt_clear",
            title = "Temizleme 🧹",
            description = "Tüm ağacı temizleyerek sıfırdan başlamanızı sağlar.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "rbt_canvas",
            title = "Görselleştirme Alanı 🎨",
            description = "Burası ağacın görüntülendiği alandır. Parmağınızla sürükleyerek ağacı hareket ettirebilirsiniz.",
            calloutPosition = HighlightPosition.TOP_CENTER
        ),
        SpotlightTarget(
            elementId = "rbt_explanation",
            title = "Adım Adım Açıklamalar 💡",
            description = "Her işlemin detaylı açıklamasını burada görebilirsiniz. Algoritmanın nasıl çalıştığını öğrenin!",
            calloutPosition = HighlightPosition.TOP_CENTER
        )
    )

    fun getAVLTreeTutorial(): List<SpotlightTarget> = listOf(
        SpotlightTarget(
            elementId = "avl_welcome",
            title = "AVL Tree ⚖️",
            description = "AVL Tree, her düğümde yükseklik dengesi sağlayan otomatik dengeli bir ikili arama ağacıdır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "avl_input_field",
            title = "Değer Giriş Alanı ✍️",
            description = "Buraya eklemek veya silmek istediğiniz sayıyı yazın.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "avl_add_button",
            title = "Ekleme Butonu ➕",
            description = "Değeri ağaca ekleyin. Gerekirse otomatik rotasyon yapılır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "avl_delete_button",
            title = "Silme Butonu 🗑️",
            description = "Girdiğiniz değeri ağaçtan silmek için bu ikona tıklayın.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "avl_zoom_in",
            title = "Yakınlaştırma 🔍",
            description = "Ağacı büyüterek düğümleri daha net görebilirsiniz.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "avl_zoom_out",
            title = "Uzaklaştırma 🔎",
            description = "Ağacın tamamını görmek için uzaklaştırabilirsiniz.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "avl_reset_zoom",
            title = "Zoom Sıfırlama 🔄",
            description = "Görünümü varsayılan zoom seviyesine döndürür.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "avl_clear",
            title = "Temizleme 🧹",
            description = "Tüm ağacı temizleyerek sıfırdan başlamanızı sağlar.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "avl_canvas",
            title = "Görselleştirme Alanı 🎨",
            description = "AVL ağacının görüntülendiği alan. Parmağınızla sürükleyerek hareket ettirebilirsiniz.",
            calloutPosition = HighlightPosition.TOP_CENTER
        ),
        SpotlightTarget(
            elementId = "avl_explanation",
            title = "Adım Adım Açıklamalar 💡",
            description = "Her işlemin detaylı açıklamasını burada görebilirsiniz. Algoritmanın nasıl çalıştığını öğrenin!",
            calloutPosition = HighlightPosition.TOP_CENTER
        )
    )

    fun getSplayTreeTutorial(): List<SpotlightTarget> = listOf(
        SpotlightTarget(
            elementId = "splay_welcome",
            title = "Splay Tree 🌳",
            description = "Splay Tree, son erişilen elemanı köke taşıyan kendi kendini düzenleyen bir ağaç yapısıdır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "splay_input_field",
            title = "Değer Giriş Alanı ✍️",
            description = "Buraya eklemek veya silmek istediğiniz sayıyı yazın.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "splay_add_button",
            title = "Ekleme Butonu ➕",
            description = "Değeri ağaca ekleyin. Eklenen eleman köke taşınır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "splay_delete_button",
            title = "Silme Butonu 🗑️",
            description = "Girdiğiniz değeri ağaçtan silmek için bu ikona tıklayın.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "splay_zoom_in",
            title = "Yakınlaştırma 🔍",
            description = "Ağacı büyüterek düğümleri daha net görebilirsiniz.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "splay_zoom_out",
            title = "Uzaklaştırma 🔎",
            description = "Ağacın tamamını görmek için uzaklaştırabilirsiniz.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "splay_reset_zoom",
            title = "Zoom Sıfırlama 🔄",
            description = "Görünümü varsayılan zoom seviyesine döndürür.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "splay_clear",
            title = "Temizleme 🧹",
            description = "Tüm ağacı temizleyerek sıfırdan başlamanızı sağlar.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "splay_canvas",
            title = "Görselleştirme Alanı 🎨",
            description = "Splay ağacının görüntülendiği alan. Parmağınızla sürükleyerek hareket ettirebilirsiniz.",
            calloutPosition = HighlightPosition.TOP_CENTER
        ),
        SpotlightTarget(
            elementId = "splay_explanation",
            title = "Adım Adım Açıklamalar 💡",
            description = "Her işlemin detaylı açıklamasını burada görebilirsiniz. Splay operasyonlarını izleyin!",
            calloutPosition = HighlightPosition.TOP_CENTER
        )
    )

    fun getMaxHeapTutorial(): List<SpotlightTarget> = listOf(
        SpotlightTarget(
            elementId = "heap_welcome",
            title = "Max Heap 🔺",
            description = "Max Heap, her ebeveyn düğümün çocuklarından büyük veya eşit olduğu tam ikili ağaç yapısıdır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "heap_input_field",
            title = "Değer Giriş Alanı ✍️",
            description = "Buraya eklemek istediğiniz sayıyı yazın.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "heap_add_button",
            title = "Ekleme Butonu ➕",
            description = "Yeni eleman sona eklenir ve yukarı doğru 'bubble up' işlemi yapılır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "heap_extract_button",
            title = "Extract Max Butonu 👑",
            description = "Kök elemanı (en büyük) çıkarır. Son eleman köke taşınır ve 'bubble down' yapılır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "heap_delete_button",
            title = "Silme Butonu 🗑️",
            description = "Belirli bir değeri heap'ten silmek için bu ikona tıklayın.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "heap_zoom_in",
            title = "Yakınlaştırma 🔍",
            description = "Heap'i büyüterek düğümleri daha net görebilirsiniz.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "heap_zoom_out",
            title = "Uzaklaştırma 🔎",
            description = "Heap'in tamamını görmek için uzaklaştırabilirsiniz.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "heap_reset_zoom",
            title = "Zoom Sıfırlama 🔄",
            description = "Görünümü varsayılan zoom seviyesine döndürür.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "heap_clear",
            title = "Temizleme 🧹",
            description = "Tüm heap'i temizleyerek sıfırdan başlamanızı sağlar.",
            calloutPosition = HighlightPosition.BOTTOM_LEFT
        ),
        SpotlightTarget(
            elementId = "heap_canvas",
            title = "Görselleştirme Alanı 🎨",
            description = "Max Heap'in görüntülendiği alan. Parmağınızla sürükleyerek hareket ettirebilirsiniz.",
            calloutPosition = HighlightPosition.TOP_CENTER
        ),
        SpotlightTarget(
            elementId = "heap_explanation",
            title = "Adım Adım Açıklamalar 💡",
            description = "Her işlemin detaylı açıklamasını burada görebilirsiniz. Heap operasyonlarını izleyin!",
            calloutPosition = HighlightPosition.TOP_CENTER
        )
    )

    fun getHashTableTutorial(): List<SpotlightTarget> = listOf(
        SpotlightTarget(
            elementId = "hash_welcome",
            title = "Hash Table 🗂️",
            description = "Hash Table, anahtar-değer çiftlerini saklayan ve hızlı erişim sağlayan bir veri yapısıdır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "hash_collision_method",
            title = "Çakışma Çözüm Yöntemi 🔧",
            description = "Linear, Quadratic veya Double Hashing yöntemlerinden birini seçin. Çakışma durumunda farklı stratejiler uygular.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "hash_input_field",
            title = "Değer Giriş Alanı ✍️",
            description = "Buraya eklemek veya silmek istediğiniz sayıyı yazın.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "hash_add_button",
            title = "Ekleme Butonu ➕",
            description = "Değer hash fonksiyonu ile tabloya eklenir. Çakışma varsa seçili yöntemle çözülür.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "hash_delete_button",
            title = "Silme Butonu 🗑️",
            description = "Değeri tablodan siler ve DELETED olarak işaretler.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "hash_canvas",
            title = "Hash Table Görselleştirme 📊",
            description = "Tablonun görsel temsili. Her indeks ve içeriği gösterilir. Çakışmaları ve çözümleri izleyebilirsiniz.",
            calloutPosition = HighlightPosition.TOP_CENTER
        ),
        SpotlightTarget(
            elementId = "hash_explanation",
            title = "Adım Adım Açıklamalar 💡",
            description = "Hash işlemlerinin detaylı açıklamasını burada görebilirsiniz.",
            calloutPosition = HighlightPosition.TOP_CENTER
        )
    )

    fun getHuffmanCodingTutorial(): List<SpotlightTarget> = listOf(
        SpotlightTarget(
            elementId = "huffman_welcome",
            title = "Huffman Kodlama 📝",
            description = "Huffman Kodlama, veri sıkıştırma için kullanılan optimal bir prefix kodlama algoritmasıdır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "huffman_input_field",
            title = "Metin Giriş Alanı ✍️",
            description = "Kodlamak istediğiniz metni buraya yazın. Algoritma her karakterin frekansını hesaplayacaktır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "huffman_encode_button",
            title = "Encode Butonu 🔐",
            description = "Metni Huffman algoritması ile kodlayın. Frekans tablosu ve ağaç oluşturulur.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "huffman_canvas",
            title = "Huffman Ağacı Görselleştirme 🌲",
            description = "Huffman ağacının görsel temsili. Sol dallar 0, sağ dallar 1 ile kodlanır.",
            calloutPosition = HighlightPosition.TOP_CENTER
        ),
        SpotlightTarget(
            elementId = "huffman_results",
            title = "Kodlama Sonuçları 💾",
            description = "Her karakter için ikili kod, frekans ve sıkıştırma oranını burada görebilirsiniz.",
            calloutPosition = HighlightPosition.TOP_CENTER
        )
    )

    fun getGraphAlgorithmsTutorial(): List<SpotlightTarget> = listOf(
        SpotlightTarget(
            elementId = "graph_welcome",
            title = "Graph Algoritmaları 🕸️",
            description = "Graf yapıları ve temel graf algoritmaları ile çalışabilirsiniz.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "graph_add_node",
            title = "Düğüm Ekleme ➕",
            description = "Graf'a yeni düğüm eklemek için bu butonu kullanın.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "graph_add_edge",
            title = "Kenar Ekleme 🔗",
            description = "İki düğüm arasında bağlantı oluşturun. Ağırlıklı veya ağırlıksız kenar ekleyebilirsiniz.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "graph_bfs_button",
            title = "BFS Algoritması 🌊",
            description = "Genişlik Öncelikli Arama. Başlangıç düğümünden katman katman graf'ı dolaşır.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "graph_dfs_button",
            title = "DFS Algoritması 🏔️",
            description = "Derinlik Öncelikli Arama. Bir dalı sonuna kadar takip eder, sonra geri döner.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "graph_dijkstra_button",
            title = "Dijkstra Algoritması 🗺️",
            description = "Ağırlıklı graf'ta en kısa yolu bulur. Başlangıç ve bitiş düğümü seçin.",
            calloutPosition = HighlightPosition.BOTTOM_CENTER
        ),
        SpotlightTarget(
            elementId = "graph_canvas",
            title = "Graf Görselleştirme 🎨",
            description = "Graf'ın görsel temsili. Düğümleri sürükleyerek yerlerini değiştirebilirsiniz.",
            calloutPosition = HighlightPosition.TOP_CENTER
        ),
        SpotlightTarget(
            elementId = "graph_explanation",
            title = "Algoritma Açıklamaları 💡",
            description = "Algoritma adımlarını burada görebilirsiniz. Ziyaret edilen düğümler farklı renklerde görünür.",
            calloutPosition = HighlightPosition.TOP_CENTER
        )
    )
}

// Composable function to show tutorial for a specific screen with spotlight
@Composable
fun ShowScreenTutorial(
    screenName: String,
    targets: List<SpotlightTarget>,
    targetPositions: Map<String, Rect>,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    var showTutorial by remember {
        mutableStateOf(!TutorialPreferences.isTutorialCompleted(context, screenName))
    }

    if (showTutorial) {
        SpotlightTutorial(
            targets = targets,
            targetPositions = targetPositions,
            onComplete = {
                TutorialPreferences.setTutorialCompleted(context, screenName)
                showTutorial = false
                onComplete()
            },
            onSkip = {
                TutorialPreferences.setTutorialCompleted(context, screenName)
                showTutorial = false
                onComplete()
            }
        )
    }
}
