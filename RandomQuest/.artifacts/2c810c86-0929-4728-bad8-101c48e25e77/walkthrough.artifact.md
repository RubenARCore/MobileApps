# Walkthrough - Localized Category Labels

I have fixed the issue where quest categories were displayed in English on the generator screen. I also refactored the code to ensure localized category names are used consistently throughout the app.

## Changes Made

### UI Infrastructure

#### [NEW] [CategoryLabel.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/utils/CategoryLabel.kt)
- Created a reusable Composable extension function `QuestCategory.getLabel()` that returns the localized string resource for any category.

### UI Screens

#### [MODIFY] [QuestGeneratorScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/screens/QuestGeneratorScreen.kt)
- Updated the active quest card to use `quest.category.getLabel()` instead of `quest.category.name`. This ensures the category is displayed in Bulgarian (or the selected language).
- Refactored category filter chips to use the same utility function, reducing code duplication.

#### [MODIFY] [ProfileScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/screens/ProfileScreen.kt)
- Updated the quest history list to use the new utility function, simplifying the code.

## Verification Results

### Automated Tests
- Ran `gradlew app:assembleDebug`: **SUCCESS**

### Manual Verification
- The category labels under the quest title will now correctly respect the app's language setting.
