package com.lianglliu.hermoodbarometer.feature.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.domain.EmotionStatistics
import com.lianglliu.hermoodbarometer.core.ui.component.LoadingIndicator
import com.lianglliu.hermoodbarometer.core.ui.component.StatisticItem

/**
 * 统计数据卡片组件
 */
@Composable
fun StatisticsCard(
    statistics: EmotionStatistics?,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.mood_trends),
                style = MaterialTheme.typography.titleMedium
            )

            if (isLoading) {
                LoadingIndicator()
            } else {
                if (statistics != null) {
                    MetricsColumn(
                        totalRecords = statistics.totalRecords,
                        averageIntensity = statistics.averageIntensity,
                        mostFrequentEmotion = statistics.mostFrequentEmotion
                    )
                } else {
                    MetricsColumn(
                        totalRecords = 0,
                        averageIntensity = Float.NaN,
                        mostFrequentEmotion = null
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricsColumn(
    totalRecords: Int,
    averageIntensity: Float,
    mostFrequentEmotion: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatisticItem(
            label = stringResource(R.string.total_records),
            value = totalRecords.toString()
        )
        StatisticItem(
            label = stringResource(R.string.average_mood),
            value = if (averageIntensity.isNaN()) stringResource(R.string.no_data) else "%.1f".format(
                averageIntensity
            )
        )
        StatisticItem(
            label = stringResource(R.string.most_frequent_emotion),
            value = mostFrequentEmotion ?: stringResource(R.string.no_data)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsCardEmptyPreview() {
    StatisticsCard(statistics = null, isLoading = false)
}