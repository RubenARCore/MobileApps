package com.ruben.randomquest.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ruben.randomquest.R
import com.ruben.randomquest.model.QuestCategory

@Composable
fun QuestCategory.getLabel(): String {
    return when (this) {
        QuestCategory.SOCIAL -> stringResource(R.string.cat_social)
        QuestCategory.FUN -> stringResource(R.string.cat_fun)
        QuestCategory.FITNESS -> stringResource(R.string.cat_fitness)
        QuestCategory.LOVE -> stringResource(R.string.cat_love)
        QuestCategory.EXTREME -> stringResource(R.string.cat_extreme)
        QuestCategory.KNOWLEDGE -> stringResource(R.string.cat_knowledge)
    }
}
