package com.example.datastructer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HashViewModel : ViewModel() {
    private var hashTable = HashTable(11, HashTable.CollisionMethod.LINEAR)
    var table: Array<Int?> by mutableStateOf(Array(11) { null })
        private set
    var deleted: BooleanArray by mutableStateOf(BooleanArray(11) { false })
        private set
    var collisionMethod: HashTable.CollisionMethod by mutableStateOf(HashTable.CollisionMethod.LINEAR)
    var explanations: List<String> by mutableStateOf(emptyList())
        private set
    
    fun changeCollisionMethod(method: HashTable.CollisionMethod) {
        collisionMethod = method
        hashTable = HashTable(11, method)
        updateTable()
    }
    
    fun insert(key: Int) {
        val steps = hashTable.insert(key)
        explanations = steps
        updateTable()
    }
    
    fun delete(key: Int) {
        val steps = hashTable.delete(key)
        explanations = steps
        updateTable()
    }
    
    private fun updateTable() {
        table = hashTable.getTable()
        deleted = hashTable.getDeleted()
    }
    
    fun clear() {
        hashTable.clear()
        explanations = emptyList()
        updateTable()
    }
}

