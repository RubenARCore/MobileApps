package com.ruben.balkanclickergame.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ruben.balkanclickergame.R
import com.ruben.balkanclickergame.presentation.viewmodels.UpgradeUiModel

@Composable
fun UpgradeCard(
    uiModel: UpgradeUiModel,
    onBuyClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(uiModel.upgrade.nameResId),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.shop_item_level, uiModel.currentLevel),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Button(
                    onClick = onBuyClick,
                    enabled = uiModel.canAfford
                ) {
                    Text(stringResource(R.string.shop_item_buy, uiModel.currentCost))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(uiModel.upgrade.descriptionResId),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                if (uiModel.upgrade.passiveIncomePerLevel > 0) {
                    Text(
                        text = stringResource(R.string.shop_item_passive_benefit, uiModel.upgrade.passiveIncomePerLevel),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                if (uiModel.upgrade.clickPowerPerLevel > 0) {
                    Text(
                        text = stringResource(R.string.shop_item_click_benefit, uiModel.upgrade.clickPowerPerLevel),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}
