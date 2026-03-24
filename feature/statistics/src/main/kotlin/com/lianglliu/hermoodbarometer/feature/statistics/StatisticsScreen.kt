package com.lianglliu.hermoodbarometer.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.core.designsystem.component.CardLabel
import com.lianglliu.hermoodbarometer.core.designsystem.component.ScreenHeader
import com.lianglliu.hermoodbarometer.core.designsystem.component.StatRow
import com.lianglliu.hermoodbarometer.core.designsystem.component.WarmCard
import com.lianglliu.hermoodbarometer.core.designsystem.icon.Emojis
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedColorScheme
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.model.data.EmotionCount
import com.lianglliu.hermoodbarometer.core.model.data.EmotionStatistics
import com.lianglliu.hermoodbarometer.core.model.data.TimeRange
import com.lianglliu.hermoodbarometer.core.ui.component.LoadingState
import com.lianglliu.hermoodbarometer.core.ui.component.emojiToLocalizedName

@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = hiltViewModel()) {
    val uiState by viewModel.statisticsUiState.collectAsStateWithLifecycle()

    StatisticsScreen(statisticsUiState = uiState, updateTimeRange = viewModel::updateTimeRange)
}

@Composable
private fun StatisticsScreen(
    statisticsUiState: StatisticsUiState,
    updateTimeRange: (TimeRange) -> Unit,
) {
    Scaffold(
        topBar = {
            ScreenHeader(
                title = stringResource(R.string.statistics_title),
                subtitle = stringResource(R.string.statistics_subtitle),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        when (statisticsUiState) {
            StatisticsUiState.Loading -> {
                LoadingState(modifier = Modifier.fillMaxSize().padding(paddingValues))
            }

            is StatisticsUiState.Success -> {
                Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                    TimeRangeSelector(
                        selectedTimeRange = statisticsUiState.timeRange,
                        onTimeRangeSelected = updateTimeRange,
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding =
                            PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        item { OverviewCard(statistics = statisticsUiState.statistics) }

                        item { EmotionDistributionCard(statistics = statisticsUiState.statistics) }

                        item { ComparisonCard(uiState = statisticsUiState) }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeRangeSelector(
    selectedTimeRange: TimeRange,
    onTimeRangeSelected: (TimeRange) -> Unit,
) {
    val colors = ExtendedTheme.colors
    val scrollState = rememberScrollState()

    Row(
        modifier =
            Modifier.fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(bottom = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        TimeRange.entries.forEach { timeRange ->
            val isSelected = timeRange == selectedTimeRange
            val label =
                when (timeRange) {
                    TimeRange.LAST_WEEK -> stringResource(R.string.last_week)
                    TimeRange.LAST_MONTH -> stringResource(R.string.last_month)
                    TimeRange.LAST_3_MONTHS -> stringResource(R.string.last_3_months)
                    TimeRange.LAST_SIX_MONTHS -> stringResource(R.string.last_six_months)
                    TimeRange.LAST_YEAR -> stringResource(R.string.last_year)
                }

            Box(
                modifier =
                    Modifier.clip(RoundedCornerShape(100.dp))
                        .background(if (isSelected) colors.accentBg else colors.warmBackground)
                        .border(
                            1.dp,
                            if (isSelected) colors.accentSoft else colors.borderLight,
                            RoundedCornerShape(100.dp),
                        )
                        .clickable { onTimeRangeSelected(timeRange) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) colors.accent else colors.textMuted,
                )
            }
        }
    }
}

// Data class to hold comparison row data (emoji + percentages + change string)
private data class ComparisonRowData(
    val emoji: String,
    val currentPct: String,
    val prevPct: String,
    val changeStr: String,
    val currentPctInt: Int,
)

@Composable
private fun ComparisonCard(uiState: StatisticsUiState.Success) {
    val colors = ExtendedTheme.colors

    val currentPeriodName =
        when (uiState.timeRange) {
            TimeRange.LAST_WEEK -> stringResource(R.string.period_this_week)
            TimeRange.LAST_MONTH -> stringResource(R.string.period_this_month)
            TimeRange.LAST_3_MONTHS -> stringResource(R.string.period_last_3_months)
            TimeRange.LAST_SIX_MONTHS -> stringResource(R.string.period_last_6_months)
            TimeRange.LAST_YEAR -> stringResource(R.string.period_this_year)
        }

    val previousPeriodName =
        when (uiState.timeRange) {
            TimeRange.LAST_WEEK -> stringResource(R.string.period_previous_week)
            TimeRange.LAST_MONTH -> stringResource(R.string.period_previous_month)
            TimeRange.LAST_3_MONTHS -> stringResource(R.string.period_previous_3_months)
            TimeRange.LAST_SIX_MONTHS -> stringResource(R.string.period_previous_6_months)
            TimeRange.LAST_YEAR -> stringResource(R.string.period_last_year_comparison)
        }

    val currentDistribution = uiState.statistics.emotionDistribution
    val prevDistribution = uiState.previousStatistics?.emotionDistribution ?: emptyList()

    // Memoize expensive list operations (map, filter, sort, take)
    // emojiToLocalizedName is called at render time since it's @Composable
    val comparisonData =
        remember(currentDistribution, prevDistribution) {
            val allEmojis =
                (currentDistribution.map { it.emotionEmoji } +
                        prevDistribution.map { it.emotionEmoji })
                    .distinct()

            allEmojis
                .map { emoji ->
                    val currentPercentage =
                        currentDistribution.find { it.emotionEmoji == emoji }?.percentage ?: 0f
                    val prevPercentage =
                        prevDistribution.find { it.emotionEmoji == emoji }?.percentage ?: 0f

                    val change = currentPercentage - prevPercentage
                    val changeStr =
                        if (change > 0) "+${change.toInt()}%"
                        else if (change < 0) "${change.toInt()}%" else "0%"

                    ComparisonRowData(
                        emoji = emoji,
                        currentPct = "${currentPercentage.toInt()}%",
                        prevPct = "${prevPercentage.toInt()}%",
                        changeStr = changeStr,
                        currentPctInt = currentPercentage.toInt(),
                    )
                }
                .filter { it.currentPct != "0%" || it.prevPct != "0%" }
                .sortedByDescending { it.currentPctInt }
                .take(5)
        }

    if (comparisonData.isEmpty()) return

    WarmCard(modifier = Modifier.fillMaxWidth()) {
        CardLabel(
            text =
                stringResource(
                    R.string.emotion_comparison_label,
                    currentPeriodName,
                    previousPeriodName,
                )
        )

        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.comparison_header_emotion),
                style =
                    MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                    ),
                color = colors.textHint,
                modifier = Modifier.weight(1.5f),
            )
            Text(
                text = currentPeriodName,
                style =
                    MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                    ),
                color = colors.textHint,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = previousPeriodName,
                style =
                    MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                    ),
                color = colors.textHint,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(R.string.comparison_header_change),
                style =
                    MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                    ),
                color = colors.textHint,
                modifier = Modifier.weight(1f),
            )
        }
        HorizontalDivider(thickness = 1.5.dp, color = colors.border)
        Spacer(modifier = Modifier.height(4.dp))

        comparisonData.forEachIndexed { index, rowData ->
            val emotionTitle = "${rowData.emoji} ${emojiToLocalizedName(rowData.emoji)}"
            val isPositive = rowData.changeStr.startsWith("+")
            val isNeutral = rowData.changeStr == "0%"
            val changeColor =
                if (isPositive) colors.sage else if (isNeutral) colors.textHint else colors.rose

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = emotionTitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                    color = colors.textSecondary,
                    modifier = Modifier.weight(1.5f),
                )
                Text(
                    text = rowData.currentPct,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                    color = colors.textSecondary,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = rowData.prevPct,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                    color = colors.textSecondary,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = rowData.changeStr,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                    color = changeColor,
                    modifier = Modifier.weight(1f),
                )
            }
            if (index < comparisonData.lastIndex) {
                HorizontalDivider(thickness = 1.dp, color = colors.borderLight)
            }
        }
    }
}

