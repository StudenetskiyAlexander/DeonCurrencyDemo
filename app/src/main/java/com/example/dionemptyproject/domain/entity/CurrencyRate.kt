package com.example.dionemptyproject.domain.entity

import com.example.dionemptyproject.presentation.entity.CurrencyApiErrorCode

sealed class CurrencyRate(
    open val currency: Currencies
) {

    data class Loading(override val currency: Currencies) : CurrencyRate(currency)

    data class Error(override val currency: Currencies,
                     val errorCode: CurrencyApiErrorCode = CurrencyApiErrorCode.UNKNOWN_ERROR) : CurrencyRate(currency)

    data class Rate(
        override val currency: Currencies,
        val rate: String
    ) : CurrencyRate(currency)
}
