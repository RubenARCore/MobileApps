# Balkan Clicker Game - Project Walkthrough

This document summarizes the current project structure, setup, and key architectural decisions for the Balkan Clicker Game.

## 🚀 Tech Stack

The application is built using modern Android development tools and libraries:

- **Jetpack Compose**: Purely declarative UI framework.
- **Navigation 3**: Utilizing the latest state-driven navigation for Compose.
- **Hilt**: Dependency injection for clean and modular architecture.
- **Adaptive UI**: Built-in support for multiple form factors (phones, tablets, etc.).
- **Kotlin Coroutines & Serialization**: For asynchronous operations and type-safe navigation.
- **Clean Architecture**: Decoupled layers for maintainability and testability.
- **Material 3**: Following the latest Material Design guidelines with dynamic color support.

## 📁 Package Structure

The project follows a **Clean Architecture** approach, organized by layer:

- `com.ruben.balkanclickergame`
    - `data/`: Data layer containing repository implementations, data sources (Room/DataStore), and mappers.
    - `domain/`: Business logic layer with repository interfaces, use cases, and domain models.
    - `presentation/`: UI layer containing ViewModels, Screens (Composables), and Navigation keys.
    - `ui/`: Design system tokens (Theme, Color, Type).
    - `di/`: Hilt modules for dependency injection.
    - `MainActivity.kt`: Entry point hosting the navigation and adaptive scaffold.

## 🧭 Navigation & Adaptive UI

### Navigation 3
Navigation is implemented using **Jetpack Navigation 3**, which is state-driven and type-safe:
- **Routes**: Defined in `presentation/navigation/Routes.kt` as `@Serializable` objects implementing `NavKey`.
- **Backstack**: Managed by `rememberNavBackStack` in `MainActivity.kt`.
- **Display**: `NavDisplay` handles the rendering of screens based on the current backstack entry.

### Adaptive Design
The app is adaptive by default:
- **Window Size Classes**: Uses `currentWindowAdaptiveInfo()` to detect the device's width.
- **Navigation Components**:
    - **Compact (Phones)**: Displays a `NavigationBar` at the bottom.
    - **Medium/Expanded (Tablets/Foldables)**: Displays a `NavigationRail` on the side.
- **Edge-to-Edge**: Full screen rendering enabled with proper window insets handling.

## 🏗️ Architecture & DI Confirmation

- **Clean Architecture**:
    - `domain` layer has no dependencies on `data` or `presentation`.
    - `presentation` layer interacts with `domain` via Use Cases.
- **Hilt Setup**:
    - `BalkanClickerApplication` is annotated with `@HiltAndroidApp`.
    - `MainActivity` uses `@AndroidEntryPoint`.
    - `RepositoryModule` provides repository bindings using `@Binds`.
    - ViewModels are injected with `@HiltViewModel`.

## 🎨 UI & Theme
- **Material 3 Theme**: Implements dynamic color (Android 12+) with expressive color palettes.
- **Components**: Uses Material 3 components like `Scaffold`, `NavigationBar`, and `NavigationRail`.
