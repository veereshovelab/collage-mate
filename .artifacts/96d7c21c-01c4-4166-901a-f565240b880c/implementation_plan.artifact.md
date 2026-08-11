# Implementation Plan - Friend Suggestion Carousel

I will implement a horizontally scrollable "Friend Suggestion" carousel and integrate it into the main vertical feed of the `HomeScreen`. This will involve updating the data structure of the feed and creating new Bento-styled components for the suggestions.

## Proposed Changes

### Data Models
- **[NEW]** `HomeFeedItem` sealed class to handle both `FeedPost` and a list of `FriendSuggestion`.
- **[NEW]** `FriendSuggestion` data class to represent suggested users.

### UI Components
- **[NEW]** `FriendSuggestionCard`: A Material 3 card with a circular avatar, username, context text (e.g., "Mutual friends"), a "Connect" button, and a dismiss ('X') icon.
- **[NEW]** `FriendSuggestionCarousel`: A horizontally scrollable `LazyRow` containing multiple `FriendSuggestionCard` items.

### HomeScreen Integration
- **[MODIFY] [HomeScreen.kt](file:///C:/Users/Vanishree/StudioProjects/collage-mate/app/src/main/java/com/example/ui/screens/HomeScreen.kt)**:
    - Update the `LazyColumn` to handle `HomeFeedItem`.
    - Logic to "inject" the suggestion carousel at a specific position (e.g., after the 2nd post).
    - Implement the dismissal logic for the carousel.

## Verification Plan

### Manual Verification
- I will use `render_compose_preview` to verify the "Friend Suggestion" card design.
- I will verify the carousel scrolls horizontally and fits within the vertical feed.
- I will ensure the styling matches the existing Bento theme (Lavender and Lilac containers).
