package com.example.weddingplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weddingplanner.data.DataManager
import com.example.weddingplanner.data.models.ChecklistItem
import com.example.weddingplanner.data.models.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChecklistViewModel : ViewModel() {

    private val _checklistItems = MutableStateFlow<List<ChecklistItem>>(emptyList())
    val checklistItems: StateFlow<List<ChecklistItem>> = _checklistItems.asStateFlow()

    private val _filteredItems = MutableStateFlow<List<ChecklistItem>>(emptyList())
    val filteredItems: StateFlow<List<ChecklistItem>> = _filteredItems.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadChecklistItems()
    }

    private fun loadChecklistItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val items = DataManager.getChecklistItems()
                _checklistItems.value = items
                applyFilters()
            } finally {
                _isLoading.value = false } } }

    fun addChecklistItem(title: String, description: String, category: String, priority: Priority) {
        viewModelScope.launch {
            val newItem = ChecklistItem(
                title = title,
                description = description,
                category = category,
                priority = priority
            )
            DataManager.addChecklistItem(newItem)
            loadChecklistItems() } }
    fun updateChecklistItem(item: ChecklistItem) {
        viewModelScope.launch {
            DataManager.updateChecklistItem(item)
            loadChecklistItems() } }
    fun toggleItemCompletion(itemId: String) {
        viewModelScope.launch {
            val currentItems = _checklistItems.value
            val item = currentItems.find { it.id == itemId }
            item?.let {
                val updatedItem = it.copy(isCompleted = !it.isCompleted)
                DataManager.updateChecklistItem(updatedItem)
                loadChecklistItems() } } }
    fun deleteChecklistItem(itemId: String) {
        viewModelScope.launch {
            DataManager.deleteChecklistItem(itemId)
            loadChecklistItems()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
        applyFilters()
    }

    private fun applyFilters() {
        val items = _checklistItems.value
        val query = _searchQuery.value.lowercase()
        val category = _selectedCategory.value

        val filtered = items.filter { item ->
            val matchesSearch = query.isEmpty() ||
                    item.title.lowercase().contains(query) ||
                    item.description.lowercase().contains(query)

            val matchesCategory = category == "All" || item.category == category

            matchesSearch && matchesCategory
        }.sortedWith(
            compareBy<ChecklistItem> { it.isCompleted }
                .thenByDescending { it.priority.ordinal }
        )

        _filteredItems.value = filtered
    }

    fun getCategories(): List<String> {
        val items = _checklistItems.value
        val categories = items.map { it.category }.distinct().sorted()
        return listOf("All") + categories
    }

    fun getCompletionStats(): Pair<Int, Int> {
        val items = _checklistItems.value
        val completed = items.count { it.isCompleted }
        val total = items.size
        return Pair(completed, total)
    }
}