# Major Improvements Plan: Persistence, Sound, Haptics, Regional Themes, Prestige, and Upgrades

This plan outlines the implementation of six major improvements to the Balkan Clicker game to enhance user engagement, retention, and sensory experience.

## User Review Required

> [!IMPORTANT]
> - **Prestige System (Emigration):** Resetting progress can be frustrating if not clearly communicated. We will add a confirmation dialog explaining what is lost and what is gained.
> - **Sound Assets:** I will assume we have (or can generate/find) license-free Balkan-themed sounds for clicks and purchases.

## Proposed Changes

### 1. Persistence (Room Database)
Currently, game state is likely held in memory or DataStore. We will move to Room for robust local storage.

#### [NEW] [GameDatabase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/local/GameDatabase.kt)
- Define the database and its version.
#### [NEW] [GameStateDao.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/local/GameStateDao.kt)
- CRUD operations for the single `GameState` record.
#### [NEW] [Converters.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/local/Converters.kt)
- Handle serialization of `Map<String, Int>` and `Set<String>` using Moshi or JSON.
#### [MODIFY] [GameState.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/model/GameState.kt)
- Add `@Entity` annotation.
- Add fields for `prestigeMultiplier`, `prestigePoints`, and `currentRegion`.

---

### 2. Sound & Haptics
Enhance the "juice" of the game with auditory and tactile feedback.

#### [NEW] [AudioManager.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/ui/util/AudioManager.kt)
- Helper class to wrap `SoundPool` for low-latency click sounds.
#### [MODIFY] [ClickerScreen.kt] (Location to be confirmed)
- Inject `AudioManager` and use `LocalHapticFeedback.current` on click and purchase events.

---

### 3. Regional Themes
Allow players to choose or unlock different Balkan regions, changing the background and currency icon (e.g., RSD, HRK/EUR, BAM, ALL).

#### [NEW] [Region.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/domain/model/Region.kt)
- Enum class for regions (Serbia, Croatia, Bosnia, Montenegro, etc.).
- Contains properties for currency symbol and background image resource.
#### [MODIFY] [MainViewModel.kt]
- Add logic to swap the current region.

---

### 4. Prestige System (Emigration to the "West")
The "Emigration" mechanic allows players to reset their progress in exchange for a permanent multiplier.

#### [MODIFY] [GameState.kt]
- `totalPrestigePoints: Double`: Calculated based on `lifetimeCoins`.
- `currentPrestigeMultiplier: Double`: (1.0 + prestigePoints * 0.05).
#### [NEW] [EmigrationUseCase.kt]
- Logic to reset score and upgrades while increasing prestige.

---

### 5. New Upgrades
Expand the content with more Balkan tropes.

#### [MODIFY] [UpgradeRepositoryImpl.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/repository/UpgradeRepositoryImpl.kt)
- Add: "Burek Stand", "Cevapi Grill", "Turbo Folk Radio", "Stockpiling Ajvar", "Illegal Betting Shop".

## Verification Plan

### Automated Tests
- `GameStateDaoTest`: Verify saving and loading state correctly with TypeConverters.
- `PrestigeLogicTest`: Verify the multiplier calculation is correct.

### Manual Verification
- Verify that closing the app and reopening it preserves the score.
- Verify haptic feedback is felt on click.
- Verify sound plays on click (simulated if no physical device).
- Verify "Emigration" resets the game but increases income speed.
