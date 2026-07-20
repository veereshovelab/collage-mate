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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.CampusViewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MarketplaceScreen
import com.example.ui.screens.GigScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.AssignmentScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.BentoBackground
import com.example.ui.theme.BentoNavBg
import com.example.ui.theme.BentoLilacContainer
import com.example.ui.theme.BentoLilacContent
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.theme.BentoTextMain
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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
  FEED, ASSIGNMENTS, MARKETPLACE, GIGS, PROFILE
}

@Composable
fun MainAppContent(viewModel: CampusViewModel) {
  val currentUser by viewModel.currentUser.collectAsState()
  var currentTab by remember { mutableStateOf(CampusTab.FEED) }

  if (currentUser == null) {
    LoginScreen(viewModel = viewModel)
  } else {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = BentoBackground,
      bottomBar = {
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
          CampusTab.PROFILE -> ProfileScreen(viewModel = viewModel, modifier = modifier)
        }
      }
    }
  }
}
