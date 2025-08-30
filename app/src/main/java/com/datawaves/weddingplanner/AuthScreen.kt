// ui/screens/AuthScreen.kt
package com.example.weddingplanner.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weddingplanner.ui.theme.*
import com.example.weddingplanner.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val authError by authViewModel.authError.collectAsStateWithLifecycle()
    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Pink40.copy(alpha = 0.1f),
                        RoseGold.copy(alpha = 0.2f),
                        LightRose.copy(alpha = 0.3f))))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(
                text = "💐 Wedding Planner",
                style = MaterialTheme.typography.headlineLarge,
                color = Pink40,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp))
            Text(
                text = "Plan Your Perfect Day",
                style = MaterialTheme.typography.bodyLarge,
                color = DeepRose,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(4.dp)) {
                        Button(
                            onClick = { isLoginMode = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isLoginMode) Pink40 else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isLoginMode) White else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = if (isLoginMode) 4.dp else 0.dp)) {
                            Text("Login") }

                        Button(
                            onClick = { isLoginMode = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isLoginMode) Pink40 else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (!isLoginMode) White else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = if (!isLoginMode) 4.dp else 0.dp)) {
                            Text("Register") } }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            authViewModel.clearError()
                        },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Pink40,
                            focusedLabelColor = Pink40
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Registration
                    AnimatedVisibility(
                        visible = !isLoginMode,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column {
                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    authViewModel.clearError()
                                },
                                label = { Text("Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Pink40,
                                    focusedLabelColor = Pink40
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = {
                                    phoneNumber = it
                                    authViewModel.clearError()
                                },
                                label = { Text("Phone Number (Optional)") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Pink40,
                                    focusedLabelColor = Pink40))
                            Spacer(modifier = Modifier.height(16.dp)) } }
                    authError?.let { error ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Error.copy(alpha = 0.1f)
                            )
                        ) {
                            Text(
                                text = error,
                                color = Error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(12.dp)) } }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            if (isLoginMode) {
                                authViewModel.login(email)
                            } else {
                                authViewModel.register(email, phoneNumber, name)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Pink40
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = White
                            )
                        } else {
                            Text(
                                text = if (isLoginMode) "Login" else "Register",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium) } }
                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            isLoginMode = !isLoginMode
                            authViewModel.clearError() }) {
                        Text(
                            text = if (isLoginMode)
                                "Don't have an account? Register"
                            else
                                "Already have an account? Login",
                            color = Pink40) } } } } } }