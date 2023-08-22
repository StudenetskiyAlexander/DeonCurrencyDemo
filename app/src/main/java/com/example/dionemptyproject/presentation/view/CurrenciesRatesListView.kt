package com.example.dionemptyproject.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dionemptyproject.domain.entity.Currencies
import com.example.dionemptyproject.domain.entity.CurrencyRate
import com.example.dionemptyproject.presentation.theme.DionEmptyProjectTheme

@Composable
fun CurrenciesRatesListView(
    modifier: Modifier = Modifier,
    currenciesRates: List<CurrencyRate>,
    onAllCurrenciesRatesUpdateClick: () -> Unit,
    onSingleCurrencyUpdateClick: (Currencies) -> Unit
) {
    Column {
        Button(onClick = { onAllCurrenciesRatesUpdateClick() }) {
            Text(text = "refresh all")
        }

        currenciesRates.forEach { rate ->
            when (rate) {
                is CurrencyRate.Loading -> LoadingAnimation(circleSize = 20.dp)
                is CurrencyRate.Rate -> {
                    CurrencyRateView(modifier, rate) {
                        onSingleCurrencyUpdateClick(rate.currency)
                    }
                }

                is CurrencyRate.Error -> {
                    Text(
                        text = "Error loading ${rate.currency}"
                    )
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun CurrenciesRateListPreview() {
    val rubRate = CurrencyRate.Rate(Currencies.RUB, "100.0")
    val eurRate = CurrencyRate.Rate(Currencies.EUR, "130.0")
    val loadingRate = CurrencyRate.Loading(Currencies.USD)

    DionEmptyProjectTheme {
        CurrenciesRatesListView(
            currenciesRates = listOf(rubRate, loadingRate, eurRate),
            onAllCurrenciesRatesUpdateClick = {},
            onSingleCurrencyUpdateClick = {})
    }
}