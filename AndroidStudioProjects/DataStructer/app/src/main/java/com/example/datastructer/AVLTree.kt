package com.example.datastructer

/**
 * AVL Tree veri yapısı implementasyonu
 * 
 * AVL Tree, self-balancing binary search tree'dir.
 * Her düğümün balance factor'ü -1, 0 veya 1 olmalıdır.
 * 
 * Özellikler:
 * 1. Binary Search Tree özelliklerini korur
 * 2. Her düğümün balance factor'ü |1|'den küçük veya eşittir
 * 3. Rotasyonlar ile dengelenir
 */
class AVLTree {
    var root: AVLNode? = null
        private set
    
    /**
     * Ağaca yeni bir düğüm ekler
     * 
     * @param key Eklenecek değer
     * @return Ekleme işlemi sırasında yapılan adımların listesi
     */
    fun insert(key: Int): List<String> {
        val steps = mutableListOf<String>()
        steps.add("AVL Tree'ye yeni düğüm ekleniyor: $key")
        root = insertHelper(root, key, steps)
        return steps
    }
    
    /**
     * Insert yardımcı fonksiyonu
     */
    private fun insertHelper(node: AVLNode?, key: Int, steps: MutableList<String>): AVLNode? {
        if (node == null) {
            steps.add("Yaprak düğüme ulaşıldı, yeni düğüm oluşturuldu: $key")
            return AVLNode(key)
        }

        if (key < node.key) {
            steps.add("$key < ${node.key} olduğundan BST kuralına göre sol alt ağaca ekleniyor")
            node.left = insertHelper(node.left, key, steps)
        } else if (key > node.key) {
            steps.add("$key > ${node.key} olduğundan BST kuralına göre sağ alt ağaca ekleniyor")
            node.right = insertHelper(node.right, key, steps)
        } else {
            steps.add("$key zaten mevcut, AVL Tree'de duplicate değerlere izin verilmez")
            return node
        }

        // Yüksekliği güncelle
        node.updateHeight()

        // Balance factor'ü kontrol et
        val balance = node.getBalanceFactor()
        steps.add("Düğüm ${node.key} için balance factor: $balance (sol_yükseklik - sağ_yükseklik)")

        // Left Left Case
        if (balance < -1 && key < node.left!!.key) {
            steps.add("Left-Left dengesizlik: BF=$balance < -1 ve yeni düğüm ($key) sol çocuğun solunda")
            steps.add("→ Düğüm ${node.key} etrafında sağa rotasyon yapılıyor, böylece sol ağacın yüksekliği azaltılıyor")
            return rightRotate(node, steps)
        }

        // Right Right Case
        if (balance > 1 && key > node.right!!.key) {
            steps.add("Right-Right dengesizlik: BF=$balance > 1 ve yeni düğüm ($key) sağ çocuğun sağında")
            steps.add("→ Düğüm ${node.key} etrafında sola rotasyon yapılıyor, böylece sağ ağacın yüksekliği azaltılıyor")
            return leftRotate(node, steps)
        }

        // Left Right Case
        if (balance < -1 && key > node.left!!.key) {
            steps.add("Left-Right dengesizlik: BF=$balance < -1 ama yeni düğüm ($key) sol çocuğun sağında")
            steps.add("→ Sol çocuk (${node.left!!.key}) etrafında sola rotasyon yapılıyor, Left-Left case'e dönüştürülüyor")
            node.left = leftRotate(node.left!!, steps)
            steps.add("→ Şimdi düğüm ${node.key} etrafında sağa rotasyon yapılıyor → Denge sağlandı")
            return rightRotate(node, steps)
        }

        // Right Left Case
        if (balance > 1 && key < node.right!!.key) {
            steps.add("Right-Left dengesizlik: BF=$balance > 1 ama yeni düğüm ($key) sağ çocuğun solunda")
            steps.add("→ Sağ çocuk (${node.right!!.key}) etrafında sağa rotasyon yapılıyor, Right-Right case'e dönüştürülüyor")
            node.right = rightRotate(node.right!!, steps)
            steps.add("→ Şimdi düğüm ${node.key} etrafında sola rotasyon yapılıyor → Denge sağlandı")
            return leftRotate(node, steps)
        }

        if (balance >= -1 && balance <= 1) {
            steps.add("Düğüm ${node.key} dengeli (BF=$balance), rotasyon gerekmez")
        }

        return node
    }
    
    /**
     * Ağaçtan bir düğüm siler
     * 
     * @param key Silinecek değer
     * @return Silme işlemi sırasında yapılan adımların listesi
     */
    fun delete(key: Int): List<String> {
        val steps = mutableListOf<String>()
        steps.add("AVL Tree'den düğüm siliniyor: $key")
        root = deleteHelper(root, key, steps)
        return steps
    }
    
