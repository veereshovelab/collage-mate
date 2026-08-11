# Implementation Plan - Complete Rebrand from ClgMate to Campusdeck

This plan outlines the steps required to execute a full rebranding of the application, including name changes across the codebase, configuration files, and asset references.

## User Review Required

> [!WARNING]
> This rebrand involves changing the `applicationId`. This is a breaking change for existing installations and external services (like Firebase or Google Play Console). Teammates will need to perform a clean build after these changes are applied.

## Proposed Changes

### Configuration & Build Files

#### [MODIFY] [settings.gradle.kts](file:///C:/Users/Microsoft/StudioProjects/collage-mate/settings.gradle.kts)
- Change `rootProject.name` from `"ClgMate"` to `"Campusdeck"`.

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/build.gradle.kts)
- Change `applicationId` from `"com.aistudio.clgmate.xyrvwn"` to `"com.aistudio.campusdeck.xyrvwn"`.

#### [MODIFY] [strings.xml](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/res/values/strings.xml)
- Change `app_name` from `"ClgMate"` to `"Campusdeck"`.

#### [MODIFY] [metadata.json](file:///C:/Users/Microsoft/StudioProjects/collage-mate/metadata.json)
- Update `name` and `description` to reflect the new brand.

---

### Data & Logic

#### [MODIFY] [AppDatabase.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/data/db/AppDatabase.kt)
- Rename the database file from `"clgmate_database"` to `"campusdeck_database"`.

#### [MODIFY] [CampusViewModel.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/CampusViewModel.kt)
- Update seeded content strings and greeting messages.

---

### UI & UX Components

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/HomeScreen.kt)
- Update header text and localized strings.

#### [MODIFY] [LoginScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/LoginScreen.kt)
- Update branding text and welcome messages.

#### [MODIFY] [ProfileScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/ProfileScreen.kt)
- Update help text and informational sections.

---

### Assets & References

#### [RENAME] [clgmate_logo.xml](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/res/drawable/clgmate_logo.xml) -> [campusdeck_logo.xml](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/res/drawable/campusdeck_logo.xml)

#### [MODIFY] [ic_launcher.xml](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml) & [ic_launcher_round.xml](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml)
- Update logo references from `@drawable/clgmate_logo` to `@drawable/campusdeck_logo`.

## Verification Plan

### Automated Tests
- Run `./gradlew clean :app:assembleDebug` to ensure the project builds correctly with the new name and application ID.

### Manual Verification
- Verify the app name on the launcher.
- Verify headers and welcome text in the UI (Login, Home, Profile).
- Check that the database migration (or fresh creation) works with the new name.
