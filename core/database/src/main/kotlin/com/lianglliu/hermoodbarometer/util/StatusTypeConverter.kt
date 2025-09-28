package com.lianglliu.hermoodbarometer.util

import androidx.room.TypeConverter
import com.lianglliu.hermoodbarometer.core.model.data.StatusType

internal class StatusTypeConverter {

    @TypeConverter
    fun statusToString(value: StatusType?) = value?.name

    @TypeConverter
    fun stringToStatus(value: String?) = value?.let { enumValueOf<StatusType>(it) }
}