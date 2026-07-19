# Implementation Plan - Achievement System

This plan outlines the implementation of a robust Achievement System for the Balkan Clicker Game. The system will track player progress, unlock milestones, and provide visual feedback through animated popups.

## User Review Required

> [!IMPORTANT]
> The achievement check will be integrated directly into the `ProcessClickUseCase`, `PurchaseUpgradeUseCase`, and `ProcessPassiveIncomeUseCase`. This ensures that achievements are checked whenever the game state changes.

## Proposed Changes

### Domain Layer

#### [NEW] [Achievement.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/model/Achievement.kt)
Define the `Achievement` data class.
- `id`: String
- `title`: String
- `description`: String
- `icon`: ImageVector (using Material Icons)
- `criteria`: (GameState) -> Boolean

#### [NEW] [Achievements.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/model/Achievements.kt)
A hardcoded list of achievements (e.g., "First Click", "Millionaire", "Upgrade Addict").

#### [MODIFY] [GameState.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/model/GameState.kt)
Add `unlockedAchievementIds: Set<String>` to track unlocked achievements.

#### [NEW] [CheckAchievementsUseCase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/usecase/CheckAchievementsUseCase.kt)
Logic to evaluate which achievements are newly met based on the current `GameState` and previously unlocked IDs.

---

### Presentation Layer

#### [NEW] [AchievementNotificationManager.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/manager/AchievementNotificationManager.kt)
A `@Singleton` manager to handle the achievement queue and the currently displayed popup.
- `currentAchievement`: StateFlow of the achievement to show.
- `show(achievement)`: Adds to queue.
- `dismiss()`: Moves to the next achievement in the queue.

#### [NEW] [AchievementPopup.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/components/AchievementPopup.kt)
A Composable that renders the achievement notification with Material 3 styling and Expressive animations.
- Uses `AnimatedVisibility` for entry/exit.
- Auto-dismiss logic after a delay.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/MainActivity.kt)
Observe `AchievementNotificationManager` and display the `AchievementPopup` at the top of the screen.

---

### Integration

#### [MODIFY] [ProcessClickUseCase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/usecase/ProcessClickUseCase.kt)
#### [MODIFY] [PurchaseUpgradeUseCase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/usecase/PurchaseUpgradeUseCase.kt)
#### [MODIFY] [ProcessPassiveIncomeUseCase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/usecase/ProcessPassiveIncomeUseCase.kt)
These use cases will be updated to:
1. Call `CheckAchievementsUseCase` after updating the state.
2. Update the `GameState` with the new `unlockedAchievementIds`.
3. Notify `AchievementNotificationManager` of any newly unlocked achievements.

## Verification Plan

### Automated Tests
- **Unit Tests**: Create `CheckAchievementsUseCaseTest` to verify that criteria are correctly evaluated and duplicates are handled.
- **ViewModel Tests**: Verify that ViewModels correctly trigger the notification manager when achievements are unlocked.

### Manual Verification
1. Launch the app.
2. Perform actions (click coin, buy upgrades) to trigger achievements.
3. Verify that the popup appears with the correct title, description, and icon.
4. Verify that multiple achievements are shown sequentially if triggered at the same time.
5. Verify that achievements persist (i.e., don't trigger again after being unlocked).
