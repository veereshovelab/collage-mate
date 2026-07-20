package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.CampusViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val user by viewModel.currentUser.collectAsState()

    var name by remember { mutableStateOf(user?.name ?: "") }
    var bio by remember { mutableStateOf(user?.bio ?: "") }
    var collegeCourse by remember { mutableStateOf(user?.collegeCourse ?: "") }
    var phoneNumber by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var socialLink by remember { mutableStateOf(user?.socialLink ?: "") }
    var appearance by remember { mutableStateOf(user?.appearance ?: "System") }
    var profilePictureUri by remember { mutableStateOf(user?.profilePictureUri) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                // In a real app, we might need to take persistable URI permission
                // or copy the file to internal storage.
                profilePictureUri = it.toString()
            }
        }
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp),
                        color = BentoTextMain
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BentoTextMain)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val updatedUser = user?.copy(
                                name = name,
                                bio = bio,
                                collegeCourse = collegeCourse,
                                phoneNumber = phoneNumber,
                                socialLink = socialLink,
                                appearance = appearance,
                                profilePictureUri = profilePictureUri
                            )
                            if (updatedUser != null) {
                                viewModel.updateUserProfile(updatedUser)
                                onBack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save", tint = BentoLavenderContent)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BentoBackground,
                    titleContentColor = BentoTextMain
                )
            )
        },
        containerColor = BentoBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture Section
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(BentoLavenderContainer)
                    .border(2.dp, BentoBorder.copy(alpha = 0.5f), CircleShape)
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (profilePictureUri != null) {
                    AsyncImage(
                        model = profilePictureUri,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Pick Image", tint = BentoLavenderContent, modifier = Modifier.size(32.dp))
                        Text("Change", style = MaterialTheme.typography.labelSmall, color = BentoLavenderContent)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            EditField(label = "Full Name", value = name, onValueChange = { name = it })
            EditField(label = "Bio", value = bio, onValueChange = { bio = it }, singleLine = false, minLines = 3)
            EditField(label = "College Course", value = collegeCourse, onValueChange = { collegeCourse = it })
            EditField(label = "Phone Number", value = phoneNumber, onValueChange = { phoneNumber = it })
            EditField(label = "Social Link (LinkedIn/GitHub)", value = socialLink, onValueChange = { socialLink = it })

            Spacer(modifier = Modifier.height(16.dp))

            // Appearance Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoBorder.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Appearance",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoTextMain
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Light", "Dark", "System").forEach { mode ->
                            FilterChip(
                                selected = appearance == mode,
                                onClick = { appearance = mode },
                                label = { Text(mode) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BentoLavenderContainer,
                                    selectedLabelColor = BentoLavenderContent
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Read-only Login Info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoBorder.copy(alpha = 0.3f), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoBackground.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Login Info (Read-only)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = user?.email ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BentoTextMain
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = BentoTextSecondary,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BentoLavenderContent,
                unfocusedBorderColor = BentoBorder,
                focusedLabelColor = BentoLavenderContent,
                cursorColor = BentoLavenderContent
            ),
            singleLine = singleLine,
            minLines = minLines
        )
    }
}
