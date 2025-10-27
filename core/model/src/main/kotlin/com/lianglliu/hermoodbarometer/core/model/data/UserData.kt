package com.lianglliu.hermoodbarometer.core.model.data

data class UserData(
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val reminderStatus: Boolean,
    val reminderTime: String = "09:00", // HH:mm format
)