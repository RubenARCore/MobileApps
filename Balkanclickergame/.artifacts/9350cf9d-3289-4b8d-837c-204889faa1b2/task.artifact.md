# Tasks: Balkan Clicker 2.0 Mega-Update

- [x] **Phase 1: Persistence (Room)**
    - [x] Add Room dependencies to `build.gradle.kts`
    - [x] Create `GameStateEntity` and `OwnedUpgradeEntity`
    - [x] Create `GameDao` and `GameDatabase`
    - [x] Update `GameRepositoryImpl` to use Room
    - [x] Verify persistence with a manual test (restart app)

- [x] **Phase 2: Prestige System**
    - [x] Update `GameState` domain model with prestige fields
    - [x] Implement `EmigrateUseCase`
    - [x] Add "Emigrate" button to Dashboard/Shop
    - [x] Implement Prestige multiplier in `CalculatePassiveIncomeUseCase` and `CalculateClickPowerUseCase`

- [x] **Phase 3: New Upgrades & Mechanics**
    - [x] Add "Municipal Connections" and "IT Nephew" to `UpgradeRepository`
    - [x] Implement discount logic in `Upgrade.calculateCost` or `PurchaseUpgradeUseCase`
    - [x] Create `AutoClickManager` for "IT Nephew" auto-clicking

- [x] **Phase 4: Regional Themes**
    - [x] Define `RegionalTheme` data class and regional data
    - [x] Update `DashboardScreen` to display current currency and icons
    - [x] Implement theme switching logic based on lifetime coins

- [x] **Phase 5: Random Events**
    - [x] Create `RandomEventManager` with timer logic
    - [x] Implement "Wedding" (Bonus) and "Inflation" (Penalty)
    - [x] Add UI overlay/notification for active events

- [x] **Phase 6: Audio-Visual Feedback**
    - [x] Create `FeedbackManager` for Haptics and Sounds
    - [/] Add SFX resources (Integration ready, assets pending)
    - [x] Integrate feedback into `DashboardScreen` and `ShopViewModel`
    - [x] Create Adaptive App Icon
