package com.lianglliu.hermoodbarometer.core.designsystem.icon

/** Emoji constants used across the app. */
object Emojis {
    // Navigation & UI symbols
    const val NAV_PREV = "\u2039" // ‹
    const val NAV_NEXT = "\u203A" // ›
    const val CLOSE = "\u00D7" // ×
    const val MIDDLE_DOT = "\u00B7" // ·
    const val PLUS = "+"

    // Stat / display icons
    const val CALENDAR = "\uD83D\uDCC5" // 📅
    const val CHART_UP = "\uD83D\uDCC8" // 📈
    const val STATISTICS = "\uD83D\uDCCA" // 📊
    const val SMILE = "\uD83D\uDE0A" // 😊
    const val ALARM = "\u23F0" // ⏰
    const val NOTEBOOK = "\uD83D\uDCD4" // 📔
    const val GEAR = "\u2699\uFE0F" // ⚙️
    const val TROPHY = "\uD83C\uDFC6" // 🏆
    const val BULB = "\uD83D\uDCA1" // 💡

    // Settings icons
    const val GLOBE = "\uD83C\uDF10" // 🌐
    const val PALETTE = "\uD83C\uDFA8" // 🎨
    const val MASKS = "\uD83C\uDFAD" // 🎭
    const val BELL = "\uD83D\uDD14" // 🔔
    const val INFO = "\u2139\uFE0F" // ℹ️
    const val PAGE = "\uD83D\uDCC4" // 📄
    const val NUMBERS = "\uD83D\uDD22" // 🔢

    // Weather icons
    const val SUNNY = "\u2600\uFE0F" // ☀️
    const val CLOUDY = "\u2601\uFE0F" // ☁️
    const val RAINY = "\uD83C\uDF27\uFE0F" // 🌧️
    const val SNOWY = "\u2744\uFE0F" // ❄️

    // Activity icons
    const val BRIEFCASE = "\uD83D\uDCBC" // 💼
    const val RUNNING = "\uD83C\uDFC3" // 🏃
    const val BOOK = "\uD83D\uDCD6" // 📖
    const val PEOPLE = "\uD83D\uDC65" // 👥
    const val GAME = "\uD83C\uDFAE" // 🎮
    const val SLEEPING = "\uD83D\uDE34" // 😴

    /**
     * Positive emotion emojis used for mood index calculation. These are the predefined emotions
     * considered "positive" including happy, excited, calm, grateful, confident, touched, and
     * celebrating.
     */
    val POSITIVE_EMOTIONS =
        setOf(
            SMILE, // 😊 happy
            "\uD83E\uDD29", // 🤩 excited
            "\uD83D\uDE0C", // 😌 calm
            "\uD83D\uDE4F", // 🙏 grateful
            "\uD83D\uDE0E", // 😎 confident
            "\uD83E\uDD79", // 🥹 touched
            "\uD83E\uDD73", // 🥳 celebrating
        )

    /**
     * Mood categorization for statistics overview card. Broader than POSITIVE_EMOTIONS — includes
     * love-related emojis (❤️, 🥰) but excludes celebration emojis (🥹, 🥳) that are less common in
     * daily tracking.
     */
    val MOOD_POSITIVE =
        setOf(
            "\uD83D\uDE0A", // 😊
            "\uD83E\uDD29", // 🤩
            "\uD83D\uDE0C", // 😌
            "\uD83D\uDE4F", // 🙏
            "\u2764\uFE0F", // ❤️
            "\uD83E\uDD70", // 🥰
            "\uD83D\uDE0E", // 😎
        )

    val MOOD_STABLE =
        setOf(
            "\uD83D\uDE15", // 😕
            "\uD83D\uDE34", // 😴
            "\uD83D\uDE10", // 😐
            "\uD83E\uDD14", // 🤔
        )

    val MOOD_NEGATIVE =
        setOf(
            "\uD83D\uDE22", // 😢
            "\uD83D\uDE21", // 😡
            "\uD83D\uDE30", // 😰
            "\uD83D\uDE14", // 😔
            "\uD83D\uDE2D", // 😭
            "\uD83E\uDD2F", // 🤯
            "\uD83D\uDE29", // 😩
            "\uD83E\uDD2C", // 🤬
        )

    // Bar chart color group sets (used in statistics distribution chart)
    val BAR_JOYFUL =
        setOf(
            "\uD83D\uDE0A", // 😊
            "\uD83D\uDE04", // 😄
            "\uD83E\uDD29", // 🤩
            "\uD83D\uDE01", // 😁
        )

    val BAR_PEACEFUL =
        setOf(
            "\uD83D\uDE0C", // 😌
            "\uD83D\uDE4F", // 🙏
            "\uD83D\uDE07", // 😇
            "\uD83E\uDD70", // 🥰
        )

    val BAR_INTENSE =
        setOf(
            "\uD83D\uDE30", // 😰
            "\uD83D\uDE21", // 😡
            "\uD83E\uDD2F", // 🤯
            "\uD83D\uDE24", // 😤
        )

    val BAR_THOUGHTFUL =
        setOf(
            "\uD83D\uDE14", // 😔
            "\uD83D\uDE35", // 😵
            "\uD83E\uDD14", // 🤔
            "\uD83D\uDE36", // 😶
        )

    val BAR_SAD =
        setOf(
            "\uD83D\uDE22", // 😢
            "\uD83D\uDE1E", // 😞
            "\uD83E\uDD7A", // 🥺
            "\uD83D\uDE2D", // 😭
        )
}
