package com.example.dionemptyproject.data

import com.example.dionemptyproject.data.retrofit.CurrencyApi
import com.example.dionemptyproject.data.retrofit.RetrofitHelper
import com.example.dionemptyproject.domain.entity.Currencies
import com.example.dionemptyproject.domain.entity.CurrencyRate
import com.example.dionemptyproject.presentation.entity.CurrencyApiErrorCode

class CurrenciesRetrofitRepository {

    suspend fun getCurrencyData(currency: Currencies): CurrencyRate {
        val quotesApi = RetrofitHelper.getInstance().create(CurrencyApi::class.java)

        return mapCurrencyResponseToRate(
            quotesApi.getRates().body()?.listOfRates,
            currency
        )
    }

    private fun mapStringCurrencyToCurrency(currencyString: String): Currencies? {
        return Currencies.values().firstOrNull { it.name == currencyString }
    }

    private fun mapCurrencyResponseToRate(currencyResponse: Map<String, Double>?,
                                          findingCurrency: Currencies): CurrencyRate {

        val response =
            currencyResponse ?: return CurrencyRate.Error(findingCurrency, CurrencyApiErrorCode.RESPONSE_ERROR)

        return response.map {
            CurrencyRate.Rate(
                currency = mapStringCurrencyToCurrency(it.key)
                    ?: return@map null,
                rate = it.value.toString()
            )
        }.filterNotNull().firstOrNull { it.currency == findingCurrency } ?: CurrencyRate.Error(
            findingCurrency, CurrencyApiErrorCode.FAILED_TO_FIND_CURRENCY
        )
    }
}