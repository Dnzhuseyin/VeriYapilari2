package com.example.datastructer

/**
 * Hash Table veri yapısı
 * Linear Probing, Quadratic Probing, Double Hashing destekler
 */
class HashTable(
    private val size: Int = 11,
    private val collisionMethod: CollisionMethod = CollisionMethod.LINEAR
) {
    enum class CollisionMethod {
        LINEAR, QUADRATIC, DOUBLE_HASHING
    }
    
    private val table = Array<Int?>(size) { null }
    private val deleted = BooleanArray(size) { false }
    
    fun insert(key: Int): List<String> {
        val steps = mutableListOf<String>()
        steps.add("Hash Table'ye ekleniyor: $key")
        val hash = hashFunction(key)
        val initialIndex = hash % size
        steps.add("Hash fonksiyonu: h(key) = key")
        steps.add("Hesaplama: h($key) = $hash")
        steps.add("Index hesaplama: $hash % $size = $initialIndex")
        steps.add("Başlangıç yuvası: index $initialIndex")

        when (collisionMethod) {
            CollisionMethod.LINEAR -> insertLinear(key, hash, steps)
            CollisionMethod.QUADRATIC -> insertQuadratic(key, hash, steps)
            CollisionMethod.DOUBLE_HASHING -> insertDoubleHashing(key, hash, steps)
        }

        return steps
    }
    
    private fun insertLinear(key: Int, hash: Int, steps: MutableList<String>) {
        var index = hash % size
        var attempts = 0
        while (table[index] != null && !deleted[index] && attempts < size) {
            val nextIndex = (index + 1) % size
            steps.add("Çakışma! Index $index dolu")
            steps.add("Linear Probing: h'(key, i) = (h(key) + i) % $size")
            steps.add("Hesaplama: ($hash + ${attempts + 1}) % $size = $nextIndex")
            steps.add("Sonraki yuva: index $nextIndex deneniyor")
            index = nextIndex
            attempts++
        }
        if (attempts >= size) {
            steps.add("Tablo dolu, ekleme başarısız")
            return
        }
        table[index] = key
        deleted[index] = false
        steps.add("✓ Başarılı! $key değeri index $index'e eklendi")
    }
    
    private fun insertQuadratic(key: Int, hash: Int, steps: MutableList<String>) {
        var index = hash % size
        var i = 1
        var attempts = 0
        while (table[index] != null && !deleted[index] && attempts < size) {
            val newIndex = (hash + i * i) % size
            steps.add("Çakışma! Index $index dolu")
            steps.add("Quadratic Probing: h'(key, i) = (h(key) + i²) % $size")
            steps.add("Hesaplama: ($hash + $i²) % $size = ($hash + ${i * i}) % $size = $newIndex")
            steps.add("Sonraki yuva: index $newIndex deneniyor")
            index = newIndex
            i++
            attempts++
        }
        if (attempts >= size) {
            steps.add("Tablo dolu, ekleme başarısız")
            return
        }
        table[index] = key
        deleted[index] = false
        steps.add("✓ Başarılı! $key değeri index $index'e eklendi")
    }
    
    private fun insertDoubleHashing(key: Int, hash: Int, steps: MutableList<String>) {
        val hash2 = hashFunction2(key)
        var index = hash % size
        var i = 1
        var attempts = 0
        steps.add("İkinci hash fonksiyonu: h2(key) = 7 - (key % 7)")
        steps.add("Hesaplama: h2($hash) = 7 - ($hash % 7) = $hash2")

        while (table[index] != null && !deleted[index] && attempts < size) {
            val newIndex = (hash + i * hash2) % size
            steps.add("Çakışma! Index $index dolu")
            steps.add("Double Hashing: h'(key, i) = (h(key) + i × h2(key)) % $size")
            steps.add("Hesaplama: ($hash + $i × $hash2) % $size = ($hash + ${i * hash2}) % $size = $newIndex")
            steps.add("Sonraki yuva: index $newIndex deneniyor")
            index = newIndex
            i++
            attempts++
        }
        if (attempts >= size) {
            steps.add("Tablo dolu, ekleme başarısız")
            return
        }
        table[index] = key
        deleted[index] = false
        steps.add("✓ Başarılı! $key değeri index $index'e eklendi")
    }
    
    fun delete(key: Int): List<String> {
        val steps = mutableListOf<String>()
        steps.add("Hash Table'den siliniyor: $key")
        val hash = hashFunction(key)
        var index = hash % size
        var attempts = 0
        
        when (collisionMethod) {
            CollisionMethod.LINEAR -> {
                while ((table[index] != key || deleted[index]) && attempts < size) {
                    index = (index + 1) % size
                    attempts++
                }
            }
            CollisionMethod.QUADRATIC -> {
                var i = 1
                while ((table[index] != key || deleted[index]) && attempts < size) {
                    index = (hash + i * i) % size
                    i++
                    attempts++
                }
            }
            CollisionMethod.DOUBLE_HASHING -> {
                val hash2 = hashFunction2(key)
                var i = 1
                while ((table[index] != key || deleted[index]) && attempts < size) {
                    index = (hash + i * hash2) % size
                    i++
                    attempts++
                }
            }
        }
        
        if (table[index] == key && !deleted[index]) {
            deleted[index] = true
            steps.add("$key index $index'den silindi (lazy deletion)")
        } else {
            steps.add("$key bulunamadı")
        }
        
        return steps
    }
    
    private fun hashFunction(key: Int): Int {
        return key
    }
    
    private fun hashFunction2(key: Int): Int {
        return 7 - (key % 7) // Prime number için
    }
    
    fun getTable(): Array<Int?> = table.copyOf()
    fun getDeleted(): BooleanArray = deleted.copyOf()
    
    fun clear() {
        for (i in table.indices) {
            table[i] = null
            deleted[i] = false
        }
    }
}




