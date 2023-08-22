package com.example.dionemptyproject.data.entity

import com.google.gson.annotations.SerializedName

data class CurrencyRateResponse(
    @SerializedName("data")
    val listOfRates: Map<String, Double>
)