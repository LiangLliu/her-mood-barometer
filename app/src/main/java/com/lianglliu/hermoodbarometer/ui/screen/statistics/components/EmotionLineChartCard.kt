package com.lianglliu.hermoodbarometer.ui.screen.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.usecase.DailyEmotionPoint
import com.lianglliu.hermoodbarometer.domain.usecase.EmotionStatistics
import com.lianglliu.hermoodbarometer.ui.components.EmptyState

@Composable
fun EmotionLineChartCard(
    statistics: EmotionStatistics?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (statistics != null && statistics.totalRecords > 0) {
                EmotionLineChart(statistics = statistics, modifier = Modifier.fillMaxSize())
            } else {
                EmptyState(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    },
                    title = stringResource(R.string.no_data),
                    subtitle = stringResource(R.string.chart_empty_hint)
                )
            }
        }
    }
}

@Composable
internal fun EmotionLineChart(
    statistics: EmotionStatistics,
    modifier: Modifier = Modifier
) {
    val points: List<DailyEmotionPoint> = statistics.dailyEmotionTrend
    val labels = remember(points) { points.map { it.date.toString().substring(5) } }
    val emotionLabels = remember(points) { points.map { it.emotionEmoji } }

    val cd = stringResource(R.string.cd_chart_emotion_counts)
    Column(
        modifier = modifier
            .semantics { contentDescription = cd },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.chart_emotion_trend),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        // 显示情绪趋势时间线
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            points.forEachIndexed { index, point ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 日期
                    Text(
                        text = labels[index],
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // 情绪表情符号
                    Text(
                        text = point.emotionEmoji,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // 情绪名称
                    Text(
                        text = point.emotionName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // 分隔线（除了最后一项）
                if (index < points.size - 1) {
                    androidx.compose.material3.HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EmotionLineChartCardEmptyPreview() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        EmotionLineChartCard(statistics = null)
    }
}
