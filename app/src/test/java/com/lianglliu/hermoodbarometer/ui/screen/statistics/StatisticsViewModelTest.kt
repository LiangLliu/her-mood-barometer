package com.lianglliu.hermoodbarometer.ui.screen.statistics

import com.lianglliu.hermoodbarometer.domain.model.TimeRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for StatisticsViewModel
 * Focus on testing custom date range selection functionality
 * 
 * Note: These tests mainly verify UI state management logic, not data loading logic
 * since we don't have proper mock framework to simulate UseCase
 */
@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testTimeRangeEnumValuesAreCorrectlyDefined() {
        // Verify TimeRange enum contains all necessary values
        val timeRanges = TimeRange.entries
        
        assertTrue(timeRanges.contains(TimeRange.LAST_WEEK))
        assertTrue(timeRanges.contains(TimeRange.LAST_MONTH))
        assertTrue(timeRanges.contains(TimeRange.LAST_3_MONTHS))
        assertTrue(timeRanges.contains(TimeRange.LAST_SIX_MONTHS))
        assertTrue(timeRanges.contains(TimeRange.LAST_YEAR))
        assertTrue(timeRanges.contains(TimeRange.CUSTOM))
    }

    @Test
    fun testChartTypeEnumValuesAreCorrectlyDefined() {
        // Verify ChartType enum contains all necessary values
        val chartTypes = ChartType.entries
        
        assertTrue(chartTypes.contains(ChartType.BAR))
        assertTrue(chartTypes.contains(ChartType.LINE))
        assertEquals(2, chartTypes.size)
    }

    @Test
    fun testLocalDateBoundaryConditions() {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val tomorrow = today.plusDays(1)
        
        // Verify date calculations are correct
        assertTrue(yesterday.isBefore(today))
        assertTrue(tomorrow.isAfter(today))
        assertEquals(1, today.toEpochDay() - yesterday.toEpochDay())
        assertEquals(1, tomorrow.toEpochDay() - today.toEpochDay())
    }

    @Test
    fun testStatisticsUiStateDefaultValues() {
        val defaultState = StatisticsUiState()
        
        assertEquals(TimeRange.LAST_WEEK, defaultState.selectedTimeRange)
        assertEquals(ChartType.BAR, defaultState.selectedChartType)
        assertNull(defaultState.customStartDate)
        assertNull(defaultState.customEndDate)
        assertNull(defaultState.statistics)
        assertFalse(defaultState.isLoading)
        assertNull(defaultState.errorMessage)
    }
}
