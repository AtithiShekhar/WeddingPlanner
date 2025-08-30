package com.example.weddingplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weddingplanner.ui.screens.AuthScreen
import com.example.weddingplanner.ui.screens.ChecklistScreen
import com.example.weddingplanner.ui.screens.HomeScreen
import com.example.weddingplanner.ui.screens.VenueListScreen
import com.example.weddingplanner.ui.theme.WeddingPlannerTheme
import com.example.weddingplanner.viewmodel.AuthViewModel
import com.example.weddingplanner.viewmodel.ChecklistViewModel
import com.example.weddingplanner.viewmodel.VenueViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val checklistViewModel: ChecklistViewModel by viewModels()
    private val venueViewModel: VenueViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeddingPlannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeddingPlannerApp(
                        authViewModel = authViewModel,
                        checklistViewModel = checklistViewModel,
                        venueViewModel = venueViewModel) } } } } }
@Composable
fun WeddingPlannerApp(
    authViewModel: AuthViewModel,
    checklistViewModel: ChecklistViewModel,
    venueViewModel: VenueViewModel
) {
    val navController = rememberNavController()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "auth"
    ) {
        composable("auth") {
            AuthScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true } } }) }
        composable("home") {
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel) }
        composable("checklist") {
            ChecklistScreen(
                navController = navController,
                viewModel = checklistViewModel
            )
        }

        composable("venues") {
            VenueListScreen(
                navController = navController,
                viewModel = venueViewModel) } } }