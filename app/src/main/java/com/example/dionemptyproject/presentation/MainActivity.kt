package com.example.dionemptyproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dionemptyproject.presentation.vm.CurrencyScreenViewModel
import com.example.dionemptyproject.presentation.view.LoadingAnimation
import com.example.dionemptyproject.presentation.entity.CurrencyScreenState
import com.example.dionemptyproject.presentation.theme.DionEmptyProjectTheme
import com.example.dionemptyproject.presentation.view.CurrenciesRatesListView
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //TODO Переделать на DI
            //Вообще так не делают, конечно. Но DI - это отдельная тема, потом переделаем
            val viewModel = CurrencyScreenViewModel(LocalContext.current)
            val state by viewModel.state.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(true) {
                viewModel.initialLoad()
            }

            DionEmptyProjectTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    when (val localState = state) {
                        CurrencyScreenState.Loading -> LoadingAnimation(circleSize = 100.dp)
                        is CurrencyScreenState.Completed -> {
                            CurrenciesRatesListView(
                                currenciesRates = localState.rates,
                                onAllCurrenciesRatesUpdateClick = {
                                    coroutineScope.launch { viewModel.updateAllCurrenciesRate() }
                                },
                                onSingleCurrencyUpdateClick = {
                                    coroutineScope.launch { viewModel.updateSingleCurrencyRate(it) }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
