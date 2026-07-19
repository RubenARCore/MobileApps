# Project Plan

Create a new Android application "Balkan clicker game".
Requirements:
- Material 3
- MVVM architecture
- Navigation Compose
- Hilt
- StateFlow
- Clean Architecture

Project structure should follow Clean Architecture principles (Data, Domain, Presentation layers).
Only create the structure and boilerplate, no game logic.

## Project Brief

# Balkan Clicker Game - Project Brief

## Features
- **Core Clicker Mechanic**: A primary interactive UI element representing a Balkan icon that increments currency ("Balkan Coins") upon user interaction.
- **Themed Upgrades Shop**: A catalog of upgrades (e.g., "Traditional Coffee Pot", "Old Zastava") that increase the value per click or provide passive income.
- **Adaptive Dashboard**: A responsive main screen that adjusts its layout for phones, tablets, and foldables, showing stats and the clickable item.
- **Progression & Ranks**: A simple leveling system that unlocks new visual themes or titles based on total coins earned.

## High-Level Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Navigation**: Jetpack Navigation 3 (State-driven navigation model)
- **Adaptive Strategy**: Compose Material Adaptive library for multi-pane and responsive layouts
- **Architecture**: MVVM + Clean Architecture (Data, Domain, and Presentation layers)
- **Dependency Injection**: Hilt
- **Concurrency & State**: Kotlin Coroutines and StateFlow for reactive UI updates

---

> [!NOTE]
> The project is structured to strictly follow Clean Architecture principles, ensuring a clear separation between business logic (Domain), data sources (Data), and the UI (Presentation).

## Implementation Steps

### Task_1_ProjectSetup: Configure project dependencies (Hilt, Navigation 3, Compose Adaptive) and set up the Clean Architecture package structure.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Hilt, Navigation 3, and Compose Adaptive dependencies added to build files
  - Data, Domain, and Presentation package structure created
  - Project builds successfully
- **StartTime:** 2026-07-19 07:52:54 EEST

### Task_2_ArchitectureDI: Implement Clean Architecture boilerplate including Domain interfaces, Data layer skeletons, and Hilt DI modules.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Repository interfaces and dummy UseCases defined in Domain layer
  - Hilt modules configured for dependency injection
  - Base ViewModel and StateFlow patterns established

### Task_3_UINavigation: Implement Navigation 3 infrastructure and create adaptive placeholder screens for Dashboard and Shop.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Navigation 3 setup with NavKey and NavHost implemented
  - Adaptive layout scaffolding (e.g., ListDetail or SupportingPane) for Dashboard and Shop
  - Screens are reachable via navigation

### Task_4_RunVerify: Final verification of the application structure, stability, and requirement alignment.
- **Status:** PENDING
- **Acceptance Criteria:**
  - App does not crash on startup
  - Navigation between placeholders works
  - Hilt injection verified
  - Build pass
  - Critic agent confirms alignment with requirements

