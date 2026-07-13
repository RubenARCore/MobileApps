package com.ruben.randomquest.data

import com.ruben.randomquest.model.EnergyLevel
import com.ruben.randomquest.model.Quest
import com.ruben.randomquest.model.QuestCategory

object QuestDataSource {
    val initialQuests = listOf(
        // Social
        Quest(title = "Compliment a stranger", description = "Find someone you don't know and give them a genuine compliment.", category = QuestCategory.SOCIAL, energyLevel = EnergyLevel.LOW),
        Quest(title = "Call an old friend", description = "Pick up the phone and call someone you haven't spoken to in over 6 months.", category = QuestCategory.SOCIAL, energyLevel = EnergyLevel.MEDIUM),
        Quest(title = "Attend a local meetup", description = "Go to a community event or meetup related to your hobbies.", category = QuestCategory.SOCIAL, energyLevel = EnergyLevel.HIGH),
        
        // Fitness
        Quest(title = "10 Pushups", description = "Drop and give me 10! Quick energy boost.", category = QuestCategory.FITNESS, energyLevel = EnergyLevel.LOW),
        Quest(title = "30-minute power walk", description = "Walk at a brisk pace around your neighborhood.", category = QuestCategory.FITNESS, energyLevel = EnergyLevel.MEDIUM),
        Quest(title = "Try a new workout class", description = "Go to a gym and try a class you've never done before (Yoga, HIIT, etc.).", category = QuestCategory.FITNESS, energyLevel = EnergyLevel.HIGH),
        
        // Creative
        Quest(title = "Doodle for 5 minutes", description = "Grab a pen and paper and let your hand move freely.", category = QuestCategory.CREATIVE, energyLevel = EnergyLevel.LOW),
        Quest(title = "Write a short poem", description = "Write at least 4 lines about how you feel right now.", category = QuestCategory.CREATIVE, energyLevel = EnergyLevel.MEDIUM),
        Quest(title = "Cook a new recipe", description = "Find a recipe online that you've never tried and make it for dinner.", category = QuestCategory.CREATIVE, energyLevel = EnergyLevel.HIGH),
        
        // Mindful
        Quest(title = "1 minute of deep breathing", description = "Close your eyes and take 5 slow, deep breaths.", category = QuestCategory.MINDFUL, energyLevel = EnergyLevel.LOW),
        Quest(title = "Meditate for 10 minutes", description = "Find a quiet spot and focus on your breath.", category = QuestCategory.MINDFUL, energyLevel = EnergyLevel.MEDIUM),
        Quest(title = "Digital detox evening", description = "Turn off all screens 2 hours before bed.", category = QuestCategory.MINDFUL, energyLevel = EnergyLevel.HIGH)
    )
}
