package com.example.weddingplanner.data

import com.example.weddingplanner.data.models.ChecklistItem
import com.example.weddingplanner.data.models.Priority
import com.example.weddingplanner.data.models.User
import com.example.weddingplanner.data.models.Venue

object DataManager {

    private val users = mutableListOf<User>()
    private var currentUser: User? = null

    private val defaultChecklistItems = listOf(
        ChecklistItem(
            title = "Book Wedding Venue",
            description = "Find and book the perfect venue for your special day",
            category = "Venue",
            priority = Priority.HIGH
        ),
        ChecklistItem(
            title = "Hire Wedding Photographer",
            description = "Book a professional photographer to capture memories",
            category = "Photography",
            priority = Priority.HIGH
        ),
        ChecklistItem(
            title = "Select Catering Service",
            description = "Choose menu and catering service for the wedding",
            category = "Catering",
            priority = Priority.HIGH
        ),
        ChecklistItem(
            title = "Plan Mehendi Ceremony",
            description = "Organize mehendi ceremony and book mehendi artist",
            category = "Ceremonies",
            priority = Priority.MEDIUM
        ),
        ChecklistItem(
            title = "Plan Sangeet Event",
            description = "Organize sangeet ceremony with music and dance",
            category = "Ceremonies",
            priority = Priority.MEDIUM
        ),
        ChecklistItem(
            title = "Book Honeymoon Package",
            description = "Plan and book honeymoon destination and accommodation",
            category = "Travel",
            priority = Priority.MEDIUM
        ),
        ChecklistItem(
            title = "Order Wedding Invitations",
            description = "Design and order wedding invitation cards",
            category = "Invitations",
            priority = Priority.HIGH
        ),
        ChecklistItem(
            title = "Wedding Dress Shopping",
            description = "Find and purchase wedding dress and accessories",
            category = "Attire",
            priority = Priority.HIGH
        ),
        ChecklistItem(
            title = "Book Florist",
            description = "Select and book florist for wedding decorations",
            category = "Decoration",
            priority = Priority.MEDIUM
        ),
        ChecklistItem(
            title = "Arrange Transportation",
            description = "Book cars/transportation for wedding day",
            category = "Transportation",
            priority = Priority.MEDIUM
        )
    )

    private val checklistItems = defaultChecklistItems.toMutableList()

