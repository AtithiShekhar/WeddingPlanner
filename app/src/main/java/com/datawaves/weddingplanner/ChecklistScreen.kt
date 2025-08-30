package com.example.weddingplanner.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.weddingplanner.data.models.ChecklistItem
import com.example.weddingplanner.data.models.Priority
import com.example.weddingplanner.ui.theme.*
import com.example.weddingplanner.viewmodel.ChecklistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(
    navController: NavController,
    viewModel: ChecklistViewModel
) {
    val filteredItems by viewModel.filteredItems.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val categories = remember(filteredItems) { viewModel.getCategories() }
    val (completed, total) = remember(filteredItems) { viewModel.getCompletionStats() }

    var showAddDialog by remember { mutableStateOf(false) }

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
            title = { Text("Wedding Checklist") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Pink40,
                titleContentColor = White,
                navigationIconContentColor = White,
                actionIconContentColor = White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Progress Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Pink40
                        )

                        Text(
                            text = "$completed/$total",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepRose,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = if (total > 0) completed.toFloat() / total else 0f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Pink40,
                        trackColor = Pink40.copy(alpha = 0.2f)
                    )

                    if (completed == total && total > 0) {
                        Text(
                            text = "🎉 Congratulations! All tasks completed!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Success,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 8.dp)) } } }
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Search tasks...") },
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

            // Category
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        onClick = { viewModel.setSelectedCategory(category) },
                        label = { Text(category) },
                        selected = category == selectedCategory,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Pink40,
                            selectedLabelColor = White)) } }

            // Tasks List
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Pink40)
                }
            } else if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📝",
                            fontSize = 48.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "No tasks found",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Add your first wedding planning task!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)) } }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = filteredItems,
                        key = { it.id }
                    ) { item ->
                        ChecklistItemCard(
                            item = item,
                            onToggle = { viewModel.toggleItemCompletion(item.id) },
                            onDelete = { viewModel.deleteChecklistItem(item.id) }) } } } } }

    // Add Task
    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title, description, category, priority ->
                viewModel.addChecklistItem(title, description, category, priority)
                showAddDialog = false }) } }

@Composable
private fun ChecklistItemCard(
    item: ChecklistItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val priorityColor = when (item.priority) {
        Priority.HIGH -> Error
        Priority.MEDIUM -> Warning
        Priority.LOW -> Success
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted)
                Success.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Pink40
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null,
                        color = if (item.isCompleted)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Priority Indicator
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(priorityColor)) }

                if (item.description.isNotEmpty()) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    ) }

                // Category Tag
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = Pink40,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .background(
                            Pink40.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)) }

            // Delete
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Error) } } } }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Priority) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    val categories = listOf("General", "Venue", "Photography", "Catering", "Ceremonies", "Travel", "Invitations", "Attire", "Decoration", "Transportation")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add New Task",
                color = Pink40,
                fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Pink40,
                        focusedLabelColor = Pink40))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Pink40,
                        focusedLabelColor = Pink40))

                // Category
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            onClick = { category = cat },
                            label = { Text(cat) },
                            selected = cat == category,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Pink40,
                                selectedLabelColor = White)) } }
                // Priority
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Priority.values().forEach { p ->
                        FilterChip(
                            onClick = { priority = p },
                            label = { Text(p.name) },
                            selected = p == priority,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = when (p) {
                                    Priority.HIGH -> Error
                                    Priority.MEDIUM -> Warning
                                    Priority.LOW -> Success
                                },
                                selectedLabelColor = White)) } } } },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title.trim(), description.trim(), category, priority)
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Pink40)
            ) {
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Pink40)
            }
        }
    )
}