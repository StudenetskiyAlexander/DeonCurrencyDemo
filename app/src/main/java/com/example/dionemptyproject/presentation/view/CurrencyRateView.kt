package com.example.dionemptyproject.presentation.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dionemptyproject.domain.entity.Currencies
import com.example.dionemptyproject.domain.entity.CurrencyRate
import com.example.dionemptyproject.presentation.theme.DionEmptyProjectTheme

@Composable
fun CurrencyRateView(
    modifier: Modifier = Modifier,
    currencyRate: CurrencyRate.Rate,
    onRefreshClicked: (CurrencyRate) -> Unit
) {
    Row {
        Text(
            text = "${currencyRate.currency} = ${currencyRate.rate}",
            modifier = modifier
        )
        IconButton(
            onClick = { onRefreshClicked(currencyRate) },
            modifier = modifier.size(20.dp)
        ) {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyRatePreview() {
    val rubRate = CurrencyRate.Rate(Currencies.RUB, "100.0")

    DionEmptyProjectTheme {
        CurrencyRateView(currencyRate = rubRate, onRefreshClicked = {})
    }
}