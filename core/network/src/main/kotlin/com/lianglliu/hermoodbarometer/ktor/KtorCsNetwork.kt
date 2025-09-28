package com.lianglliu.hermoodbarometer.ktor

import com.lianglliu.hermoodbarometer.AppNetworkDataSource
import com.lianglliu.hermoodbarometer.model.NetworkCurrencyExchangeRate
import com.lianglliu.hermoodbarometer.resource.CurrencyRateResource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class KtorCsNetwork @Inject constructor(
    private val httpClient: HttpClient,
) : AppNetworkDataSource {

    override suspend fun getCurrencyExchangeRate(
        baseCurrencyCode: String,
        targetCurrencyCode: String,
    ): NetworkCurrencyExchangeRate {
        return httpClient
            .get(CurrencyRateResource(baseCurrencyCode, targetCurrencyCode))
            .body<NetworkResponse<NetworkCurrencyExchangeRate>>()
            .data
    }
}

@Serializable
private data class NetworkResponse<T>(
    val data: T,
)