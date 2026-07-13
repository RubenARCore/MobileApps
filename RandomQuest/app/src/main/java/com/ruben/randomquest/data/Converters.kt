package com.ruben.randomquest.data

import androidx.room.TypeConverter
import com.ruben.randomquest.model.EnergyLevel
import com.ruben.randomquest.model.QuestCategory

class Converters {
    @TypeConverter
    fun fromCategory(value: QuestCategory): String = value.name

    @TypeConverter
    fun toCategory(value: String): QuestCategory = QuestCategory.valueOf(value)

    @TypeConverter
    fun fromEnergyLevel(value: EnergyLevel): String = value.name

    @TypeConverter
    fun toEnergyLevel(value: String): EnergyLevel = EnergyLevel.valueOf(value)
}
