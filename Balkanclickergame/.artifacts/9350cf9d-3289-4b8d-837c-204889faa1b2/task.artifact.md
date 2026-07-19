# Tasks: Balkan Clicker 2.0 Mega-Update

- [ ] **Phase 1: Persistence (Room)**
    - [ ] Add Room dependencies to `build.gradle.kts`
    - [ ] Create `GameStateEntity` and `OwnedUpgradeEntity`
    - [ ] Create `GameDao` and `GameDatabase`
    - [ ] Update `GameRepositoryImpl` to use Room
    - [ ] Verify persistence with a manual test (restart app)

- [ ] **Phase 2: Prestige System**
    - [ ] Update `GameState` domain model with prestige fields
    - [ ] Implement `EmigrateUseCase`
    - [ ] Add "Emigrate" button to Dashboard/Shop
    - [ ] Implement Prestige multiplier in `CalculatePassiveIncomeUseCase` and `CalculateClickPowerUseCase`

- [ ] **Phase 3: New Upgrades & Mechanics**
    - [ ] Add "Municipal Connections" and "IT Nephew" to `UpgradeRepository`
    - [ ] Implement discount logic in `Upgrade.calculateCost` or `PurchaseUpgradeUseCase`
    - [ ] Create `AutoClickManager` for "IT Nephew" auto-clicking

- [ ] **Phase 4: Regional Themes**
    - [ ] Define `RegionalTheme` data class and regional data
    - [ ] Update `DashboardScreen` to display current currency and icons
    - [ ] Implement theme switching logic based on lifetime coins

- [ ] **Phase 5: Random Events**
    - [ ] Create `RandomEventManager` with timer logic
    - [ ] Implement "Wedding" (Bonus) and "Inflation" (Penalty)
    - [ ] Add UI overlay/notification for active events

- [ ] **Phase 6: Audio-Visual Feedback**
    - [ ] Create `FeedbackManager` for Haptics and Sounds
    - [ ] Add SFX resources (Click, Purchase, Event)
    - [ ] Integrate feedback into `DashboardScreen` and `ShopViewModel`
