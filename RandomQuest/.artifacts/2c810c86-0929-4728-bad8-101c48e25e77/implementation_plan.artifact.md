# Implementation Plan - Default Welcome Quest

The goal is to show a default "Welcome Quest" when the app starts, prompting the user to start their journey.

## Proposed Changes

### UI State and Logic

#### [MODIFY] [QuestViewModel.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/viewmodel/QuestViewModel.kt)
- Define a constant `WELCOME_QUEST` in a companion object or as a top-level private val.
- Initialize `activeQuest` in `QuestUiState` with `WELCOME_QUEST`.
- Update `completeQuest()` to handle the `WELCOME_QUEST` (id = -1) by simply clearing the state without database interaction.

### UI Components

#### [MODIFY] [QuestGeneratorScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/screens/QuestGeneratorScreen.kt)
- In `ActiveQuestBentoCard`, hide the category label and icon if the quest is the `WELCOME_QUEST`.
- Also hide the "Share" button for the welcome message as it doesn't make sense to share it.

## User Review Required

> [!NOTE]
> I will use a special ID (`-1`) for the welcome quest to distinguish it from database quests.
> For the English version, I will provide a translated equivalent so it doesn't look out of place if the user changes the language.

## Verification Plan

### Manual Verification
- Launch the app.
- Verify that the "Ще се предизвикаш ли?" card is shown immediately.
- Verify that clicking "Complete" clears it.
- Verify that clicking "Reroll" or "Generate" replaces it with a real quest.
