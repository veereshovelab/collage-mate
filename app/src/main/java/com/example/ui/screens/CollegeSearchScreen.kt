package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CampusViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollegeSearchScreen(
    viewModel: CampusViewModel,
    onCollegeSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.collegeSearchQuery.collectAsState()
    val allColleges by viewModel.collegeNames.collectAsState()

    val filteredColleges = remember(searchQuery, allColleges) {
        if (searchQuery.isBlank()) {
            allColleges
        } else {
            allColleges.filter { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BentoBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Explore Campuses",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            ),
            color = BentoTextMain,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Discover colleges and access their specific notes and resources.",
            style = MaterialTheme.typography.bodyMedium,
            color = BentoTextSecondary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setCollegeSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            placeholder = { Text("Search for colleges, universities...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BentoTextSecondary) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BentoLavenderContent,
                unfocusedBorderColor = BentoBorder,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        if (filteredColleges.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.School, 
                        contentDescription = null, 
                        modifier = Modifier.size(64.dp),
                        tint = BentoBorder
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No colleges found matching your search.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BentoTextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredColleges) { collegeName ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCollegeSelect(collegeName) }
                            .border(1.dp, BentoBorder.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(BentoLavenderContainer, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = BentoLavenderContent)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = collegeName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BentoTextMain
                                )
                            }
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward, 
                                contentDescription = null, 
                                tint = BentoBorder
                            )
                        }
                    }
                }
            }
        }
    }
}
