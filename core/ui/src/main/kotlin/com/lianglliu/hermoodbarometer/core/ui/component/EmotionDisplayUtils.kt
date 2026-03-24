package com.lianglliu.hermoodbarometer.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lianglliu.hermoodbarometer.core.designsystem.icon.Emojis
import com.lianglliu.hermoodbarometer.core.locales.R as localesR

/**
 * Maps emoji unicode strings to localized emotion names.
 *
 * This is a @Composable function that uses stringResource() for localization. For non-Composable
 * contexts, see [EmotionProvider] which uses Context.getString().
 *
 * @param emoji The emoji unicode string (e.g., Emojis.SMILE for 😊)
 * @return The localized emotion name, or the original emoji if not found
 */
@Composable
fun emojiToLocalizedName(emoji: String): String =
    when (emoji) {
        // Positive emotions
        Emojis.SMILE -> stringResource(localesR.string.emotion_happy) // 😊
        "\uD83E\uDD29" -> stringResource(localesR.string.emotion_excited) // 🤩
        "\uD83D\uDE0C" -> stringResource(localesR.string.emotion_calm) // 😌
        "\uD83D\uDE4F" -> stringResource(localesR.string.emotion_grateful) // 🙏
        "\uD83D\uDE0E" -> stringResource(localesR.string.emotion_confident) // 😎
        "\uD83E\uDD79" -> stringResource(localesR.string.emotion_touched) // 🥹
        "\uD83E\uDD73" -> stringResource(localesR.string.emotion_celebrating) // 🥳

        // Negative emotions
        "\uD83D\uDE22" -> stringResource(localesR.string.emotion_sad) // 😢
        "\uD83D\uDE2D" -> stringResource(localesR.string.emotion_sad) // 😭 (also maps to sad)
        "\uD83D\uDE30" -> stringResource(localesR.string.emotion_anxious) // 😰
        "\uD83D\uDE21" -> stringResource(localesR.string.emotion_angry) // 😡
        "\uD83D\uDE20" -> stringResource(localesR.string.emotion_angry) // 😠 (also maps to angry)
        "\uD83D\uDE28" -> stringResource(localesR.string.emotion_fearful) // 😨
        "\uD83D\uDE24" -> stringResource(localesR.string.emotion_stressed) // 😤
        "\uD83D\uDE29" -> stringResource(localesR.string.emotion_helpless) // 😩
        "\uD83D\uDE33" -> stringResource(localesR.string.emotion_embarrassed) // 😳
        "\uD83E\uDD7A" -> stringResource(localesR.string.emotion_wronged) // 🥺

        // Neutral/Other emotions
        "\uD83D\uDE14" -> stringResource(localesR.string.emotion_lonely) // 😔
        Emojis.SLEEPING -> stringResource(localesR.string.emotion_tired) // 😴 (predefined)
        "\uD83D\uDE35" -> stringResource(localesR.string.emotion_tired) // 😵 (also maps to tired)
        "\uD83D\uDE15" -> stringResource(localesR.string.emotion_confused) // 😕 (predefined)
        "\uD83E\uDD14" ->
            stringResource(localesR.string.emotion_confused) // 🤔 (also maps to confused)
        "\uD83E\uDD2F" ->
            stringResource(localesR.string.emotion_confused) // 🤯 (also maps to confused)
        "\uD83D\uDE36" -> stringResource(localesR.string.emotion_silent) // 😶
        "\uD83D\uDE12" -> stringResource(localesR.string.emotion_dissatisfied) // 😒
        "\uD83D\uDE1E" ->
            stringResource(localesR.string.emotion_dissatisfied) // 😞 (also maps to dissatisfied)

        else -> emoji
    }
