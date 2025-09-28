package com.lianglliu.hermoodbarometer.resource

import io.ktor.resources.Resource

@Resource("{base}")
internal class CurrencyRateResource(
    val base: String,
    val target: String,
)