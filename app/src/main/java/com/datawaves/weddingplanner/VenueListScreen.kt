// ui/screens/VenueListScreen.kt
package com.example.weddingplanner.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.weddingplanner.data.models.Venue
import com.example.weddingplanner.ui.theme.*
import com.example.weddingplanner.viewmodel.VenueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenueListScreen(
    navController: NavController,
    viewModel: VenueViewModel
) {
    val filteredVenues by viewModel.filteredVenues.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()
    val showFilters by viewModel.showFilters.collectAsStateWithLifecycle()
    val budgetRange by viewModel.budgetRange.collectAsStateWithLifecycle()
    val capacityRange by viewModel.capacityRange.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val locations = remember { viewModel.getLocations() }

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
            title = { Text("Wedding Venues") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { viewModel.toggleFilters() }) {
                    Icon(
                        if (showFilters) Icons.Default.FilterListOff else Icons.Default.FilterList,
                        contentDescription = if (showFilters) "Hide Filters" else "Show Filters"
                    )
                }
                IconButton(onClick = { viewModel.clearFilters() }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Filters")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Pink40,
                titleContentColor = White,
                navigationIconContentColor = White,
                actionIconContentColor = White))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Search venues...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear") } } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Pink40,
                    focusedLabelColor = Pink40))

            // Location Filter
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(locations) { location ->
                    FilterChip(
                        onClick = { viewModel.setSelectedLocation(location) },
                        label = { Text(location) },
                        selected = location == selectedLocation,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Pink40,
                            selectedLabelColor = White)) } }
            // Advanced Filters
            AnimatedVisibility(
                visible = showFilters,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Advanced Filters",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Pink40,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Budget Range
                        Text(
                            text = "Budget Range: ₹${budgetRange.first/1000}k - ₹${budgetRange.second/1000}k",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        RangeSlider(
                            value = budgetRange.first.toFloat()..budgetRange.second.toFloat(),
                            onValueChange = { range ->
                                viewModel.setBudgetRange(range.start.toInt(), range.endInclusive.toInt())
                            },
                            valueRange = 50000f..1000000f,
                            steps = 20,
                            colors = SliderDefaults.colors(
                                thumbColor = Pink40,
                                activeTrackColor = Pink40
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Capacity: ${capacityRange.first} - ${capacityRange.second} guests",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        RangeSlider(
                            value = capacityRange.first.toFloat()..capacityRange.second.toFloat(),
                            onValueChange = { range ->
                                viewModel.setCapacityRange(range.start.toInt(), range.endInclusive.toInt())
                            },
                            valueRange = 50f..1000f,
                            steps = 20,
                            colors = SliderDefaults.colors(
                                thumbColor = Pink40,
                                activeTrackColor = Pink40)) } } }
            Text(
                text = "${filteredVenues.size} venues found",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Venues List
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Pink40)
                }
            } else if (filteredVenues.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🏰",
                            fontSize = 48.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "No venues found",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Try adjusting your filters",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)) } }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = filteredVenues,
                        key = { it.id }
                    ) { venue ->
                        VenueCard(
                            venue = venue,
                            onClick = {
                            }) } } } } } }

@Composable
private fun VenueCard(
    venue: Venue,
    onClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .animateContentSize(
                animationSpec = tween(durationMillis = 300)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = venue.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Pink40
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = DeepRose,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = venue.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant) } }

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Warning,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = venue.rating.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface) } }

            Spacer(modifier = Modifier.height(12.dp))

            // Price and Capa
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Price Range",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = venue.priceRange,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Success
                    )
                }

                Column {
                    Text(
                        text = "Capacity",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = venue.capacity,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface) } }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = venue.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )

            // Amenities
            if (venue.amenities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(venue.amenities.take(if (expanded) venue.amenities.size else 3)) { amenity ->
                        Box(
                            modifier = Modifier
                                .background(
                                    RoseGold.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = amenity,
                                style = MaterialTheme.typography.labelSmall,
                                color = DeepRose) } }

                    if (!expanded && venue.amenities.size > 3) {
                        item {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Pink40.copy(alpha = 0.2f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+${venue.amenities.size - 3} more",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Pink40) } } } } }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Contact Information",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Pink40,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (venue.contactNumber.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = null,
                                tint = DeepRose,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = venue.contactNumber,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface) } }
                    if (venue.email.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = DeepRose,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = venue.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface) } } } }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { expanded = !expanded }
                ) {
                    Text(
                        text = if (expanded) "Show Less" else "Show More",
                        color = Pink40
                    )
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Pink40,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Row {
                    OutlinedButton(
                        onClick = {  },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Pink40
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Contact") }
                    Button(
                        onClick = {  },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Pink40)) {
                        Text("Book Now") } } } } } }