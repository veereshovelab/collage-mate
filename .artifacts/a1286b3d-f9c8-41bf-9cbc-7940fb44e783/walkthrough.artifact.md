# Walkthrough - New Assignment Section

I have added a dedicated **Assignment Vault** section to the ClgMate app. This allows students to share, find, and unlock assignment resources specifically.

## Changes Made

### 1. Dedicated Assignment Screen
- Created [AssignmentScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/AssignmentScreen.kt), a specialized UI for browsing and uploading assignment-related documents.
- The screen features a unique "Bento Pink" theme to distinguish it from the general resource library.

### 2. Navigation Integration
- Added a new **Asgn** tab to the bottom navigation bar in [MainActivity.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/MainActivity.kt).
- Integrated smooth transitions when navigating to the Assignment Vault.

### 3. Data & Backend
- Updated [CampusViewModel.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/CampusViewModel.kt) to seed the database with initial assignment samples (e.g., CS 102 Lab, ECON 101 Problem Set).
- Updated [MarketplaceScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/MarketplaceScreen.kt) to include "Assignment" in its classification filters and upload options.

## Verification Results

### Build Success
The project builds successfully with the new section.
```bash
$ ./gradlew :app:assembleDebug
BUILD SUCCESSFUL
```

### Manual Verification
- Navigate to the **Asgn** tab in the bottom bar.
- See the seeded assignments from "Alice Adams" and "Charlie Chen".
- Try uploading a new assignment using the `+` Floating Action Button.
- Verify that assignments are unlockable using Campus Credits (CC).
