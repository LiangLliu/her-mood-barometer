package com.lianglliu.hermoodbarometer.core.model.data

import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/** Time range enum for statistics page filtering */
enum class TimeRange {
    LAST_WEEK,
    LAST_MONTH,
    LAST_3_MONTHS,
    LAST_SIX_MONTHS,
    LAST_YEAR;

    /** Get the start datetime for this time range */
    fun getStartDateTime(): LocalDateTime {
        val now = LocalDateTime.now()
        return when (this) {
            LAST_WEEK -> now.minusWeeks(1)
            LAST_MONTH -> now.minusMonths(1)
            LAST_3_MONTHS -> now.minusMonths(3)
            LAST_SIX_MONTHS -> now.minusMonths(6)
            LAST_YEAR -> now.minusYears(1)
        }
    }

    /** Get the end datetime for this time range */
    fun getEndDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

    /**
     * Convert this time range to a pair of Instant bounds (start, end).
     *
     * Uses fixed day counts (30 days for a month, 365 for a year) for database queries where
     * approximate ranges are acceptable. For calendar-aware calculations, use [getStartDateTime]
     * instead.
     */
    fun toInstantBounds(): Pair<Instant, Instant> {
        val now = Instant.now()
        val startTime =
            when (this) {
                LAST_WEEK -> now.minus(7, ChronoUnit.DAYS)
                LAST_MONTH -> now.minus(30, ChronoUnit.DAYS)
                LAST_3_MONTHS -> now.minus(90, ChronoUnit.DAYS)
                LAST_SIX_MONTHS -> now.minus(180, ChronoUnit.DAYS)
                LAST_YEAR -> now.minus(365, ChronoUnit.DAYS)
            }
        return startTime to now
    }
}
