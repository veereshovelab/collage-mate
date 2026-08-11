# Walkthrough - Complete Rebrand to Campusdeck

I have successfully rebranded the entire application from **ClgMate** to **Campusdeck**. This included updating the project identity, UI text, assets, and database configuration.

## Changes Made

### 1. Identity & Build Configuration
- **Application ID**: Changed to `com.aistudio.campusdeck.xyrvwn` in [app/build.gradle.kts](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/build.gradle.kts).
- **Project Name**: Updated to `Campusdeck` in [settings.gradle.kts](file:///C:/Users/Microsoft/StudioProjects/collage-mate/settings.gradle.kts) and [strings.xml](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/res/values/strings.xml).
- **Metadata**: Updated [metadata.json](file:///C:/Users/Microsoft/StudioProjects/collage-mate/metadata.json) with the new brand name and description.

### 2. Assets & Branding
- **Logo**: Renamed `clgmate_logo.xml` to `campusdeck_logo.xml`.
- **Icons**: Updated [ic_launcher.xml](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml) and [ic_launcher_round.xml](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml) to reference the new logo resource.

### 3. User Interface Overhaul
- **Home Screen**: Updated the title header in [HomeScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/HomeScreen.kt).
- **Login Screen**: Updated welcome text and auth card labels in [LoginScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/LoginScreen.kt).
- **Profile Screen**: Updated the informational "How CC Works" section in [ProfileScreen.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/ProfileScreen.kt).

### 4. Data Layer
- **Database**: Renamed the local Room database to `campusdeck_database` in [AppDatabase.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/data/db/AppDatabase.kt).
- **Seeded Content**: Updated the initial greeting post in [CampusViewModel.kt](file:///C:/Users/Microsoft/StudioProjects/collage-mate/app/src/main/java/com/example/ui/CampusViewModel.kt).

## Verification Results

### Build Success
The project builds successfully with the new package name and branding.
```bash
$ ./gradlew clean :app:assembleDebug
BUILD SUCCESSFUL in 6s
```

> [!IMPORTANT]
> Because the `applicationId` has changed, you should uninstall the old "ClgMate" app from your device/emulator before running the new "Campusdeck" app to avoid conflicts.