    private val venues = listOf(
        Venue(
            name = "Royal Palace Gardens",
            location = "Mumbai, Maharashtra",
            priceRange = "₹2,00,000 - ₹5,00,000",
            capacity = "200-500 guests",
            description = "Luxurious palace-style venue with beautiful gardens",
            amenities = listOf("AC Halls", "Garden Area", "Parking", "Catering", "Decoration"),
            rating = 4.8f,
            contactNumber = "+91 98765 43210",
            email = "bookings@royalpalace.com"
        ),
        Venue(
            name = "Sunset Beach Resort",
            location = "Goa",
            priceRange = "₹3,00,000 - ₹8,00,000",
            capacity = "150-300 guests",
            description = "Beachside resort perfect for destination weddings",
            amenities = listOf("Beach Access", "Resort Accommodation", "Catering", "Photography"),
            rating = 4.9f,
            contactNumber = "+91 87654 32109",
            email = "events@sunsetbeach.com"
        ),
        Venue(
            name = "Heritage Haveli",
            location = "Rajasthan",
            priceRange = "₹1,50,000 - ₹4,00,000",
            capacity = "100-400 guests",
            description = "Traditional Rajasthani haveli with royal ambiance",
            amenities = listOf("Royal Architecture", "Traditional Decor", "Folk Performances", "Catering"),
            rating = 4.7f,
            contactNumber = "+91 76543 21098",
            email = "bookings@heritagehaveli.com"
        ),
        Venue(
            name = "Crystal Banquet Hall",
            location = "Delhi",
            priceRange = "₹1,00,000 - ₹3,00,000",
            capacity = "250-600 guests",
            description = "Modern banquet hall with crystal chandeliers",
            amenities = listOf("AC Halls", "LED Lighting", "Stage Setup", "Parking", "Catering"),
            rating = 4.5f,
            contactNumber = "+91 65432 10987",
            email = "info@crystalbanquet.com"
        ),
        Venue(
            name = "Garden Paradise Resort",
            location = "Kerala",
            priceRange = "₹2,50,000 - ₹6,00,000",
            capacity = "100-350 guests",
            description = "Tropical paradise with lush gardens and backwaters",
            amenities = listOf("Garden Views", "Backwater Access", "Ayurvedic Spa", "Traditional Cuisine"),
            rating = 4.8f,
            contactNumber = "+91 54321 09876",
            email = "weddings@gardenparadise.com"
        ),
        Venue(
            name = "Metropolitan Grand Hotel",
            location = "Bangalore",
            priceRange = "₹1,80,000 - ₹4,50,000",
            capacity = "200-500 guests",
            description = "Luxury hotel with modern amenities in the city center",
            amenities = listOf("5-Star Service", "Multiple Halls", "Hotel Rooms", "Fine Dining"),
            rating = 4.6f,
            contactNumber = "+91 43210 98765",
            email = "events@metrogrand.com"
        ),
        Venue(
            name = "Hillside Manor",
            location = "Shimla, Himachal Pradesh",
            priceRange = "₹1,20,000 - ₹3,50,000",
            capacity = "80-250 guests",
            description = "Scenic hillside venue with mountain views",
            amenities = listOf("Mountain Views", "Outdoor Ceremony Area", "Cozy Interiors", "Local Cuisine"),
            rating = 4.4f,
            contactNumber = "+91 32109 87654",
            email = "bookings@hillsidemanor.com"
        ),
        Venue(
            name = "Urban Rooftop Venue",
            location = "Pune, Maharashtra",
            priceRange = "₹90,000 - ₹2,50,000",
            capacity = "100-300 guests",
            description = "Modern rooftop venue with city skyline views",
            amenities = listOf("Rooftop Access", "City Views", "Modern Decor", "DJ Setup"),
            rating = 4.3f,
            contactNumber = "+91 21098 76543",
            email = "events@urbanrooftop.com"
        ),
        Venue(
            name = "Riverside Retreat",
            location = "Rishikesh, Uttarakhand",
            priceRange = "₹1,60,000 - ₹4,20,000",
            capacity = "120-280 guests",
            description = "Peaceful riverside venue perfect for intimate weddings",
            amenities = listOf("River Views", "Adventure Activities", "Yoga Sessions", "Organic Food"),
            rating = 4.7f,
            contactNumber = "+91 10987 65432",
            email = "weddings@riversideretreat.com"
        ),
        Venue(
            name = "Desert Oasis Resort",
            location = "Jaisalmer, Rajasthan",
            priceRange = "₹2,20,000 - ₹5,50,000",
            capacity = "150-400 guests",
            description = "Exotic desert resort with camel safari and cultural performances",
            amenities = listOf("Desert Safari", "Cultural Shows", "Traditional Tents", "Rajasthani Cuisine"),
            rating = 4.9f,
            contactNumber = "+91 09876 54321",
            email = "bookings@desertoasis.com"
        )
    )
    fun registerUser(email: String, phoneNumber: String, name: String): Boolean {
        if (users.any { it.email == email }) {
            return false // User already exist
        }

        val newUser = User(
            email = email,
            phoneNumber = phoneNumber,
            name = name,
            isLoggedIn = true
        )
        users.add(newUser)
        currentUser = newUser
        return true
    }

    fun loginUser(email: String): Boolean {
        val user = users.find { it.email == email }
        return if (user != null) {
            currentUser = user.copy(isLoggedIn = true)
            users.removeIf { it.email == email }
            users.add(currentUser!!)
            true
        } else {
            false } }

    fun getCurrentUser(): User? = currentUser

    fun logoutUser() {
        currentUser?.let { user ->
            val updatedUser = user.copy(isLoggedIn = false)
            users.removeIf { it.email == user.email }
            users.add(updatedUser)
        }
        currentUser = null
    }

    fun isUserLoggedIn(): Boolean = currentUser?.isLoggedIn == true

    // Checklist
    fun getChecklistItems(): List<ChecklistItem> = checklistItems.toList()

    fun addChecklistItem(item: ChecklistItem) {
        checklistItems.add(item)
    }

    fun updateChecklistItem(item: ChecklistItem) {
        val index = checklistItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            checklistItems[index] = item
        }
    }

    fun deleteChecklistItem(itemId: String) {
        checklistItems.removeIf { it.id == itemId }
    }

    // Venue
    fun getVenues(): List<Venue> = venues

    fun getVenueById(id: String): Venue? = venues.find { it.id == id }
}