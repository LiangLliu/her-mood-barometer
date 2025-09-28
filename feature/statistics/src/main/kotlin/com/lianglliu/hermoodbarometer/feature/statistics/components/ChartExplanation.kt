package com.lianglliu.hermoodbarometer.feature.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Info
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.MoreVert
import com.lianglliu.hermoodbarometer.core.locales.R

/**
 * 图表解释组件
 * 为每个图表提供说明和洞察
 */
@Composable
fun ChartExplanation(
    title: String,
    content: String,
    insight: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 标题
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = AppIcons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // 内容
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            // 洞察
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = AppIcons.Outlined.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = insight,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(start = 8.dp),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

/**
 * 情绪分布图表解释
 */
@Composable
fun EmotionDistributionExplanation(modifier: Modifier = Modifier) {
    ChartExplanation(
        title = stringResource(R.string.chart_explanation_distribution_title),
        content = stringResource(R.string.chart_explanation_distribution_content),
        insight = stringResource(R.string.chart_insight_distribution),
        modifier = modifier
    )
}

/**
 * 情绪趋势图表解释
 */
@Composable
fun EmotionTrendExplanation(modifier: Modifier = Modifier) {
    ChartExplanation(
        title = stringResource(R.string.chart_explanation_trend_title),
        content = stringResource(R.string.chart_explanation_trend_content),
        insight = stringResource(R.string.chart_insight_trend),
        modifier = modifier
    )
}

/**
 * 情绪模式图表解释
 */
@Composable
fun EmotionPatternsExplanation(modifier: Modifier = Modifier) {
    ChartExplanation(
        title = stringResource(R.string.chart_explanation_patterns_title),
        content = stringResource(R.string.chart_explanation_patterns_content),
        insight = stringResource(R.string.chart_insight_patterns),
        modifier = modifier
    )
}

/**
 * 情绪对比图表解释
 */
@Composable
fun EmotionComparisonExplanation(modifier: Modifier = Modifier) {
    ChartExplanation(
        title = stringResource(R.string.chart_explanation_comparison_title),
        content = stringResource(R.string.chart_explanation_comparison_content),
        insight = stringResource(R.string.chart_insight_comparison),
        modifier = modifier
    )
}