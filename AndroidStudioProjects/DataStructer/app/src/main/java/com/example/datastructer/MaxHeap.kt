package com.example.datastructer

/**
 * Max Heap veri yapısı implementasyonu
 * 
 * Max Heap, tam binary tree'dir ve her düğümün değeri
 * çocuklarının değerlerinden büyük veya eşittir.
 * 
 * Özellikler:
 * 1. Tam binary tree (complete binary tree)
 * 2. Max Heap property: parent >= children
 * 3. Array tabanlı implementasyon (görselleştirme için tree'ye dönüştürülür)
 */
class MaxHeap {
    // Array tabanlı heap storage
    private val heap = mutableListOf<Int>()
    
    // Kök düğüm (görselleştirme için)
    var root: HeapNode? = null
        private set
    
    /**
     * Heap'e yeni bir değer ekler
     * 
     * @param key Eklenecek değer
     * @return Ekleme işlemi sırasında yapılan adımların listesi
     */
    fun insert(key: Int): List<String> {
        val steps = mutableListOf<String>()
        steps.add("Heap'e yeni değer ekleniyor: $key")
        
        // Değeri heap'in sonuna ekle
        heap.add(key)
        steps.add("$key heap'in sonuna eklendi (index: ${heap.size - 1})")
        
        // Heapify up (yukarı doğru düzeltme)
        var currentIndex = heap.size - 1
        steps.addAll(heapifyUp(currentIndex))
        
        // Tree yapısını güncelle
        rebuildTree()
        
        return steps
    }
    
    /**
     * Heap'ten maksimum değeri (kök) siler
     * 
     * @return Silme işlemi sırasında yapılan adımların listesi
     */
    fun extractMax(): List<String> {
        val steps = mutableListOf<String>()
        
        if (heap.isEmpty()) {
            steps.add("Heap boş, silme işlemi yapılamaz")
            return steps
        }
        
        val maxValue = heap[0]
        steps.add("Maksimum değer bulundu: $maxValue (kök düğüm)")
        
        if (heap.size == 1) {
            heap.clear()
            steps.add("Heap'te tek eleman vardı, heap temizlendi")
        } else {
            // Son elemanı köke taşı
            heap[0] = heap[heap.size - 1]
            heap.removeAt(heap.size - 1)
            steps.add("Son eleman (${heap[0]}) köke taşındı")
            
            // Heapify down (aşağı doğru düzeltme)
            steps.addAll(heapifyDown(0))
        }
        
        // Tree yapısını güncelle
        rebuildTree()
        
        return steps
    }
    
    /**
     * Belirli bir değeri heap'ten siler
     * 
     * @param key Silinecek değer
     * @return Silme işlemi sırasında yapılan adımların listesi
     */
    fun delete(key: Int): List<String> {
        val steps = mutableListOf<String>()
        steps.add("Heap'ten değer siliniyor: $key")
        
        val index = heap.indexOf(key)
        if (index == -1) {
            steps.add("Değer bulunamadı: $key")
            return steps
        }
        
        steps.add("Değer bulundu, index: $index")
        
        // Son elemanla değiştir
        if (index == heap.size - 1) {
            heap.removeAt(heap.size - 1)
            steps.add("Son elemandı, direkt silindi")
        } else {
            val lastValue = heap[heap.size - 1]
            heap[index] = lastValue
            heap.removeAt(heap.size - 1)
            steps.add("Son eleman ($lastValue) ile değiştirildi")
            
            // Heapify işlemi
            if (index > 0 && heap[parent(index)] < heap[index]) {
                steps.add("Heapify Up yapılıyor (index: $index)")
                steps.addAll(heapifyUp(index))
            } else {
                steps.add("Heapify Down yapılıyor (index: $index)")
                steps.addAll(heapifyDown(index))
            }
        }
        
        // Tree yapısını güncelle
        rebuildTree()
        
        return steps
    }
    
    /**
     * Yukarı doğru heapify işlemi (ekleme sonrası)
     *
     * @param index Başlangıç index'i
     * @return Yapılan adımlar
     */
    private fun heapifyUp(index: Int): List<String> {
        val steps = mutableListOf<String>()
        var currentIndex = index

        if (currentIndex == 0) {
            steps.add("Kök düğüm eklendi, heapify gerekmez")
            return steps
        }

        while (currentIndex > 0) {
            val parentIndex = parent(currentIndex)

            if (heap[parentIndex] >= heap[currentIndex]) {
                steps.add("✓ Max Heap özelliği sağlandı: Parent[${parentIndex}]=${heap[parentIndex]} >= Current[${currentIndex}]=${heap[currentIndex]}")
                break
            }

            steps.add("✗ Max Heap ihlali tespit edildi: Parent[${parentIndex}]=${heap[parentIndex]} < Current[${currentIndex}]=${heap[currentIndex]}")
            steps.add("→ Parent daha küçük olduğundan, Max Heap özelliğini sağlamak için swap yapılıyor")
            // Swap
            val temp = heap[parentIndex]
            heap[parentIndex] = heap[currentIndex]
            heap[currentIndex] = temp
            steps.add("→ Swap tamamlandı: Parent[${parentIndex}]=${heap[parentIndex]}, Current[${currentIndex}]=${heap[currentIndex]}")
            steps.add("→ Kontrol yukarı doğru devam ediyor (index: $parentIndex)")

            currentIndex = parentIndex
        }

        if (currentIndex == 0) {
            steps.add("→ Kök düğüme ulaşıldı, heapify up tamamlandı")
        }

        return steps
    }
    
