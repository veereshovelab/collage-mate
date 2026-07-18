package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ResourceMaterial
import com.example.ui.CampusViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val user by viewModel.currentUser.collectAsState()
    val resources by viewModel.resources.collectAsState()
    val unlockedIds by viewModel.unlockedResourceIds.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showUploadDialog by remember { mutableStateOf(false) }
    var selectedFilterCategory by remember { mutableStateOf("All") }

    val filterChips = listOf("All", "CS201", "MATH302", "CHEM101", "Notes", "Study Guide", "Cheat Sheet")

    val filteredResources = resources.filter { item ->
        when (selectedFilterCategory) {
            "All" -> true
            "CS201" -> item.courseCode.contains("CS201", ignoreCase = true)
            "MATH302" -> item.courseCode.contains("MATH302", ignoreCase = true)
            "CHEM101" -> item.courseCode.contains("CHEM101", ignoreCase = true)
            "Notes" -> item.fileType.contains("Notes", ignoreCase = true)
            "Study Guide" -> item.fileType.contains("Guide", ignoreCase = true)
            "Cheat Sheet" -> item.fileType.contains("Cheat", ignoreCase = true)
            else -> true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Resource Library",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp),
                            color = BentoTextMain
                        )
                        Card(
                            colors = CardDefaults.cardColors(containerColor = BentoLavenderContainer),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "${user?.points ?: 0} CC",
                                color = BentoLavenderContent,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BentoBackground,
                    titleContentColor = BentoTextMain
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showUploadDialog = true },
                containerColor = BentoLavenderContent,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .testTag("upload_notes_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Upload Note")
            }
        },
        containerColor = BentoBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input Field - Bento Styled Round Box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search course, prof, or title...", color = BentoTextSecondary.copy(alpha = 0.6f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = BentoTextSecondary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Cancel, contentDescription = "Clear", tint = BentoTextSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = BentoTextMain,
                    unfocusedTextColor = BentoTextMain,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = BentoBorder,
                    unfocusedBorderColor = BentoBorder.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("search_bar")
            )

            // Horizontal Category Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterChips) { chip ->
                    val isSelected = selectedFilterCategory == chip
                    Card(
                        modifier = Modifier
                            .clickable { selectedFilterCategory = chip }
                            .testTag("filter_chip_$chip")
                            .then(
                                if (!isSelected) Modifier.border(1.dp, BentoBorder.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                else Modifier
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) BentoLilacContainer else Color.White
                        )
                    ) {
                        Text(
                            text = chip,
                            color = if (isSelected) BentoLilacContent else BentoTextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable library items list
            if (filteredResources.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(BentoLavenderContainer, shape = RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.MenuBook,
                                contentDescription = "Empty Library",
                                tint = BentoLavenderContent,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Study Materials Found",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = BentoTextMain
                        )
                        Text(
                            text = "Try adjusting your search criteria, selecting a different filter chip, or be the first to upload reference study files!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = BentoTextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredResources) { resource ->
                        ResourceCardItem(
                            resource = resource,
                            isUnlocked = unlockedIds.contains(resource.id) || resource.uploaderEmail == user?.email,
                            isSelfUploaded = resource.uploaderEmail == user?.email,
                            onPurchase = {
                                viewModel.purchaseResource(resource) { success, msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            },
                            onDownloadSimulate = {
                                Toast.makeText(context, "Simulating secure vault download for ${resource.title}...", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        // Upload Materials Modal Dialog
        if (showUploadDialog) {
            UploadResourceDialog(
                onDismiss = { showUploadDialog = false },
                onUpload = { title, course, prof, sem, desc, type, price ->
                    viewModel.addResource(title, course, prof, sem, desc, type, price)
                    Toast.makeText(context, "Successfully listed study notes on the marketplace!", Toast.LENGTH_SHORT).show()
                    showUploadDialog = false
                }
            )
        }
    }
}

@Composable
fun ResourceCardItem(
    resource: ResourceMaterial,
    isUnlocked: Boolean,
    isSelfUploaded: Boolean,
    onPurchase: () -> Unit,
    onDownloadSimulate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BentoBorder.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            .testTag("resource_card_${resource.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1.4f)) {
                    // Title
                    Text(
                        text = resource.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoTextMain,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Prof. ${resource.professor} • ${resource.semester}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoTextSecondary
                    )
                }

                // Course Tag & Price Code - Bento style colors
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1.1f)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BentoBlueContainer),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = resource.courseCode.uppercase(),
                            color = BentoBlueContent,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = BentoPinkContainer),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = resource.fileType.uppercase(),
                            color = BentoPinkContent,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = resource.description,
                style = MaterialTheme.typography.bodyMedium,
                color = BentoTextMain.copy(alpha = 0.8f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Uploader identifier
                Column {
                    Text(
                        text = "Shared by ${resource.uploaderName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoTextSecondary
                    )
                    if (isSelfUploaded) {
                        Text(
                            text = "Your Listing",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = BentoLavenderContent
                        )
                    }
                }

                // Unlock/Purchase Button
                if (isUnlocked) {
                    Button(
                        onClick = onDownloadSimulate,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E), contentColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open PDF", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Button(
                        onClick = onPurchase,
                        colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderContainer, contentColor = BentoLavenderContent),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag("purchase_button_${resource.id}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Icon(Icons.Default.LockOpen, contentDescription = "Unlock", tint = BentoLavenderContent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Unlock for ${resource.priceInPoints} CC", color = BentoLavenderContent, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UploadResourceDialog(
    onDismiss: () -> Unit,
    onUpload: (String, String, String, String, String, String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }
    var professor by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var fileType by remember { mutableStateOf("Notes") }
    var priceInPoints by remember { mutableStateOf("15") }

    val fileTypes = listOf("Notes", "Study Guide", "Cheat Sheet", "Exam Prep")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .border(1.dp, BentoBorder, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Share Study Guide/Notes",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp),
                    color = BentoTextMain
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Material Title (e.g. CS201 Midterm Summary)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = BentoTextMain,
                        unfocusedTextColor = BentoTextMain,
                        focusedBorderColor = BentoLavenderContent,
                        unfocusedBorderColor = BentoBorder,
                        focusedLabelColor = BentoLavenderContent,
                        unfocusedLabelColor = BentoTextSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_title_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = courseCode,
                        onValueChange = { courseCode = it },
                        label = { Text("Course Code") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = BentoTextMain,
                            unfocusedTextColor = BentoTextMain,
                            focusedBorderColor = BentoLavenderContent,
                            unfocusedBorderColor = BentoBorder,
                            focusedLabelColor = BentoLavenderContent,
                            unfocusedLabelColor = BentoTextSecondary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_course_input")
                    )

                    OutlinedTextField(
                        value = professor,
                        onValueChange = { professor = it },
                        label = { Text("Professor") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = BentoTextMain,
                            unfocusedTextColor = BentoTextMain,
                            focusedBorderColor = BentoLavenderContent,
                            unfocusedBorderColor = BentoBorder,
                            focusedLabelColor = BentoLavenderContent,
                            unfocusedLabelColor = BentoTextSecondary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_professor_input")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = semester,
                        onValueChange = { semester = it },
                        label = { Text("Semester (e.g. Fall '26)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = BentoTextMain,
                            unfocusedTextColor = BentoTextMain,
                            focusedBorderColor = BentoLavenderContent,
                            unfocusedBorderColor = BentoBorder,
                            focusedLabelColor = BentoLavenderContent,
                            unfocusedLabelColor = BentoTextSecondary
                        ),
                        modifier = Modifier
                            .weight(1.1f)
                            .testTag("dialog_semester_input")
                    )

                    OutlinedTextField(
                        value = priceInPoints,
                        onValueChange = { priceInPoints = it },
                        label = { Text("Price (CC)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = BentoTextMain,
                            unfocusedTextColor = BentoTextMain,
                            focusedBorderColor = BentoLavenderContent,
                            unfocusedBorderColor = BentoBorder,
                            focusedLabelColor = BentoLavenderContent,
                            unfocusedLabelColor = BentoTextSecondary
                        ),
                        modifier = Modifier
                            .weight(0.9f)
                            .testTag("dialog_price_input")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Document Classification", style = MaterialTheme.typography.bodySmall, color = BentoTextSecondary)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    fileTypes.forEach { type ->
                        val isSelected = fileType == type
                        Card(
                            modifier = Modifier
                                .clickable { fileType = type }
                                .weight(1f)
                                .then(
                                    if (!isSelected) Modifier.border(1.dp, BentoBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                    else Modifier
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) BentoLilacContainer else Color.White
                            )
                        ) {
                            Text(
                                text = type,
                                color = if (isSelected) BentoLilacContent else BentoTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Brief Description of Contents") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = BentoTextMain,
                        unfocusedTextColor = BentoTextMain,
                        focusedBorderColor = BentoLavenderContent,
                        unfocusedBorderColor = BentoBorder,
                        focusedLabelColor = BentoLavenderContent,
                        unfocusedLabelColor = BentoTextSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .testTag("dialog_desc_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = BentoTextSecondary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val priceInt = priceInPoints.toIntOrNull() ?: 15
                            if (title.isNotBlank() && courseCode.isNotBlank()) {
                                onUpload(title, courseCode, professor, semester, description, fileType, priceInt)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderContent, contentColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag("dialog_submit_button")
                    ) {
                        Text("Share to Library", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
