# Walkthrough - Upgrade Screen and Passive Income

I have completed the implementation of the Upgrade system and Passive Income mechanism, along with an adaptive layout for large screens.

## Key Changes

### 1. Domain Logic
- Verified the implementation of `Upgrade` model and `GameState` updates.
- Verified `PurchaseUpgradeUseCase` which handles the logic for buying upgrades, including cost calculation and score deduction.
- Verified `CalculatePassiveIncomeUseCase` and `CalculateClickPowerUseCase` for calculating game stats based on owned upgrades.
- Verified `ProcessPassiveIncomeUseCase` which handles the periodic score increase.

### 2. Presentation Layer
- Verified `ShopViewModel` which exposes available upgrades and handles purchase requests.
- Verified `HomeViewModel` which now includes a coroutine loop to process passive income every second.
- Verified `ShopScreen` which displays the "Balkan Bazaar" with upgrade cards.

### 3. Adaptive Navigation
- Refactored `MainActivity.kt` to use **Jetpack Navigation 3** with `ListDetailSceneStrategy` from the **Material 3 Adaptive** library.
- On large screens (Expanded), the app now displays the **Dashboard** and **Shop** side-by-side.
- On small screens (Compact), it switches between them while maintaining the Dashboard in the backstack to keep the passive income loop running.
- Refactored `DashboardScreen.kt` to remove internal scaffold and focus on content, allowing the top-level `NavDisplay` to manage the adaptive layout.

### 4. Automated Tests
Created unit tests for the core domain logic:
- `PurchaseUpgradeUseCaseTest`: Verifies successful purchase and failure when insufficient score.
- `CalculatePassiveIncomeUseCaseTest`: Verifies correct summation of passive income.
- `CalculateClickPowerUseCaseTest`: Verifies correct calculation of click power including base power.

## Verification Results

### Automated Tests
- Ran `./gradlew testDebugUnitTest` and all 8 tests passed.

### Build
- Ran `./gradlew assembleDebug` and the build finished successfully.

## Visuals
I have integrated the `ListDetailPaneScaffold` behavior via `NavDisplay`. On a tablet, you should see the Dashboard on the left and the Shop on the right. On a phone, you can navigate between them using the bottom bar.
