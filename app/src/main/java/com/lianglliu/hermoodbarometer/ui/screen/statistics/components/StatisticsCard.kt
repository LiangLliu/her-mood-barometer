package com.lianglliu.hermoodbarometer.ui.screen.statistics.components

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
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.usecase.EmotionStatistics
import com.lianglliu.hermoodbarometer.ui.components.LoadingIndicator
import com.lianglliu.hermoodbarometer.ui.components.StatisticItem

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
                    StatisticItem(
                        label = stringResource(R.string.total_records),
                        value = statistics.totalRecords.toString()
                    )
                    StatisticItem(
                        label = stringResource(R.string.average_mood),
                        value = "%.1f".format(statistics.averageIntensity)
                    )
                    StatisticItem(
                        label = stringResource(R.string.most_frequent_emotion),
                        value = statistics.mostFrequentEmotion ?: stringResource(R.string.no_data)
                    )
                } else {
                    StatisticItem(
                        label = stringResource(R.string.total_records),
                        value = "0"
                    )
                    StatisticItem(
                        label = stringResource(R.string.average_mood),
                        value = stringResource(R.string.no_data)
                    )
                    StatisticItem(
                        label = stringResource(R.string.most_frequent_emotion),
                        value = stringResource(R.string.no_data)
                    )
                }
            }
        }
    }
} 