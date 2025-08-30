// ui/screens/HomeScreen.kt
package com.example.weddingplanner.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.weddingplanner.ui.theme.*
import com.example.weddingplanner.viewmodel.AuthViewModel

data class MenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val route: String,
    val color: androidx.compose.ui.graphics.Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    val menuItems = remember {
        listOf(
            MenuItem(
                title = "Wedding Checklist",
                subtitle = "Track your wedding planning tasks",
                icon = Icons.Default.CheckCircle,
                route = "checklist",
                color = Pink40
            ),
            MenuItem(
                title = "Venues & Hotels",
                subtitle = "Find perfect venues for your special day",
                icon = Icons.Default.LocationOn,
                route = "venues",
                color = DeepRose)) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Pink40.copy(alpha = 0.1f),
                        RoseGold.copy(alpha = 0.2f),
                        LightRose.copy(alpha = 0.3f))))) {
        TopAppBar(
            title = { Text("Wedding Planner") },
            actions = {
                IconButton(
                    onClick = {
                        authViewModel.logout()
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true } } }) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Pink40,
                titleContentColor = White,
                actionIconContentColor = White))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💐",
                        fontSize = 48.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Welcome ${currentUser?.name ?: "Guest"}!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Pink40,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Let's plan your perfect wedding together",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)) } }
            Text(
                text = "Planning Tools",
                style = MaterialTheme.typography.headlineSmall,
                color = Pink40,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            menuItems.forEach { item ->
                MenuCard(
                    item = item,
                    onClick = { navController.navigate(item.route) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Champagne.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Wedding Planning Made Easy",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepRose,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "From checklist to venues, we've got everything covered for your special day!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)) } } } } }
@Composable
private fun MenuCard(
    item: MenuItem,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "scale"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isPressed) item.color.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 100),
        label = "color"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(item.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)) }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = item.color,
                modifier = Modifier.size(24.dp)) } } }