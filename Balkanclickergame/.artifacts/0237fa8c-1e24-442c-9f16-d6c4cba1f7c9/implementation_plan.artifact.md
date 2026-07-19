# Implementation Plan: Persistence and Verification

The goal is to ensure the persistence layer is correctly implemented according to the naming conventions and requirements, and to address missing assets and logic gaps identified during the project audit.

## User Review Required

> [!IMPORTANT]
> - I will rename `GameDao` to `GameStateDao` to match the specific request.
> - I will implement actual currency icons (currently using placeholders) in a subsequent phase, as Task 1 focuses on Persistence.
> - I will verify that the Room database correctly handles the `GameState` and `OwnedUpgrade` entities.

## Proposed Changes

### [Data Layer] Persistence Alignment

#### [MODIFY] [GameDatabase.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/datasource/local/GameDatabase.kt)
- Update to use `GameStateDao` instead of `GameDao`.

#### [NEW] [GameStateDao.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/datasource/local/dao/GameStateDao.kt)
- Rename from `GameDao.kt` and ensure all methods are correctly defined.

#### [DELETE] [GameDao.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/datasource/local/dao/GameDao.kt)
- Removed in favor of `GameStateDao.kt`.

### [DI Layer] Dependency Update

#### [MODIFY] [DatabaseModule.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/di/DatabaseModule.kt)
- Update to provide `GameStateDao`.

### [Repository Layer] Repository Update

#### [MODIFY] [GameRepositoryImpl.kt](file:///C:/Users/Ruben/Desktop/MobileApps/Balkanclickergame/app/src/main/java/com/ruben/balkanclickergame/data/repository/GameRepositoryImpl.kt)
- Update to use `GameStateDao`.

## Verification Plan

### Automated Tests
- Run existing unit tests: `./gradlew :app:testDebugUnitTest`
- Specifically check `GameRepositoryImplTest` if it exists, or create a simple persistence test.

### Manual Verification
- Build the app to ensure no compilation errors: `./gradlew :app:assembleDebug`
