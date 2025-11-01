package com.lianglliu.hermoodbarometer.util

import androidx.room.TypeConverter
import java.time.Instant

/**
 * Type converter for java.time.Instant
 */
internal class InstantConverter {

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? = instant?.toEpochMilli()

    @TypeConverter
    fun longToInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }
}