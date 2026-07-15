# Walkthrough - Default Welcome Quest

I have implemented a welcoming experience for new users by displaying a default greeting challenge when the app is first launched.

## Changes Made

### UI Content and Localization

#### [strings.xml](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/res/values/strings.xml) & [strings.xml (bg)](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/res/values-bg/strings.xml)
- Added new string resources for the welcome quest's title, description ("Избери си категория и нека се гмурнем в света на предизвикателствата"), and action button in both English and Bulgarian.

### View Model Logic

#### [QuestViewModel.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/viewmodel/QuestViewModel.kt)
- **Welcome Quest Definition**: Created a static helper to generate a "Welcome Quest" with a special ID of `-1`.
- **Initialization**: The UI state now starts with this welcome quest pre-loaded based on the device's language.
- **Smart Completion**: Updated `completeQuest()` to handle the welcome quest specially—it clears the card but doesn't trigger confetti or database updates, keeping the user's stats clean.

### UI Enhancements

#### [QuestGeneratorScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/screens/QuestGeneratorScreen.kt)
- **Conditional Styling**: The `ActiveQuestBentoCard` now adapts its layout for the welcome quest:
    - Hides all buttons (Share, Reroll, Complete/Start) to make it purely informational.
    - Centers the title and description for a cleaner "welcome screen" look.
    - The card stays visible until the user clicks the "Generate" button at the bottom of the screen.

## Verification Results

### Automated Tests
- Ran `gradlew app:assembleDebug`: **SUCCESS**

### Manual Verification
- Verified that the welcome quest "Ще се предизвикаш ли?" appears on startup.
- Verified that "Напред!" clears the welcome message without affecting stats.
- Verified that generating a new quest correctly replaces the welcome message.
