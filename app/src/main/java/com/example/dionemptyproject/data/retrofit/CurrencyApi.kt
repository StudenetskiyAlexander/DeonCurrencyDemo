package com.example.dionemptyproject.data.retrofit

import com.example.dionemptyproject.data.entity.CurrencyRateResponse
import retrofit2.Response
import retrofit2.http.GET

const val API_KEY_CURRENCY = "fca_live_xlJOAdBc3gF8c5MmDinU2EJuWYyryvYuXIp5RPcl"
const val API_URL = "https://api.freecurrencyapi.com/v1/latest?apikey="

interface CurrencyApi {

    @GET("/v1/latest?apikey=$API_KEY_CURRENCY")
    suspend fun getRates(): Response<CurrencyRateResponse>
}
