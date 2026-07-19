# Implementation Plan - Balkan Clicker 2.0 Mega-Update

This plan outlines the steps to upgrade "Balkan Clicker" to version 2.0, introducing persistence, prestige, new mechanics, and polished feedback.

## User Review Required

> [!IMPORTANT]
> - **Prestige Logic**: Emigration will reset all current coins and upgrade levels. The user will gain "Prestige Points" based on their lifetime earnings or current progress.
> - **Room Migration**: Since there was no persistence before, this will be a fresh installation. If we add fields later, we'll need Room migrations.
> - **Haptic Feedback**: Requires permission in AndroidManifest (already standard, but good to note).

## Proposed Changes

### 1. [Persistence] Room Database
We will implement Room to persist the game state.

#### [NEW] [GameDatabase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/datasource/local/GameDatabase.kt)
- Define the Room database.
- Include entities for `GameStateEntity` and `OwnedUpgradeEntity`.

#### [NEW] [GameStateEntity.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/datasource/local/entity/GameStateEntity.kt)
- Database entity representing the core `GameState`.

#### [NEW] [OwnedUpgradeEntity.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/datasource/local/entity/OwnedUpgradeEntity.kt)
- Relational table for storing upgrade levels.

#### [MODIFY] [GameRepositoryImpl.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/repository/GameRepositoryImpl.kt)
- Inject Room DAO and update methods to read/write from the database instead of memory.

---

### 2. [Prestige] Emigration System
Add the ability to reset progress for a permanent boost.

#### [MODIFY] [GameState.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/model/GameState.kt)
- Add `prestigePoints: Long` and `prestigeMultiplier: Double`.

#### [NEW] [EmigrateUseCase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/usecase/EmigrateUseCase.kt)
- Logic to reset `score` and `upgradesOwned`, while increasing `prestigePoints`.

---

### 3. [Upgrades] New Mechanics
"Municipal Connections" and "IT Nephew".

#### [MODIFY] [UpgradeRepositoryImpl.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/repository/UpgradeRepositoryImpl.kt)
- Add the two new upgrade definitions.

#### [MODIFY] [PurchaseUpgradeUseCase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/usecase/PurchaseUpgradeUseCase.kt)
- Incorporate "Municipal Connections" discount logic.

#### [NEW] [AutoClickService.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/manager/AutoClickManager.kt)
- Manager that triggers clicks periodically based on "IT Nephew" level.

---

### 4. [Regional Themes] Dynamic Currency
Switch currency based on progress.

#### [NEW] [RegionalTheme.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/model/RegionalTheme.kt)
- Data class containing currency symbol and icon.

#### [MODIFY] [DashboardScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/screens/DashboardScreen.kt)
- Update UI to use the current `RegionalTheme`.

---

### 5. [Events] Random Events Manager
"Wedding" and "Inflation" events.

#### [NEW] [RandomEventManager.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/presentation/manager/RandomEventManager.kt)
- Logic for timer-based event triggers.

---

### 6. [Feedback] Sounds and Haptics
Enhance the user experience.

#### [NEW] [FeedbackManager.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/presentation/manager/FeedbackManager.kt)
- Wrapper for Haptic feedback and SoundPool for SFX.

---

## Verification Plan

### Automated Tests
- `GameRepositoryTest`: Verify Room persistence (Save/Load).
- `PurchaseUpgradeUseCaseTest`: Verify discount logic.
- `EmigrateUseCaseTest`: Verify reset logic and multiplier calculation.

### Manual Verification
- Click the coin and check if progress persists after app restart.
- Reach the "Emigrate" threshold and verify boost.
- Observe random event popups.
- Listen for click sounds and feel haptics.
