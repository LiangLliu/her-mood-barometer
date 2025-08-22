package com.lianglliu.hermoodbarometer.ui.screen.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.usecase.EmotionStatistics
import com.lianglliu.hermoodbarometer.ui.components.EmptyState
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries

@Composable
fun EmotionBarChartCard(
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
                EmotionBarChart(statistics = statistics, modifier = Modifier.fillMaxSize())
            } else {
                EmptyState(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Info,
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
internal fun EmotionBarChart(
    statistics: EmotionStatistics,
    modifier: Modifier = Modifier
) {
    val emotions = remember(statistics.countsByEmotion) {
        statistics.countsByEmotion.entries.sortedByDescending { it.value }.map { it.key }
    }
    val values = remember(statistics.countsByEmotion) {
        emotions.map {
            (statistics.countsByEmotion[it] ?: 0).toFloat()
        }
    }

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(values) {
        modelProducer.runTransaction {
            columnSeries { series(*values.toTypedArray()) }
        }
    }

    val cd = stringResource(R.string.cd_chart_emotion_counts)
    Column(
        modifier = modifier
            .semantics { contentDescription = cd },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.chart_emotion_counts),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        val bottom = HorizontalAxis.rememberBottom(
            valueFormatter = { _, x, _ ->
                val idx = x.toInt().coerceIn(0, (emotions.size - 1).coerceAtLeast(0))
                emotions.getOrNull(idx) ?: ""
            }
        )
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = bottom,
            ),
            modelProducer = modelProducer,
        )
    }
}


