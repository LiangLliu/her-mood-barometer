package com.lianglliu.hermoodbarometer.util

import kotlinx.coroutines.flow.Flow

interface AppLocaleManager {

    val currentLocale: Flow<String>

    fun updateLocale(languageCode: String)
}