    /**
     * Delete yardımcı fonksiyonu
     */
    private fun deleteHelper(node: AVLNode?, key: Int, steps: MutableList<String>): AVLNode? {
        if (node == null) {
            steps.add("Düğüm bulunamadı: $key (ağaçta mevcut değil)")
            return null
        }

        if (key < node.key) {
            steps.add("$key < ${node.key} olduğundan sol alt ağaçta aranıyor")
            node.left = deleteHelper(node.left, key, steps)
        } else if (key > node.key) {
            steps.add("$key > ${node.key} olduğundan sağ alt ağaçta aranıyor")
            node.right = deleteHelper(node.right, key, steps)
        } else {
            steps.add("Silinecek düğüm bulundu: ${node.key}")

            // Tek çocuk veya çocuksuz
            if (node.left == null || node.right == null) {
                val temp = node.left ?: node.right
                if (temp == null) {
                    steps.add("→ Yaprak düğüm (çocuksuz), direkt silindi")
                    return null
                } else {
                    steps.add("→ Tek çocuklu düğüm, çocuk (${temp.key}) ile değiştirildi")
                    return temp
                }
            } else {
                // İki çocuk var, successor bul
                val successor = minValueNode(node.right!!)
                steps.add("→ İki çocuklu düğüm, sağ alt ağacın en küçük değeri (${successor.key}) ile değiştirilecek")
                node.key = successor.key
                steps.add("→ Düğüm ${successor.key} değerini aldı, şimdi successor sağ alt ağaçtan siliniyor")
                node.right = deleteHelper(node.right, successor.key, steps)
            }
        }

        // Yüksekliği güncelle
        node.updateHeight()

        // Balance factor'ü kontrol et
        val balance = node.getBalanceFactor()
        steps.add("Silme sonrası düğüm ${node.key} için balance factor: $balance")

        // Left Left Case
        if (balance < -1 && (node.left?.getBalanceFactor() ?: 0) <= 0) {
            steps.add("Left-Left dengesizlik: BF=$balance < -1 ve sol çocuk dengeli/sola yüklü")
            steps.add("→ Düğüm ${node.key} etrafında sağa rotasyon yapılıyor → Denge sağlanıyor")
            return rightRotate(node, steps)
        }

        // Right Right Case
        if (balance > 1 && (node.right?.getBalanceFactor() ?: 0) >= 0) {
            steps.add("Right-Right dengesizlik: BF=$balance > 1 ve sağ çocuk dengeli/sağa yüklü")
            steps.add("→ Düğüm ${node.key} etrafında sola rotasyon yapılıyor → Denge sağlanıyor")
            return leftRotate(node, steps)
        }

        // Left Right Case
        if (balance < -1 && (node.left?.getBalanceFactor() ?: 0) > 0) {
            steps.add("Left-Right dengesizlik: BF=$balance < -1 ama sol çocuk sağa yüklü")
            steps.add("→ Sol çocuk (${node.left!!.key}) etrafında sola rotasyon yapılıyor, Left-Left case'e dönüştürülüyor")
            node.left = leftRotate(node.left!!, steps)
            steps.add("→ Şimdi düğüm ${node.key} etrafında sağa rotasyon yapılıyor → Denge sağlandı")
            return rightRotate(node, steps)
        }

        // Right Left Case
        if (balance > 1 && (node.right?.getBalanceFactor() ?: 0) < 0) {
            steps.add("Right-Left dengesizlik: BF=$balance > 1 ama sağ çocuk sola yüklü")
            steps.add("→ Sağ çocuk (${node.right!!.key}) etrafında sağa rotasyon yapılıyor, Right-Right case'e dönüştürülüyor")
            node.right = rightRotate(node.right!!, steps)
            steps.add("→ Şimdi düğüm ${node.key} etrafında sola rotasyon yapılıyor → Denge sağlandı")
            return leftRotate(node, steps)
        }

        if (balance >= -1 && balance <= 1) {
            steps.add("Düğüm ${node.key} dengeli (BF=$balance), rotasyon gerekmez")
        }

        return node
    }
    
    /**
     * Sol rotasyon
     */
    private fun leftRotate(y: AVLNode, steps: MutableList<String>): AVLNode {
        val x = y.right!!
        val T2 = x.left
        
        steps.add("Left rotation: ${y.key} ve ${x.key}")
        
        x.left = y
        y.right = T2
        
        y.updateHeight()
        x.updateHeight()
        
        return x
    }
    
    /**
     * Sağ rotasyon
     */
    private fun rightRotate(x: AVLNode, steps: MutableList<String>): AVLNode {
        val y = x.left!!
        val T2 = y.right
        
        steps.add("Right rotation: ${x.key} ve ${y.key}")
        
        y.right = x
        x.left = T2
        
        x.updateHeight()
        y.updateHeight()
        
        return y
    }
    
    /**
     * Minimum değerli düğümü bulur
     */
    private fun minValueNode(node: AVLNode): AVLNode {
        var current = node
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }
    
    /**
     * Ağacın yüksekliğini hesaplar
     */
    fun height(): Int {
        return heightHelper(root)
    }
    
    private fun heightHelper(node: AVLNode?): Int {
        if (node == null) return 0
        return node.height
    }
    
    /**
     * Ağacı temizler
     */
    fun clear() {
        root = null
    }
}




