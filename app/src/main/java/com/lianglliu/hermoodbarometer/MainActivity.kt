package com.lianglliu.hermoodbarometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lianglliu.hermoodbarometer.ui.MoodApp
import com.lianglliu.hermoodbarometer.ui.theme.HerMoodBarometerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 主Activity
 * 应用的入口点
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HerMoodBarometerTheme {
                MoodApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoodAppPreview() {
    HerMoodBarometerTheme {
        MoodApp()
    }
}