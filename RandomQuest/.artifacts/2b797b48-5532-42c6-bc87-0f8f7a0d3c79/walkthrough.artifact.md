# Корекция на подравняването на заглавието

Извърших корекция в подравняването на основното заглавие, за да гарантирам, че то е перфектно центрирано на екрана.

## Извършени промени

### [QuestGeneratorScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/screens/QuestGeneratorScreen.kt)
- Добавих `modifier = Modifier.fillMaxWidth()` и `textAlign = TextAlign.Center` към текста в `CenterAlignedTopAppBar`. Това гарантира, че текстът ще заема цялото налично пространство в заглавната лента и ще бъде центриран спрямо него.

### [ProfileScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/screens/ProfileScreen.kt)
- Приложих същата корекция и за екрана на профила, за да има последователност в дизайна на приложението.

## Верификация
- Промените са приложени директно в Composable функциите.
- Използването на `CenterAlignedTopAppBar` в комбинация с `fillMaxWidth` на текста е стандартен начин за постигане на точно центриране в Material 3.
