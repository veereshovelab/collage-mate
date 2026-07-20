package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ResourceMaterial
import com.example.ui.CampusViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollegeHubScreen(
    viewModel: CampusViewModel,
    collegeName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val resources by viewModel.selectedCollegeResources.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Courses", "Notes", "Assignments")

    LaunchedEffect(collegeName) {
        viewModel.selectCollege(collegeName)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = collegeName,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = BentoTextMain
                        )
                        Text(
                            text = "Institutional Hub",
                            style = MaterialTheme.typography.labelSmall,
                            color = BentoTextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BentoBackground)
            )
        },
        containerColor = BentoBackground
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = BentoBackground,
                contentColor = BentoLavenderContent,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BentoLavenderContent
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                style = if (selectedTab == index)
                                    MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                else
                                    MaterialTheme.typography.bodyMedium
                            )
                        }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                when (selectedTab) {
                    0 -> CoursesList(resources)
                    1 -> NotesList(resources)
                    2 -> AssignmentsList(resources)
                }
            }
        }
    }
}

@Composable
fun CoursesList(resources: List<ResourceMaterial>) {
    val uniqueCourses = resources.map { it.courseCode }.distinct()

    if (uniqueCourses.isEmpty()) {
        EmptyState("No courses listed for this college yet.", Icons.Default.School)
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(uniqueCourses) { courseCode ->
                ResourceSummaryCard(
                    title = courseCode.uppercase(),
                    subtitle = "Multiple resources available",
                    icon = Icons.Default.School,
                    containerColor = BentoBlueContainer,
                    contentColor = BentoBlueContent
                )
            }
        }
    }
}

@Composable
fun NotesList(resources: List<ResourceMaterial>) {
    val notes = resources.filter { it.fileType != "Assignment" }

    if (notes.isEmpty()) {
        EmptyState("No study notes shared yet.", Icons.AutoMirrored.Filled.MenuBook)
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(notes) { note ->
                ResourceSummaryCard(
                    title = note.title,
                    subtitle = "${note.courseCode} • ${note.uploaderName}",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    containerColor = BentoLavenderContainer,
                    contentColor = BentoLavenderContent
                )
            }
        }
    }
}

@Composable
fun AssignmentsList(resources: List<ResourceMaterial>) {
    val assignments = resources.filter { it.fileType == "Assignment" }

    if (assignments.isEmpty()) {
        EmptyState("No assignments shared yet.", Icons.Default.Assignment)
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(assignments) { assignment ->
                ResourceSummaryCard(
                    title = assignment.title,
                    subtitle = "${assignment.courseCode} • ${assignment.uploaderName}",
                    icon = Icons.Default.Assignment,
                    containerColor = BentoPinkContainer,
                    contentColor = BentoPinkContent
                )
            }
        }
    }
}

@Composable
fun ResourceSummaryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BentoBorder.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(containerColor, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = BentoTextMain)
                Text(subtitle, style = MaterialTheme.typography.labelSmall, color = BentoTextSecondary)
            }
        }
    }
}

@Composable
fun EmptyState(message: String, icon: ImageVector) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(48.dp), tint = BentoBorder)
            Spacer(modifier = Modifier.height(12.dp))
            Text(message, style = MaterialTheme.typography.bodyMedium, color = BentoTextSecondary)
        }
    }
}
