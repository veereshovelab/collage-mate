# Implementation Plan - Make Profile Logo Clickable and Navigate to Profile

The user wants to make the profile logo on the first page (feed) clickable and redirect the user to the profile page.

## Proposed Changes

### UI Screens

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/HomeScreen.kt)
- Add `onProfileClick: () -> Unit` parameter to the `HomeScreen` composable.
- Make the profile avatar in the `TopAppBar` clickable by adding a `clickable` modifier to the `Box`.
- Make the profile avatar in the "Write Post" section clickable as well.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/MainActivity.kt)
- Pass a lambda to `HomeScreen` that updates the `currentTab` state to `CampusTab.PROFILE`.
- Implement `AnimatedContent` for tab switching to provide a smooth "attached" transition between screens, addressing the "with attachment" part of the request as a smooth visual transition.

## Verification Plan

### Manual Verification
- Deploy the app to a device or emulator.
- On the Home (Feed) screen, tap the profile avatar in the top-right corner of the `TopAppBar`.
- Verify that the app switches to the Profile tab.
- Tap the profile avatar next to the "Share an update..." input box.
- Verify that the app switches to the Profile tab.
- Observe the smooth transition between tabs.
