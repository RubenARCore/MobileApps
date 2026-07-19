# Implementation Plan - Upgrade Screen and Passive Income

This plan outlines the implementation of the Upgrade system and Passive Income mechanism for the Balkan Clicker Game.

## User Review Required

> [!IMPORTANT]
> The passive income loop will be implemented in the `HomeViewModel` for now, meaning it will only run while the Dashboard/Main UI is active. If the game requires background passive income, a more robust solution (like a WorkManager or a long-running Service) might be needed in the future.

> [!NOTE]
> For the adaptive layout, I will use `ListDetailPaneScaffold` from the Compose Material Adaptive library to show the Dashboard and Shop side-by-side on expanded screens.

## Proposed Changes

### 1. Domain Layer (Data Models and Logic)

#### [NEW] [Upgrade.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/model/Upgrade.kt)
Define the `Upgrade` data class.
- `id: String`
- `name: String`
- `description: String`
- `baseCost: Int`
- `passiveIncomePerLevel: Int`
- `clickPowerPerLevel: Int`

#### [MODIFY] [GameState.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/model/GameState.kt)
Update `GameState` to track owned upgrades.
- Add `upgradesOwned: Map<String, Int>` (Upgrade ID to Level).
- Add `totalPassiveIncome: Int`.

#### [NEW] [PurchaseUpgradeUseCase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/usecase/PurchaseUpgradeUseCase.kt)
Handle the logic for purchasing an upgrade.
- Calculate current cost based on level.
- Check if `score >= cost`.
- Update `GameState` (subtract score, increment level).

#### [NEW] [CalculatePassiveIncomeUseCase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/usecase/CalculatePassiveIncomeUseCase.kt)
Calculate the total passive income based on owned upgrades.

---

### 2. Presentation Layer (UI and ViewModels)

#### [NEW] [ShopViewModel.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/viewmodels/ShopViewModel.kt)
ViewModel for the Shop screen.
- Expose list of `Upgrade` items with their current costs and levels.
- Provide `purchaseUpgrade(upgradeId: String)` method.

#### [MODIFY] [ShopScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/screens/ShopScreen.kt)
Implement the Shop UI.
- Use `LazyColumn` to list upgrades.
- Use `ElevatedCard` for each upgrade item.
- Display name, description, level, cost, and a "Buy" button.

#### [MODIFY] [HomeViewModel.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/viewmodels/HomeViewModel.kt)
Implement the passive income loop.
- Use a `while(true)` loop with `delay(1000)` in a coroutine started on `init`.
- Update the score based on `totalPassiveIncome` from `GameState`.

---

### 3. Adaptive Layout

#### [MODIFY] [MainActivity.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/MainActivity.kt)
Implement side-by-side view for large screens.
- Use `ListDetailPaneScaffold` or a conditional `Row` to display `DashboardScreen` and `ShopScreen` simultaneously when the window width is `EXPANDED`.

## Verification Plan

### Automated Tests
- **Unit Tests**:
    - `PurchaseUpgradeUseCaseTest`: Verify cost calculation and score subtraction.
    - `CalculatePassiveIncomeUseCaseTest`: Verify correct summation of income.

### Manual Verification
1. Open the app on a phone:
    - Verify Dashboard shows score.
    - Click "Shop" in the navigation bar.
    - Purchase an upgrade and verify score decreases and level increases.
    - Go back to Dashboard and verify score increases automatically every second.
2. Open the app on a tablet/emulator with large screen:
    - Verify Dashboard and Shop are visible side-by-side.
