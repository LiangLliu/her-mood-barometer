package com.lianglliu.hermoodbarometer.ui.statistics

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lianglliu.hermoodbarometer.MainActivity
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.ui.screen.statistics.components.EmotionBarChartCard
import com.lianglliu.hermoodbarometer.ui.screen.statistics.components.EmotionLineChartCard
import com.lianglliu.hermoodbarometer.ui.screen.statistics.components.StatisticsCard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatisticsUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun emptyStateForCharts_isDisplayed() {
        composeRule.setContent {
            EmotionLineChartCard(statistics = null)
            EmotionBarChartCard(statistics = null)
        }

        composeRule.onNodeWithContentDescription(
            composeRule.activity.getString(R.string.cd_chart_daily_avg)
        ).assertIsDisplayed()

        composeRule.onNodeWithContentDescription(
            composeRule.activity.getString(R.string.cd_chart_emotion_counts)
        ).assertIsDisplayed()
    }

    @Test
    fun statisticsMetrics_labelsVisible() {
        composeRule.setContent {
            StatisticsCard(statistics = null, isLoading = false)
        }
        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.total_records)
        ).assertIsDisplayed()
        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.average_mood)
        ).assertIsDisplayed()
        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.most_frequent_emotion)
        ).assertIsDisplayed()
    }
}


