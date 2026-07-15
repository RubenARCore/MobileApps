# Implementation Plan - Fix Quest Generation (Database Migration)

The user reports that quests are not being generated. Investigation of logs reveals a crash due to an invalid enum constant `CREATIVE` in the database, which is not present in the `QuestCategory` enum. This likely happened due to inconsistent data from a previous version of the app.

Additionally, the new 360 quests added recently might not have been loaded if the database already contained some data.

## Proposed Changes

### Data Layer

#### [MODIFY] [AppDatabase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/data/AppDatabase.kt)
- Increment the database version from `4` to `5`.
- This will trigger a destructive migration (wiping the database) because `fallbackToDestructiveMigration(dropAllTables = true)` is enabled in `MainActivity`.

#### [MODIFY] [QuestRepository.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/data/QuestRepository.kt)
- Update `prepopulateIfNeeded()` to be more robust. Instead of just checking if count is 0, we can ensure that if the count is significantly lower than the expected size, we insert the initial quests.
- However, with the version increment, `count == 0` will be true on the next start, so the new 360 quests will be loaded correctly.

## Verification Plan

### Automated Tests
- Run `gradlew app:assembleDebug` to ensure it builds.
- Check logcat after start to ensure no more `IllegalArgumentException` is thrown.

### Manual Verification
- Deploy the app and verify that the "Generate" button now works and displays quests from the new list.
