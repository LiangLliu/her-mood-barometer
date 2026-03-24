package com.lianglliu.hermoodbarometer.dao.model

/**
 * Data class for aggregated intensity counts from database queries Used internally by DAO for
 * intensity statistics queries
 */
data class IntensityCount(val intensity: Int, val count: Int)
