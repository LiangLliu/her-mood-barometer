package com.lianglliu.hermoodbarometer.util

import android.app.Activity

interface InAppReviewManager {

    suspend fun openReviewDialog(activity: Activity)
}