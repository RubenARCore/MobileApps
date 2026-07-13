# Project Plan

Random Quest - Generator of random challenges (Генератор на случайно предизвикателство)

## Project Brief

# Project Brief: RandomQuest


**RandomQuest** is a vibrant, gamified Android application designed to break daily routines by generating spontaneous challenges. It
 transforms ordinary moments into "quests," encouraging users to explore, learn, and step out of their comfort zones through a sleek
, Material 3 interface.

### Features
1.  **Dynamic Quest Generator**: A prominent, energetic central action that pulls a
 random challenge from a curated pool (e.g., "Compliment a stranger," "Learn 'Thank You' in a
 new language," or "Do 10 pushups").
2.  **Category & Energy Filters**: Allows users to filter quests
 by type (Social, Fitness, Creative, Mindful) or by the time/energy required, ensuring the challenge fits their
 current situation.
3.  **Active Quest Dashboard**: A dedicated Material 3 card view for the current quest,
 featuring "Complete" and "Reroll" actions, integrated with system sharing to post challenges to social media.
4
.  **Progress & Streaks**: A simplified profile tracking the number of completed quests and current daily streaks to build user
 engagement and a sense of achievement.

### High-Level Tech Stack
*   **Language**: Kotlin
*   **UI
 Framework**: Jetpack Compose with **Material Design 3** (energetic color schemes, edge-to-edge display).

*   **Navigation**: **Jetpack Navigation 3** (state-driven navigation model).
*   **
Adaptive Strategy**: **Compose Material 3 Adaptive** (using `NavigationSuiteScaffold` and `ListDetailPaneSc
affold` to support handhelds, foldables, and tablets).
*   **Asynchronous Logic**: Kotlin Coroutines for handling
 quest randomization and UI animations.
*   **Media**: Coil for loading vibrant quest-related iconography.

## Implementation Steps
**Total Duration:** 5m 7s

### Task_1_Core_Data_Logic: Set up the data layer and core logic. Define Quest data models, Room database for quest persistence, and a repository to handle quest randomization and filtering by category (Social, Fitness, Creative, Mindful) and energy levels.
- **Status:** COMPLETED
- **Updates:** Data layer implemented successfully. 
- Quest entity, Category and EnergyLevel enums created.
- Room database (AppDatabase) and DAO (QuestDao) with random selection logic implemented.
- QuestRepository implemented with streak calculation and filtering.
- Initial quests pre-populated in the database.
- Unit tests passed for streak logic.
- compileSdk/targetSdk updated to 37.
- **Acceptance Criteria:**
  - Room database and DAO for quests and progress are functional
  - Quest randomization logic with category/energy filtering is implemented
  - Repository provides a clean API for the UI layer
- **Duration:** 5m 7s

### Task_2_UI_Screens: Develop the primary UI screens using Jetpack Compose and Material 3. Implement the energetic Quest Generator screen with a central action button, the Active Quest Dashboard featuring complete/reroll actions and sharing, and the Progress/Streaks profile view.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Quest Generator UI is vibrant and functional
  - Active Quest card displays quest details and handle actions
  - Progress and Streaks are correctly displayed in the profile view
  - Material 3 components and Coil for iconography are integrated
- **StartTime:** 2026-07-13 16:35:05 EEST

### Task_3_Navigation_Adaptive: Implement app navigation and adaptive layouts. Use Jetpack Navigation 3 for state-driven navigation and Compose Material 3 Adaptive (NavigationSuiteScaffold and ListDetailPaneScaffold) for multi-device support (handhelds, foldables, tablets).
- **Status:** PENDING
- **Acceptance Criteria:**
  - Navigation between Generator, Dashboard, and Profile works via state-driven model
  - Adaptive layout adjusts correctly across different window size classes
  - NavigationSuiteScaffold is used for the main shell

### Task_4_Polish_Verify: Finalize the app with vibrant Material 3 styling, adaptive icons, and full edge-to-edge support. Conduct a final run to verify stability, confirm alignment with requirements, and ensure no crashes.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Vibrant Material 3 color scheme (light/dark) applied
  - Adaptive app icon matching the quest theme is created
  - Full Edge-to-Edge display is functional
  - Project builds successfully and app does not crash
  - Final stability and UI requirement verification complete

