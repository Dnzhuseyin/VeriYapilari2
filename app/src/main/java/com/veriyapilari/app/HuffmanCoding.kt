package com.veriyapilari.app

import java.util.PriorityQueue

/**
 * Huffman Coding algoritması
 * Sıkıştırma için kullanılan optimal prefix code üreten algoritma
 */
class HuffmanCoding {

    private var root: HuffmanNode? = null
    private val codes = mutableMapOf<Char, String>()

    /**
     * Metni analiz edip Huffman ağacını oluşturur
     *
     * @param text Kodlanacak metin
     * @return Adım adım açıklamalar
     */
    fun buildTree(text: String): List<String> {
        val steps = mutableListOf<String>()

        if (text.isEmpty()) {
            steps.add("❌ Metin boş!")
            return steps
        }

        steps.add("📝 Huffman Coding başlatılıyor...")
        steps.add("Metin: \"$text\"")
        steps.add("")

        // 1. Frekans hesaplama
        steps.add("1️⃣ ADIM 1: Frekans Hesaplama")
        val frequencyMap = mutableMapOf<Char, Int>()
        for (char in text) {
            frequencyMap[char] = frequencyMap.getOrDefault(char, 0) + 1
        }

        frequencyMap.forEach { (char, freq) ->
            steps.add("   '$char' karakteri: $freq kez geçiyor")
        }
        steps.add("")

        // 2. Priority Queue oluşturma
        steps.add("2️⃣ ADIM 2: Priority Queue (Min Heap) Oluşturma")
        val pq = PriorityQueue<HuffmanNode>()
        frequencyMap.forEach { (char, freq) ->
            val node = HuffmanNode(char, freq)
            pq.offer(node)
            steps.add("   Düğüm eklendi: '$char' (frekans: $freq)")
        }
        steps.add("   Queue boyutu: ${pq.size}")
        steps.add("")

        // 3. Huffman ağacını oluşturma
        steps.add("3️⃣ ADIM 3: Huffman Ağacı Oluşturma")
        var stepCount = 1

        while (pq.size > 1) {
            steps.add("   --- İterasyon $stepCount ---")

            // En küçük 2 düğümü al
            val left = pq.poll()
            val right = pq.poll()

            steps.add("   En küçük 2 düğüm çıkarıldı:")
            steps.add("     Sol: ${left?.char ?: "null"} (${left?.frequency})")
            steps.add("     Sağ: ${right?.char ?: "null"} (${right?.frequency})")

            // Yeni iç düğüm oluştur
            val newFreq = (left?.frequency ?: 0) + (right?.frequency ?: 0)
            val parent = HuffmanNode(null, newFreq, left, right)

            steps.add("   Yeni iç düğüm: frekans = $newFreq")
            steps.add("   Queue'ya geri eklendi")

            pq.offer(parent)
            stepCount++
            steps.add("")
        }

        root = pq.poll()
        steps.add("✅ Huffman ağacı tamamlandı!")
        steps.add("")

        // 4. Huffman kodlarını üret
        steps.add("4️⃣ ADIM 4: Huffman Kodları Üretiliyor")
        codes.clear()
        generateCodes(root, "", steps)
        steps.add("")

        // 5. Sıkıştırma oranı hesaplama
        steps.add("5️⃣ ADIM 5: Sıkıştırma Analizi")
        val originalBits = text.length * 8
        var huffmanBits = 0
        frequencyMap.forEach { (char, freq) ->
            huffmanBits += freq * (codes[char]?.length ?: 0)
        }
        val ratio = ((originalBits - huffmanBits) * 100.0 / originalBits)

        steps.add("   Orijinal boyut: $originalBits bit (${text.length} karakter × 8)")
        steps.add("   Huffman boyutu: $huffmanBits bit")
        steps.add("   Kazanç: ${originalBits - huffmanBits} bit")
        steps.add("   Sıkıştırma oranı: %.2f%%".format(ratio))

        return steps
    }

    /**
     * Huffman kodlarını recursive olarak üretir
     */
    private fun generateCodes(node: HuffmanNode?, code: String, steps: MutableList<String>) {
        if (node == null) return

        if (node.isLeaf()) {
            codes[node.char!!] = code.ifEmpty { "0" }
            steps.add("   '${node.char}': $code (${code.length} bit)")
        }

        generateCodes(node.left, code + "0", steps)
        generateCodes(node.right, code + "1", steps)
    }

    /**
     * Metni Huffman kodlarıyla kodlar
     */
    fun encode(text: String): String {
        return text.map { codes[it] ?: "" }.joinToString("")
    }

    /**
     * Huffman kodlarını çözer
     */
    fun decode(encoded: String): String {
        if (root == null) return ""

        val result = StringBuilder()
        var current = root

        for (bit in encoded) {
            current = if (bit == '0') current?.left else current?.right

            if (current?.isLeaf() == true) {
                result.append(current.char)
                current = root
            }
        }

        return result.toString()
    }

    fun getRoot(): HuffmanNode? = root
    fun getCodes(): Map<Char, String> = codes.toMap()
}