    /**
     * Aşağı doğru heapify işlemi (silme sonrası)
     *
     * @param index Başlangıç index'i
     * @return Yapılan adımlar
     */
    private fun heapifyDown(index: Int): List<String> {
        val steps = mutableListOf<String>()
        var currentIndex = index

        while (true) {
            var largestIndex = currentIndex
            val leftIndex = leftChild(currentIndex)
            val rightIndex = rightChild(currentIndex)

            steps.add("Kontrol ediliyor: Current[${currentIndex}]=${heap[currentIndex]}")

            // Sol çocuk kontrolü
            if (leftIndex < heap.size) {
                if (heap[leftIndex] > heap[largestIndex]) {
                    steps.add("→ Sol çocuk[${leftIndex}]=${heap[leftIndex]} > Current=${heap[largestIndex]}, en büyük olarak işaretlendi")
                    largestIndex = leftIndex
                } else {
                    steps.add("→ Sol çocuk[${leftIndex}]=${heap[leftIndex]} <= Current=${heap[currentIndex]}, OK")
                }
            }

            // Sağ çocuk kontrolü
            if (rightIndex < heap.size) {
                if (heap[rightIndex] > heap[largestIndex]) {
                    steps.add("→ Sağ çocuk[${rightIndex}]=${heap[rightIndex]} > Şu anki en büyük=${heap[largestIndex]}, yeni en büyük olarak işaretlendi")
                    largestIndex = rightIndex
                } else {
                    steps.add("→ Sağ çocuk[${rightIndex}]=${heap[rightIndex]} <= En büyük=${heap[largestIndex]}, OK")
                }
            }

            if (largestIndex == currentIndex) {
                steps.add("✓ Max Heap özelliği sağlandı: Current >= her iki çocuk, heapify down tamamlandı")
                break
            }

            steps.add("✗ Max Heap ihlali: Çocuk[${largestIndex}]=${heap[largestIndex]} > Parent[${currentIndex}]=${heap[currentIndex]}")
            steps.add("→ En büyük çocuğu (${heap[largestIndex]}) yukarı taşımak için swap yapılıyor")
            // Swap
            val temp = heap[currentIndex]
            heap[currentIndex] = heap[largestIndex]
            heap[largestIndex] = temp
            steps.add("→ Swap tamamlandı: Parent[${currentIndex}]=${heap[currentIndex]}, Child[${largestIndex}]=${heap[largestIndex]}")
            steps.add("→ Kontrol aşağı doğru devam ediyor (index: $largestIndex)")

            currentIndex = largestIndex
        }

        return steps
    }
    
    /**
     * Parent index'ini hesaplar
     */
    private fun parent(index: Int): Int = (index - 1) / 2
    
    /**
     * Sol çocuk index'ini hesaplar
     */
    private fun leftChild(index: Int): Int = 2 * index + 1
    
    /**
     * Sağ çocuk index'ini hesaplar
     */
    private fun rightChild(index: Int): Int = 2 * index + 2
    
    /**
     * Array tabanlı heap'i tree yapısına dönüştürür (görselleştirme için)
     */
    private fun rebuildTree() {
        if (heap.isEmpty()) {
            root = null
            return
        }
        
        // Array'den tree oluştur
        val nodes = Array<HeapNode?>(heap.size) { null }
        
        // Tüm düğümleri oluştur
        for (i in heap.indices) {
            nodes[i] = HeapNode(heap[i])
        }
        
        // Parent-child ilişkilerini kur
        for (i in heap.indices) {
            val leftIndex = leftChild(i)
            val rightIndex = rightChild(i)
            
            if (leftIndex < heap.size) {
                nodes[i]!!.left = nodes[leftIndex]
                nodes[leftIndex]!!.parent = nodes[i]
            }
            
            if (rightIndex < heap.size) {
                nodes[i]!!.right = nodes[rightIndex]
                nodes[rightIndex]!!.parent = nodes[i]
            }
        }
        
        root = nodes[0]
    }
    
    /**
     * Heap'in yüksekliğini hesaplar
     */
    fun height(): Int {
        if (heap.isEmpty()) return 0
        return (Math.log(heap.size.toDouble()) / Math.log(2.0)).toInt() + 1
    }
    
    /**
     * Heap'in boş olup olmadığını kontrol eder
     */
    fun isEmpty(): Boolean = heap.isEmpty()
    
    /**
     * Heap'in boyutunu döndürür
     */
    fun size(): Int = heap.size
    
    /**
     * Heap'i temizler
     */
    fun clear() {
        heap.clear()
        root = null
    }
    
    /**
     * Heap'in tüm elemanlarını döndürür
     */
    fun getElements(): List<Int> = heap.toList()
}




