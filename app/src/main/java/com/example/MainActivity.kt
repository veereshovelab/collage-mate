package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.CampusViewModel
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: CampusViewModel = viewModel()
        MainAppContent(viewModel = viewModel)
      }
    }
  }
}

enum class CampusTab {
  FEED, ASSIGNMENTS, MARKETPLACE, GIGS, PROFILE, EDIT_PROFILE, COLLEGE_SEARCH, COLLEGE_HUB
}

@Composable
fun MainAppContent(viewModel: CampusViewModel) {
  val currentUser by viewModel.currentUser.collectAsState()
  var currentTab by remember { mutableStateOf(CampusTab.FEED) }
  var selectedCollegeName by remember { mutableStateOf("") }

  if (currentUser == null) {
    LoginScreen(viewModel = viewModel)
  } else {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = BentoBackground,
      bottomBar = {
        // Only show bottom navigation on main tabs
        val mainTabs = listOf(CampusTab.FEED, CampusTab.ASSIGNMENTS, CampusTab.MARKETPLACE, CampusTab.GIGS, CampusTab.PROFILE)
        if (currentTab in mainTabs) {
          NavigationBar(
            containerColor = BentoNavBg,
            contentColor = BentoTextMain
          ) {
            NavigationBarItem(
              selected = currentTab == CampusTab.FEED,
              onClick = { currentTab = CampusTab.FEED },
              icon = { Icon(Icons.Default.Campaign, contentDescription = "Feed") },
              label = { Text("Feed", fontWeight = if (currentTab == CampusTab.FEED) FontWeight.Bold else FontWeight.Medium) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BentoLilacContent,
                selectedTextColor = BentoLilacContent,
                unselectedIconColor = BentoTextSecondary,
                unselectedTextColor = BentoTextSecondary,
                indicatorColor = BentoLilacContainer
              ),
              modifier = Modifier.testTag("tab_feed")
            )
            NavigationBarItem(
              selected = currentTab == CampusTab.ASSIGNMENTS,
              onClick = { currentTab = CampusTab.ASSIGNMENTS },
              icon = { Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = "Assignments") },
              label = { Text("Asgn", fontWeight = if (currentTab == CampusTab.ASSIGNMENTS) FontWeight.Bold else FontWeight.Medium) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BentoLilacContent,
                selectedTextColor = BentoLilacContent,
                unselectedIconColor = BentoTextSecondary,
                unselectedTextColor = BentoTextSecondary,
                indicatorColor = BentoLilacContainer
              ),
              modifier = Modifier.testTag("tab_assignments")
            )
            NavigationBarItem(
              selected = currentTab == CampusTab.MARKETPLACE,
              onClick = { currentTab = CampusTab.MARKETPLACE },
              icon = { Icon(Icons.Default.MenuBook, contentDescription = "Marketplace") },
              label = { Text("Market", fontWeight = if (currentTab == CampusTab.MARKETPLACE) FontWeight.Bold else FontWeight.Medium) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BentoLilacContent,
                selectedTextColor = BentoLilacContent,
                unselectedIconColor = BentoTextSecondary,
                unselectedTextColor = BentoTextSecondary,
                indicatorColor = BentoLilacContainer
              ),
              modifier = Modifier.testTag("tab_marketplace")
            )
            NavigationBarItem(
              selected = currentTab == CampusTab.GIGS,
              onClick = { currentTab = CampusTab.GIGS },
              icon = { Icon(Icons.Default.Handshake, contentDescription = "Gig Board") },
              label = { Text("Gigs", fontWeight = if (currentTab == CampusTab.GIGS) FontWeight.Bold else FontWeight.Medium) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BentoLilacContent,
                selectedTextColor = BentoLilacContent,
                unselectedIconColor = BentoTextSecondary,
                unselectedTextColor = BentoTextSecondary,
                indicatorColor = BentoLilacContainer
              ),
              modifier = Modifier.testTag("tab_gigs")
            )
            NavigationBarItem(
              selected = currentTab == CampusTab.PROFILE,
              onClick = { currentTab = CampusTab.PROFILE },
              icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
              label = { Text("Profile", fontWeight = if (currentTab == CampusTab.PROFILE) FontWeight.Bold else FontWeight.Medium) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BentoLilacContent,
                selectedTextColor = BentoLilacContent,
                unselectedIconColor = BentoTextSecondary,
                unselectedTextColor = BentoTextSecondary,
                indicatorColor = BentoLilacContainer
              ),
              modifier = Modifier.testTag("tab_profile")
            )
          }
        }
      }
    ) { innerPadding ->
      val modifier = Modifier.padding(innerPadding)
      AnimatedContent(
        targetState = currentTab,
        label = "tab_transition",
        transitionSpec = {
          fadeIn().togetherWith(fadeOut())
        }
      ) { targetTab ->
        when (targetTab) {
          CampusTab.FEED -> HomeScreen(
            viewModel = viewModel,
            modifier = modifier,
            onProfileClick = { currentTab = CampusTab.PROFILE }
          )
          CampusTab.ASSIGNMENTS -> AssignmentScreen(viewModel = viewModel, modifier = modifier)
          CampusTab.MARKETPLACE -> MarketplaceScreen(viewModel = viewModel, modifier = modifier)
          CampusTab.GIGS -> GigScreen(viewModel = viewModel, modifier = modifier)
          CampusTab.PROFILE -> ProfileScreen(
            viewModel = viewModel,
            onEditClick = { currentTab = CampusTab.EDIT_PROFILE },
            modifier = modifier
          )
          CampusTab.EDIT_PROFILE -> EditProfileScreen(
            viewModel = viewModel,
            onBack = { currentTab = CampusTab.PROFILE },
            modifier = modifier
          )
          CampusTab.COLLEGE_SEARCH -> CollegeSearchScreen(
            viewModel = viewModel,
            onCollegeSelect = { name ->
              selectedCollegeName = name
              currentTab = CampusTab.COLLEGE_HUB
            },
            modifier = modifier
          )
          CampusTab.COLLEGE_HUB -> CollegeHubScreen(
            viewModel = viewModel,
            collegeName = selectedCollegeName,
            onBack = { currentTab = CampusTab.COLLEGE_SEARCH },
            modifier = modifier
          )
        }
      }
    }
  }
}
