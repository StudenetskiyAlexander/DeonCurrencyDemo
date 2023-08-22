package com.example.dionemptyproject.presentation.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dionemptyproject.data.CurrenciesVolleyRepository
import com.example.dionemptyproject.data.CurrenciesRetrofitRepository
import com.example.dionemptyproject.domain.entity.Currencies
import com.example.dionemptyproject.domain.entity.CurrencyRate
import com.example.dionemptyproject.presentation.entity.CurrencyScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class CurrencyScreenViewModel(
    //TODO Не храни контекст как поле, засунь его через DI в репозиторий
    private val context: Context
) : ViewModel() {

    //TODO Через DI
    private val repositoryValley = CurrenciesVolleyRepository()
    private val repositoryRetrofit = CurrenciesRetrofitRepository()

    private val allCurrencies = listOf(Currencies.RUB, Currencies.USD, Currencies.EUR)

    private val _state: MutableStateFlow<CurrencyScreenState> = MutableStateFlow(CurrencyScreenState.Loading)

    val state: StateFlow<CurrencyScreenState> = _state.asStateFlow()

    suspend fun initialLoad() {
        updateAllCurrenciesRate()
    }

    suspend fun updateAllCurrenciesRate() {
        _state.value = CurrencyScreenState.Loading

        allCurrencies.forEach {
            updateSingleCurrencyRate(it)
        }
    }

    suspend fun updateSingleCurrencyRate(currency: Currencies) {
        _state.value = getStateChangeSingleCurrencyState(_state.value, CurrencyRate.Loading(currency))

//        repositoryValley.getCurrencyData(context, currency) { newRate ->
//            _state.value = getStateChangeSingleCurrencyState(
//                oldState = _state.value,
//                newRate = newRate
//            )
//        }

        _state.value = getStateChangeSingleCurrencyState(
            oldState = _state.value,
            newRate = repositoryRetrofit.getCurrencyData(currency)
        )
    }

    private fun getStateChangeSingleCurrencyState(oldState: CurrencyScreenState,
                                                  newRate: CurrencyRate): CurrencyScreenState {
        return when (oldState) {
            // Если еще загружается - добавляем первую валюту
            CurrencyScreenState.Loading -> CurrencyScreenState.Completed(
                rates = listOf(newRate)
            )

            // Если что-то уже загрузилось - добавляем новый либо обновляем существующий
            is CurrencyScreenState.Completed -> {
                oldState.copy(rates = getRatesWithAddOrUpdateRate(oldState.rates, newRate))
            }
        }
    }

    private fun getRatesWithAddOrUpdateRate(rates: List<CurrencyRate>, newRate: CurrencyRate): List<CurrencyRate> {
        // Копируем список, чтобы вносить изменения
        val result: MutableList<CurrencyRate> = rates.toMutableList()
        // Ищем элемент с той же валютой
        when (val index = rates.indexOfFirst { it.currency == newRate.currency }) {
            // -1 значит не нашли - добавляем в список новый
            -1 -> result.add(newRate)
            // Такой уже был - заменяем на новый
            else -> result[index] = newRate
        }
        return result
    }
}