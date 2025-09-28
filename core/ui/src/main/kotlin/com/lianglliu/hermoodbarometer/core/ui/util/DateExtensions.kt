package com.lianglliu.hermoodbarometer.core.ui.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.temporal.WeekFields
import kotlin.time.Clock
import kotlin.time.Instant

fun Instant.getZonedDateTime() = toLocalDateTime(TimeZone.currentSystemDefault())

fun Instant.getZonedYear() = this.getZonedDateTime().year

fun Instant.getZonedMonth() = getZonedDateTime().month.number

fun Instant.getZonedWeek() = this
    .getZonedDateTime()
    .toJavaLocalDateTime()
    .get(WeekFields.ISO.weekOfWeekBasedYear())

fun getCurrentZonedDateTime() =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun Instant.isInCurrentMonthAndYear(): Boolean {
    val localDateTime = this.getZonedDateTime()
    val currentDate = getCurrentZonedDateTime()
    return localDateTime.year == currentDate.year && localDateTime.month == currentDate.month
}

fun getCurrentYear(): Int = getCurrentZonedDateTime().year

fun getCurrentWeek(): Int = getCurrentZonedDateTime()
    .toJavaLocalDateTime()
    .get(WeekFields.ISO.weekOfWeekBasedYear())