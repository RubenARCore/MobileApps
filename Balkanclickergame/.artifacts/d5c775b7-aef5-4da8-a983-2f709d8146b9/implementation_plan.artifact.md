# Externalize Hardcoded Strings

This plan covers externalizing hardcoded strings from `DashboardScreen.kt` and `ShopScreen.kt` into `strings.xml` to improve maintainability and localization support.

## Proposed Changes

### [Component Name] UI Layer

#### [MODIFY] [strings.xml](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/res/values/strings.xml)
- Add string resources for all hardcoded text in Dashboard and Shop screens.
- Include placeholders for dynamic values (e.g., scores, levels, costs).

#### [MODIFY] [DashboardScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/screens/DashboardScreen.kt)
- Replace hardcoded strings with `stringResource(id = ...)` calls.
- Update dynamic text formatting to use string resources with arguments.

#### [MODIFY] [ShopScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/presentation/screens/ShopScreen.kt)
- Replace hardcoded strings with `stringResource(id = ...)` calls.
- Update dynamic text formatting for levels and costs.

### [Optional] Data Layer (Recommended)

#### [MODIFY] [Upgrade.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/model/Upgrade.kt)
- Change `name` and `description` to be resource IDs (Int) to allow localization.

#### [MODIFY] [UpgradeRepositoryImpl.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/repository/UpgradeRepositoryImpl.kt)
- Use `R.string.upgrade_...` for names and descriptions.

## Verification Plan

### Automated Tests
- Run existing tests to ensure no regressions in logic.
- `./gradlew :app:testDebugUnitTest`

### Manual Verification
- Verify that the UI still displays the correct text.
- Verify that dynamic values (score, levels) are correctly formatted.