@Composable
private fun OverviewCard(statistics: EmotionStatistics) {
    val moodPositive = stringResource(R.string.mood_positive)
    val moodStable = stringResource(R.string.mood_stable)
    val moodNegative = stringResource(R.string.mood_negative)
    val moodIntense = stringResource(R.string.mood_intense)

    val averageMoodVal =
        if (statistics.totalRecords == 0) "-"
        else {
            val mostFreqEmoji = statistics.mostFrequentEmotion?.emotionEmoji
            when {
                mostFreqEmoji in Emojis.MOOD_POSITIVE -> moodPositive
                mostFreqEmoji in Emojis.MOOD_STABLE -> moodStable
                mostFreqEmoji in Emojis.MOOD_NEGATIVE -> moodNegative
                else -> if (statistics.averageIntensity >= 3.0f) moodIntense else moodStable
            }
        }

    WarmCard(modifier = Modifier.fillMaxWidth()) {
        CardLabel(text = stringResource(R.string.overview_label))
        StatRow(
            emoji = Emojis.CHART_UP,
            label = stringResource(R.string.total_records),
            value = statistics.totalRecords.toString(),
        )
        StatRow(
            emoji = Emojis.SMILE,
            label = stringResource(R.string.average_mood),
            value = averageMoodVal,
        )
        StatRow(
            emoji = Emojis.TROPHY,
            label = stringResource(R.string.most_common_emotion),
            value =
                statistics.mostFrequentEmotion?.emotionEmoji?.let { emojiToLocalizedName(it) }
                    ?: "-",
            showDivider = false,
        )
    }
}

