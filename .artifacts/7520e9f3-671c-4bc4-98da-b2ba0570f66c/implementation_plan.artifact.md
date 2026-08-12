# Implementation Plan - Navigation, Student DM, and Glassmorphism Update

This plan outlines the steps to improve navigation, add a Direct Messaging feature for students, and overhaul the UI with a modern Glassmorphism aesthetic.

## User Review Required

> [!IMPORTANT]
> **User Role Implementation**: I will add a `role` field to the `User` entity. If there's an existing role system I missed, please let me know.
> **Navigation Overhaul**: I am migrating manual tab switching to `androidx.navigation:navigation-compose` for proper backstack handling.
> **API Level**: Glassmorphism blur effects will use `Modifier.blur` (API 31+). A subtle translucent fallback will be provided for older versions.

## Proposed Changes

### 1. Dependency and Core Data Models
* **Modify** `app/build.gradle.kts`: Enable `androidx.navigation.compose`.
* **Modify** `Entities.kt`:
    * Add `role` field to `User`.
    * Add `DirectMessage` and `ChatMessage` data models.

### 2. Navigation Fix (Task 1)
* **Modify** `MainActivity.kt`:
    * Replace `currentTab` state with `NavController`.
    * Implement `NavHost` with routes for all existing screens.
    * Ensure `NavigationBar` interacts correctly with `NavController`.
    * Fix back navigation using standard Compose Navigation patterns.

### 3. Student DM Feature (Task 2)
* **New** `MessageInboxScreen.kt`: UI for list of active conversations.
* **New** `ChatScreen.kt`: UI for individual messages.
* **Modify** `MainActivity.kt`: Add "Messages" tab to the Navigation Bar, visible only for users with the 'Student' role.
* **Modify** `CampusViewModel.kt`: Add placeholder methods for fetching messages and sending them.

### 4. Glass Fluid Aesthetic (Task 3)
* **Modify** `ui/theme/Color.kt`: Add glass-specific translucent colors.
* **Modify** `ui/theme/Theme.kt`: Define a global `GlassStyle` and update `MaterialTheme`.
* **New** `components/GlassComponents.kt`:
    * `GlassCard`: A reusable container with blur and gradient border.
    * `GlassModifier`: A custom modifier for applying glass effects.
* **Modify** `MainActivity.kt`: Update `AnimatedContent` for smoother "fluid" transitions.
* **Modify** Existing Screens: Update major UI panels to use `GlassCard`.

## Verification Plan

### Automated Tests
* Run existing unit tests to ensure no regressions in data logic.
* Verify build completion after dependency changes.

### Manual Verification
* Deploy to an emulator/device.
* Test back navigation from "Edit Profile" and "College Hub".
* Verify the "Messages" tab appears/disappears based on user role.
* Inspect UI for translucency, blur, and smooth transitions.
