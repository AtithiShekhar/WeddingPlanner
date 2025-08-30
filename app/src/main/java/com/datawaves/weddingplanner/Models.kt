// data/models/Models.kt
package com.example.weddingplanner.data.models

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val phoneNumber: String = "",
    val name: String = "",
    val isLoggedIn: Boolean = false
)

data class ChecklistItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val category: String = "General",
    val priority: Priority = Priority.MEDIUM,
    val dueDate: String = ""
)

enum class Priority {
    LOW, MEDIUM, HIGH
}

data class Venue(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val location: String,
    val priceRange: String,
    val capacity: String,
    val description: String = "",
    val amenities: List<String> = emptyList(),
    val imageUrl: String = "",
    val rating: Float = 0f,
    val contactNumber: String = "",
    val email: String = ""
)

data class VenueFilter(
    val minBudget: Int = 0,
    val maxBudget: Int = Int.MAX_VALUE,
    val minCapacity: Int = 0,
    val maxCapacity: Int = Int.MAX_VALUE,
    val location: String = ""
)