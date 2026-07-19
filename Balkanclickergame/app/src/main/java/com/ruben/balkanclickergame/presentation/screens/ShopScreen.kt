package com.ruben.balkanclickergame.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ruben.balkanclickergame.R
import com.ruben.balkanclickergame.presentation.components.UpgradeCard
import com.ruben.balkanclickergame.presentation.viewmodels.ShopViewModel
import com.ruben.balkanclickergame.presentation.viewmodels.UpgradeUiModel

@Composable
fun ShopScreen(
    viewModel: ShopViewModel,
    modifier: Modifier = Modifier
) {
    val shopItems by viewModel.shopItems.collectAsState()

    ShopContent(
        shopItems = shopItems,
        onBuyUpgrade = { viewModel.purchaseUpgrade(it) },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun ShopContent(
    shopItems: List<UpgradeUiModel>,
    onBuyUpgrade: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.shop_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
        }

        items(shopItems, key = { it.upgrade.id }) { uiModel ->
            UpgradeCard(
                uiModel = uiModel,
                onBuyClick = { onBuyUpgrade(uiModel.upgrade.id) }
            )
        }
    }
}
