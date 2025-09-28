package com.lianglliu.hermoodbarometer

import com.lianglliu.hermoodbarometer.model.NetworkCurrencyExchangeRate

interface AppNetworkDataSource {

    suspend fun getCurrencyExchangeRate(
        baseCurrencyCode: String,
        targetCurrencyCode: String,
    ): NetworkCurrencyExchangeRate
}
