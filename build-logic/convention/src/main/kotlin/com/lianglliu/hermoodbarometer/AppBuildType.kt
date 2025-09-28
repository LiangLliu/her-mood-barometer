package com.lianglliu.hermoodbarometer

enum class AppBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}