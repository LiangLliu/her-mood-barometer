package com.lianglliu.hermoodbarometer.core.model.data

enum class Language(
    val code: String,
    val displayName: String,
) {
    SYSTEM("system", "Auto"),
    ENGLISH("en", "English"),
    JAPANESE("ja", "日本語"),
    KOREAN("ko", "한국어"),
    CHINESE_SIMPLIFIED("zh", "简体中文"),
    CHINESE_TRADITIONAL("zh-TW", "繁體中文"),
}