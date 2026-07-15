package com.ruben.randomquest.data

import com.ruben.randomquest.model.Quest
import com.ruben.randomquest.model.QuestCategory

object QuestDataSource {
    val initialQuests = listOf(
        // English Quests
        Quest(title = "Compliment a stranger", description = "Find someone you don't know and give them a genuine compliment.", category = QuestCategory.SOCIAL, language = "en"),
        Quest(title = "Tell a joke to someone", description = "Find a friend or colleague and tell them a funny joke.", category = QuestCategory.FUN, language = "en"),
        Quest(title = "10 Pushups", description = "Drop and give me 10! Quick energy boost.", category = QuestCategory.FITNESS, language = "en"),
        Quest(title = "Write a love note", description = "Write a small note of appreciation to someone you care about.", category = QuestCategory.LOVE, language = "en"),
        Quest(title = "Take a cold shower", description = "Switch to cold water for the last 30 seconds of your shower.", category = QuestCategory.EXTREME, language = "en"),
        Quest(title = "Learn a new fact", description = "Look up a random fact on Wikipedia and share it with someone.", category = QuestCategory.KNOWLEDGE, language = "en"),

        // Bulgarian Quests
        Quest(title = "Направи комплимент на непознат", description = "Намери някой, когото не познаваш, и му направи искрен комплимент.", category = QuestCategory.SOCIAL, language = "bg"),
        Quest(title = "Разкажи виц на някого", description = "Намери приятел или колега и му разкажи забавен виц.", category = QuestCategory.FUN, language = "bg"),
        Quest(title = "10 лицеви опори", description = "Падни за 10! Бърза доза енергия.", category = QuestCategory.FITNESS, language = "bg"),
        Quest(title = "Напиши любовно послание", description = "Напиши малка бележка на признателност към някой, на когото държиш.", category = QuestCategory.LOVE, language = "bg"),
        Quest(title = "Вземи студен душ", description = "Превключи на студена вода за последните 30 секунди от душа си.", category = QuestCategory.EXTREME, language = "bg"),
        Quest(title = "Научи нов факт", description = "Потърси случаен факт в Wikipedia и го сподели с някого.", category = QuestCategory.KNOWLEDGE, language = "bg")
    )
}
