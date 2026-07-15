# Премахване на ниво на енергия (Energy Level)

Този план описва стъпките за пълно премахване на концепцията за "ниво на енергия" от приложението Random Quest. Това включва промени в модела на данните, базата данни, бизнес логиката и потребителския интерфейс.

## Потребителски преглед

> [!IMPORTANT]
> Промяната в модела на данните (`Quest`) ще изисква миграция на базата данни Room. Тъй като приложението е в процес на разработка, ще използваме `fallbackToDestructiveMigration()`, което ще изтрие съществуващите данни при стартиране след промяната.

## Предложени промени

### [Data Layer]

#### [MODIFY] [Quest.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/model/Quest.kt)
- Изтриване на `enum class EnergyLevel`.
- Премахване на полето `energyLevel` от `data class Quest`.

#### [MODIFY] [QuestDataSource.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/data/QuestDataSource.kt)
- Премахване на `energyLevel` параметъра при дефинирането на началните куестове.

#### [MODIFY] [Converters.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/data/Converters.kt)
- Премахване на `fromEnergyLevel` и `toEnergyLevel` конверторите.

#### [MODIFY] [QuestDao.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/data/QuestDao.kt)
- Обновяване на SQL заявката в `getRandomQuest` за премахване на филтъра по енергия.
- Премахване на параметъра `energyLevel` от функцията `getRandomQuest`.

#### [MODIFY] [QuestRepository.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/data/QuestRepository.kt)
- Премахване на параметъра `energyLevel` от функцията `getRandomQuest`.

---

### [UI/ViewModel Layer]

#### [MODIFY] [QuestViewModel.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/viewmodel/QuestViewModel.kt)
- Премахване на `selectedEnergyLevel` от `QuestUiState`.
- Изтриване на функцията `setEnergyLevel`.
- Обновяване на `generateQuest` за извикване на репозиторито без енергийно ниво.

#### [MODIFY] [QuestGeneratorScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/screens/QuestGeneratorScreen.kt)
- Премахване на UI компонентите за избор на енергийно ниво (chips).
- Премахване на показването на енергийното ниво в `ActiveQuestCard`.

#### [MODIFY] [ProfileScreen.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/main/java/com/ruben/randomquest/ui/screens/ProfileScreen.kt)
- Премахване на показването на енергийното ниво в списъка с история на завършените куестове.

---

### [Tests]

#### [MODIFY] [QuestRepositoryTest.kt](file:///C:/Users/Ruben/Desktop/MobileApps/RandomQuest/app/src/test/java/com/ruben/randomquest/data/QuestRepositoryTest.kt)
- Обновяване на фалшивия DAO обект и тестовете за съответствие с новия интерфейс на `QuestDao`.

## План за проверка

### Автоматизирани тестове
- Изпълнение на `app:testDebugUnitTest` за проверка на репозиторито.

### Ръчна проверка
- Стартиране на приложението.
- Проверка дали екранът за генериране изглежда коректно без филтъра за енергия.
- Проверка дали куестовете се генерират успешно само по категория.
- Проверка дали в историята (Профил) куестовете се показват без енергийно ниво.
