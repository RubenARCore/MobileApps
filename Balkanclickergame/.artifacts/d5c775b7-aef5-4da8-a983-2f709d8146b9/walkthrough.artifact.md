# Externalized Strings Walkthrough

I have externalized all hardcoded strings from the `DashboardScreen` and `ShopScreen` into `strings.xml`. I also refactored the `Upgrade` model to support localized names and descriptions.

## Changes Made

### UI Strings Externalized
- All static text like "Balkan Clicker", "Lifetime Stats", etc., are now in `strings.xml`.
- Dynamic text with placeholders (e.g., "+10 / tap", "Level 5") now uses formatted string resources.
- Floating text animations now use `context.getString` to correctly format the values.

### Data Layer Refactoring
- [Upgrade.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/domain/model/Upgrade.kt) now uses `@StringRes Int` for `nameResId` and `descriptionResId`.
- [UpgradeRepositoryImpl.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/repository/UpgradeRepositoryImpl.kt) was updated to use `R.string` identifiers.

### Verification Results
- **Build**: Successful.
- **Unit Tests**: All 13 tests passed, including those for use cases that create `Upgrade` instances.
