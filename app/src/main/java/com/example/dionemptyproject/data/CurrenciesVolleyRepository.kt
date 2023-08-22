package com.example.dionemptyproject.data

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dionemptyproject.data.retrofit.API_KEY_CURRENCY
import com.example.dionemptyproject.data.retrofit.API_URL
import com.example.dionemptyproject.domain.entity.Currencies
import com.example.dionemptyproject.domain.entity.CurrencyRate
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.lang.Exception

class CurrenciesVolleyRepository {

    suspend fun getCurrencyData(context: Context, currency: Currencies, onResult: (CurrencyRate) -> Unit) {
        val url = "$API_URL$API_KEY_CURRENCY"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { requestResult ->
                onResult(parseCurrencyData(requestResult, currency) ?: CurrencyRate.Error(currency))
            },
            { error ->
                // TODO Map error from volley to CurrencyApiErrorCode
                onResult(CurrencyRate.Error(currency))
            }
        )
        queue.add(request)
    }

    private fun parseCurrencyData(requestResult: String, currency: Currencies): CurrencyRate? {
        val obj = JSONObject(requestResult)
        return try {
            CurrencyRate.Rate(
                currency = currency,
                rate =  obj.getJSONObject("data").getString(currency.name)
            )
        } catch (e: Exception) {
            null
        }
    }
}