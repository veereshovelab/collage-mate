# Implementation Plan - Add Assignment Section to ClgMate

The user wants to add an "Assignment" section to the app where students can share and unlock assignment resources using Campus Credits (CC).

## Proposed Changes

### Data Layer

#### [MODIFY] [Entities.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/data/model/Entities.kt)
- No changes needed to the entity itself, as `ResourceMaterial` is sufficient to store assignment data.

#### [MODIFY] [CampusViewModel.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/CampusViewModel.kt)
- Update `seedDatabaseIfEmpty` to include some initial assignment resources.
- Ensure the `addResource` logic accommodates the new "Assignment" type.

### UI Layer

#### [MODIFY] [MainActivity.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/MainActivity.kt)
- Add `ASSIGNMENTS` to the `CampusTab` enum.
- Add an `Assignment` tab to the `NavigationBar` (using `Icons.Default.Assignment` or similar).
- Update the `AnimatedContent` block to include the new `AssignmentScreen`.

#### [NEW] [AssignmentScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/AssignmentScreen.kt)
- Create a new screen specifically for assignments.
- It will leverage the same `purchaseResource` and `addResource` logic from the `CampusViewModel`.
- It will filter `ResourceMaterial` to only show those with `fileType == "Assignment"`.
- It will have a specialized upload dialog pre-configured for assignments.

#### [MODIFY] [MarketplaceScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/MarketplaceScreen.kt)
- Update the `fileTypes` list to include "Assignment" as an option, so users can also find them in the general marketplace if they search there.

## Verification Plan

### Automated Tests
- Run `:app:assembleDebug` to ensure the project builds with the new screen and tab.

### Manual Verification
- Deploy the app and verify the "Assignment" tab is visible in the bottom navigation.
- Verify that clicking the tab shows assignment resources.
- Test uploading a new assignment resource.
- Test unlocking an assignment resource using credits.
- Verify that assignments are filtered correctly in the new section.
