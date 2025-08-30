// viewmodel/VenueViewModel.kt
package com.example.weddingplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weddingplanner.data.DataManager
import com.example.weddingplanner.data.models.Venue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VenueViewModel : ViewModel() {

    private val _venues = MutableStateFlow<List<Venue>>(emptyList())
    val venues: StateFlow<List<Venue>> = _venues.asStateFlow()

    private val _filteredVenues = MutableStateFlow<List<Venue>>(emptyList())
    val filteredVenues: StateFlow<List<Venue>> = _filteredVenues.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedLocation = MutableStateFlow("All")
    val selectedLocation: StateFlow<String> = _selectedLocation.asStateFlow()

    private val _budgetRange = MutableStateFlow(Pair(0, 1000000))
    val budgetRange: StateFlow<Pair<Int, Int>> = _budgetRange.asStateFlow()

    private val _capacityRange = MutableStateFlow(Pair(0, 1000))
    val capacityRange: StateFlow<Pair<Int, Int>> = _capacityRange.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showFilters = MutableStateFlow(false)
    val showFilters: StateFlow<Boolean> = _showFilters.asStateFlow()

    init {
        loadVenues()
    }

    private fun loadVenues() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val venues = DataManager.getVenues()
                _venues.value = venues
                applyFilters()
            } finally {
                _isLoading.value = false } } }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun setSelectedLocation(location: String) {
        _selectedLocation.value = location
        applyFilters()
    }

    fun setBudgetRange(minBudget: Int, maxBudget: Int) {
        _budgetRange.value = Pair(minBudget, maxBudget)
        applyFilters()
    }

    fun setCapacityRange(minCapacity: Int, maxCapacity: Int) {
        _capacityRange.value = Pair(minCapacity, maxCapacity)
        applyFilters()
    }

    fun toggleFilters() {
        _showFilters.value = !_showFilters.value
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedLocation.value = "All"
        _budgetRange.value = Pair(0, 1000000)
        _capacityRange.value = Pair(0, 1000)
        applyFilters()
    }

    private fun applyFilters() {
        val allVenues = _venues.value
        val query = _searchQuery.value.lowercase()
        val location = _selectedLocation.value
        val (minBudget, maxBudget) = _budgetRange.value
        val (minCapacity, maxCapacity) = _capacityRange.value

        val filtered = allVenues.filter { venue ->
            val matchesSearch = query.isEmpty() ||
                    venue.name.lowercase().contains(query) ||
                    venue.location.lowercase().contains(query) ||
                    venue.description.lowercase().contains(query)

            val matchesLocation = location == "All" || venue.location.contains(location, ignoreCase = true)

            val venueBudget = extractBudgetFromRange(venue.priceRange)
            val matchesBudget = venueBudget.first <= maxBudget && venueBudget.second >= minBudget

            val venueCapacity = extractCapacityFromRange(venue.capacity)
            val matchesCapacity = venueCapacity.first <= maxCapacity && venueCapacity.second >= minCapacity

            matchesSearch && matchesLocation && matchesBudget && matchesCapacity
        }.sortedByDescending { it.rating }

        _filteredVenues.value = filtered
    }

    private fun extractBudgetFromRange(priceRange: String): Pair<Int, Int> {
        val numbers = priceRange.replace("[^0-9,]".toRegex(), "")
            .split("-")
            .map { it.replace(",", "").trim() }
            .filter { it.isNotEmpty() }
            .mapNotNull { it.toIntOrNull() }

        return if (numbers.size >= 2) {
            Pair(numbers[0], numbers[1])
        } else if (numbers.size == 1) {
            Pair(numbers[0], numbers[0])
        } else {
            Pair(0, 1000000) } }

    private fun extractCapacityFromRange(capacity: String): Pair<Int, Int> {
        val numbers = capacity.replace("[^0-9-]".toRegex(), "")
            .split("-")
            .filter { it.isNotEmpty() }
            .mapNotNull { it.toIntOrNull() }

        return if (numbers.size >= 2) {
            Pair(numbers[0], numbers[1])
        } else if (numbers.size == 1) {
            Pair(numbers[0], numbers[0])
        } else {
            Pair(0, 1000)
        }
    }

    fun getLocations(): List<String> {
        val venues = _venues.value
        val locations = venues.map { venue ->
            venue.location.split(",")[0].trim()
        }.distinct().sorted()
        return listOf("All") + locations
    }

    fun getVenueById(id: String): Venue? {
        return _venues.value.find { it.id == id }
    }
}