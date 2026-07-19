# Home Screen Implementation Plan

Implement the core clicking mechanic and a responsive Dashboard UI for the Balkan Clicker Game.

## User Review Required

> [!IMPORTANT]
> The "Coin" will be represented by a placeholder icon (e.g., `Icons.Rounded.MonetizationOn`) initially. We can replace this with a custom Balkan-themed asset (like a Euro or Dinar coin) later.

## Proposed Changes

### Domain Layer

#### [NEW] [ProcessClickUseCase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/usecase/ProcessClickUseCase.kt)
- Create a use case to handle the score increment logic.
- It will read the current `GameState`, add `clickPower` to `score`, and call `repository.updateScore()`.

### Data Layer

#### [MODIFY] [GameRepositoryImpl.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/repository/GameRepositoryImpl.kt)
- Ensure the repository can handle rapid updates (current `MutableStateFlow` is fine for now).

### Presentation Layer

#### [MODIFY] [HomeViewModel.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/viewmodels/HomeViewModel.kt)
- Inject `ProcessClickUseCase`.
- Add `onCoinClicked()` function.

#### [MODIFY] [DashboardScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/screens/DashboardScreen.kt)
- Redesign the UI using Material 3 Expressive components.
- Implement a large, interactive coin button.
- Add `animateFloatAsState` for a scale-down effect on press.
- Implement a "Floating Text" effect to show the amount gained per click.
- Refine the `ListDetailPaneScaffold` to show detailed stats (Clicks per second, Total clicks, etc.) in the detail pane.

---

## Detailed UI Design (Dashboard)

- **Main Pane (List Pane):**
    - Large Score display at the top (`DisplayLarge`).
    - The "Big Coin" in the center, using `Box` and `IconButton` or `Modifier.clickable` with custom interaction source.
    - Click Power indicator at the bottom.
- **Detail Pane:**
    - Statistical breakdown.
    - Achievement progress bars.

## Animation Strategy

- **Scale Animation:** The coin will scale from 1.0f to 0.9f briefly when tapped.
- **Particle/Floating Text:** A small "+1" (or `clickPower`) text will spawn at the tap location and float upwards while fading out.

## Verification Plan

### Automated Tests
- Unit test for `ProcessClickUseCase` to verify score calculation.
- `./gradlew :app:testDebugUnitTest`

### Manual Verification
- Run the app on an emulator.
- Tap the coin rapidly and verify the score increments correctly.
- Verify the scale animation triggers on every tap.
- Test adaptive layout by resizing the window (e.g., Tablet/Desktop mode) and ensuring the `ListDetailPaneScaffold` behaves as expected.