@Composable
private fun EmotionDistributionCard(statistics: EmotionStatistics) {
    val colors = ExtendedTheme.colors

    WarmCard(modifier = Modifier.fillMaxWidth()) {
        CardLabel(text = stringResource(R.string.emotion_distribution))

        statistics.emotionDistribution.take(5).forEach { emotionCount ->
            BarChartItem(emotionCount = emotionCount, colors = colors)
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Insight box
        val mostFrequent = statistics.mostFrequentEmotion
        if (mostFrequent != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier =
                    Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(colors.accentBg)
                        .border(1.dp, colors.accent.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                        .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(text = Emojis.BULB, fontSize = 16.sp)
                val mostFrequentName = emojiToLocalizedName(mostFrequent.emotionEmoji)
                Text(
                    text =
                        stringResource(
                            R.string.emotion_insight_positive,
                            mostFrequentName,
                            mostFrequent.percentage.toInt(),
                        ),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                    color = colors.textSecondary,
                    lineHeight = 20.sp,
                )
            }
        }
    }
}

@Composable
private fun BarChartItem(emotionCount: EmotionCount, colors: ExtendedColorScheme) {
    val barColors = emojiToBarColors(emotionCount.emotionEmoji, colors)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Label
        Row(
            modifier = Modifier.width(70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(text = emotionCount.emotionEmoji, fontSize = 14.sp)
            Text(
                text = emojiToLocalizedName(emotionCount.emotionEmoji),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                color = colors.textSecondary,
            )
        }

        // Bar track
        Box(
            modifier =
                Modifier.weight(1f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.warmBackground)
        ) {
            val fraction = (emotionCount.percentage / 100f).coerceIn(0f, 1f)
            Box(
                modifier =
                    Modifier.fillMaxWidth(fraction)
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.horizontalGradient(barColors))
            )
        }

        // Percentage
        Text(
            text = "${emotionCount.percentage.toInt()}%",
            style =
                MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                ),
            color = colors.textMuted,
            modifier = Modifier.width(38.dp),
            textAlign = TextAlign.End,
        )
    }
}

private fun emojiToBarColors(emoji: String, colors: ExtendedColorScheme): List<Color> =
    when {
        emoji in Emojis.BAR_JOYFUL -> listOf(colors.amber, colors.amberSoft)
        emoji in Emojis.BAR_PEACEFUL -> listOf(colors.sage, colors.sageSoft)
        emoji in Emojis.BAR_INTENSE -> listOf(colors.accent, colors.accentSoft)
        emoji in Emojis.BAR_THOUGHTFUL -> listOf(colors.lavender, colors.lavenderSoft)
        emoji in Emojis.BAR_SAD -> listOf(colors.rose, colors.roseSoft)
        else -> listOf(colors.accent, colors.accentSoft)
    }
