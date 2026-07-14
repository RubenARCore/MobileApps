package com.ruben.randomquest.data

import com.ruben.randomquest.model.EnergyLevel
import com.ruben.randomquest.model.Quest
import com.ruben.randomquest.model.QuestCategory

object QuestDataSource {
    val initialQuests = listOf(
        // English Quests
        Quest(title = "Compliment a stranger", description = "Find someone you don't know and give them a genuine compliment.", category = QuestCategory.SOCIAL, energyLevel = EnergyLevel.LOW, language = "en"),
        Quest(title = "Call an old friend", description = "Pick up the phone and call someone you haven't spoken to in over 6 months.", category = QuestCategory.SOCIAL, energyLevel = EnergyLevel.MEDIUM, language = "en"),
        Quest(title = "Attend a local meetup", description = "Go to a community event or meetup related to your hobbies.", category = QuestCategory.SOCIAL, energyLevel = EnergyLevel.HIGH, language = "en"),
        
        Quest(title = "10 Pushups", description = "Drop and give me 10! Quick energy boost.", category = QuestCategory.FITNESS, energyLevel = EnergyLevel.LOW, language = "en"),
        Quest(title = "30-minute power walk", description = "Walk at a brisk pace around your neighborhood.", category = QuestCategory.FITNESS, energyLevel = EnergyLevel.MEDIUM, language = "en"),
        Quest(title = "Try a new workout class", description = "Go to a gym and try a class you've never done before (Yoga, HIIT, etc.).", category = QuestCategory.FITNESS, energyLevel = EnergyLevel.HIGH, language = "en"),
        
        Quest(title = "Doodle for 5 minutes", description = "Grab a pen and paper and let your hand move freely.", category = QuestCategory.CREATIVE, energyLevel = EnergyLevel.LOW, language = "en"),
        Quest(title = "Write a short poem", description = "Write at least 4 lines about how you feel right now.", category = QuestCategory.CREATIVE, energyLevel = EnergyLevel.MEDIUM, language = "en"),
        Quest(title = "Cook a new recipe", description = "Find a recipe online that you've never tried and make it for dinner.", category = QuestCategory.CREATIVE, energyLevel = EnergyLevel.HIGH, language = "en"),
        
        Quest(title = "1 minute of deep breathing", description = "Close your eyes and take 5 slow, deep breaths.", category = QuestCategory.MINDFUL, energyLevel = EnergyLevel.LOW, language = "en"),
        Quest(title = "Meditate for 10 minutes", description = "Find a quiet spot and focus on your breath.", category = QuestCategory.MINDFUL, energyLevel = EnergyLevel.MEDIUM, language = "en"),
        Quest(title = "Digital detox evening", description = "Turn off all screens 2 hours before bed.", category = QuestCategory.MINDFUL, energyLevel = EnergyLevel.HIGH, language = "en"),

        // Bulgarian Quests
        Quest(title = "Направи комплимент на непознат", description = "Намери някой, когото не познаваш, и му направи искрен комплимент.", category = QuestCategory.SOCIAL, energyLevel = EnergyLevel.LOW, language = "bg"),
        Quest(title = "Обади се на стар приятел", description = "Вземи телефона и се обади на някой, с когото не си говорил повече от 6 месеца.", category = QuestCategory.SOCIAL, energyLevel = EnergyLevel.MEDIUM, language = "bg"),
        Quest(title = "Посети местно събитие", description = "Отиди на събитие в общността или среща, свързана с твоите хобита.", category = QuestCategory.SOCIAL, energyLevel = EnergyLevel.HIGH, language = "bg"),
        
        Quest(title = "10 лицеви опори", description = "Падни за 10! Бърза доза енергия.", category = QuestCategory.FITNESS, energyLevel = EnergyLevel.LOW, language = "bg"),
        Quest(title = "30-минутна бърза разходка", description = "Разходи се с бързо темпо из квартала.", category = QuestCategory.FITNESS, energyLevel = EnergyLevel.MEDIUM, language = "bg"),
        Quest(title = "Опитай нов вид тренировка", description = "Отиди във фитнеса и опитай клас, който никога не си правил (Йога, HIIT и т.н.).", category = QuestCategory.FITNESS, energyLevel = EnergyLevel.HIGH, language = "bg"),
        
        Quest(title = "Драскай 5 минути", description = "Вземи химикалка и хартия и остави ръката си да се движи свободно.", category = QuestCategory.CREATIVE, energyLevel = EnergyLevel.LOW, language = "bg"),
        Quest(title = "Напиши кратко стихотворение", description = "Напиши поне 4 реда за това как се чувстваш в момента.", category = QuestCategory.CREATIVE, energyLevel = EnergyLevel.MEDIUM, language = "bg"),
        Quest(title = "Сготви по нова рецепта", description = "Намери рецепта онлайн, която никога не си опитвал, и я приготви за вечеря.", category = QuestCategory.CREATIVE, energyLevel = EnergyLevel.HIGH, language = "bg"),
        
        Quest(title = "1 минута дълбоко дишане", description = "Затвори очи и поеми 5 бавни, дълбоки вдишвания.", category = QuestCategory.MINDFUL, energyLevel = EnergyLevel.LOW, language = "bg"),
        Quest(title = "Медитирай за 10 минути", description = "Намери тихо място и се съсредоточи върху дишането си.", category = QuestCategory.MINDFUL, energyLevel = EnergyLevel.MEDIUM, language = "bg"),
        Quest(title = "Вечер без цифрови устройства", description = "Изключи всички екрани 2 часа преди лягане.", category = QuestCategory.MINDFUL, energyLevel = EnergyLevel.HIGH, language = "bg")
    )
}
