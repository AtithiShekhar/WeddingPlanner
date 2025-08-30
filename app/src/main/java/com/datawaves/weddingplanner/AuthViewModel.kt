// viewmodel/AuthViewModel.kt
package com.example.weddingplanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weddingplanner.data.DataManager
import com.example.weddingplanner.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            val user = DataManager.getCurrentUser()
            _currentUser.value = user
            _isLoggedIn.value = DataManager.isUserLoggedIn()
        }
    }

    fun login(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null

            try {
                if (email.isBlank()) {
                    _authError.value = "Email cannot be empty"
                    return@launch
                }

                if (!isValidEmail(email)) {
                    _authError.value = "Please enter a valid email address"
                    return@launch
                }

                val success = DataManager.loginUser(email)
                if (success) {
                    _currentUser.value = DataManager.getCurrentUser()
                    _isLoggedIn.value = true
                } else {
                    _authError.value = "User not found. Please register first."
                }
            } catch (e: Exception) {
                _authError.value = "Login failed. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(email: String, phoneNumber: String, name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null

            try {
                if (email.isBlank() || name.isBlank()) {
                    _authError.value = "Email and name are required"
                    return@launch
                }

                if (!isValidEmail(email)) {
                    _authError.value = "Please enter a valid email address"
                    return@launch
                }

                if (phoneNumber.isNotBlank() && !isValidPhoneNumber(phoneNumber)) {
                    _authError.value = "Please enter a valid phone number"
                    return@launch
                }

                val success = DataManager.registerUser(email, phoneNumber, name)
                if (success) {
                    _currentUser.value = DataManager.getCurrentUser()
                    _isLoggedIn.value = true
                } else {
                    _authError.value = "User already exists. Please login instead."
                }
            } catch (e: Exception) {
                _authError.value = "Registration failed. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            DataManager.logoutUser()
            _currentUser.value = null
            _isLoggedIn.value = false
        }
    }

    fun clearError() {
        _authError.value = null
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.length >= 10 && phoneNumber.all { it.isDigit() }
    }
}