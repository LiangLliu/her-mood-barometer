package com.lianglliu.hermoodbarometer.feature.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.locales.R

fun LazyListScope.sentimentOverview() {
    item { SectionTitle(stringResource(R.string.emotion_overview)) }
    item {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SummaryRow(
                    label = stringResource(R.string.total_records),
                    valueContent = {
                        ValueTagThemed {
                            Text(
                                text = "42",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                SummaryRow(
                    label = stringResource(R.string.average_mood),
                    valueContent = {
                        ValueTagThemed {
                            Text(
                                text = "积极",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                SummaryRow(
                    label = stringResource(R.string.most_frequent_emotion),
                    valueContent = {
                        ValueTagThemed {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "😊",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text(
                                    text = "开心",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun SummaryRow(
    label: String,
    valueContent: @Composable () -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label, style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.weight(1f))
        valueContent()
    }
}

@Composable
fun ValueTagThemed(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

