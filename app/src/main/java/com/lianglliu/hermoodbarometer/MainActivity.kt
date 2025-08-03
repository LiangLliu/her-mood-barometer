package com.lianglliu.hermoodbarometer

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lianglliu.hermoodbarometer.ui.LocaleManager
import com.lianglliu.hermoodbarometer.ui.MoodApp
import com.lianglliu.hermoodbarometer.ui.theme.HerMoodBarometerTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

/**
 * 主Activity
 * 应用的入口点
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun attachBaseContext(newBase: Context) {
        // 在Activity创建时应用语言设置
        val languageCode = getStoredLanguageCode(newBase)
        val context = LocaleManager.applyLocale(newBase, languageCode)
        super.attachBaseContext(context)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HerMoodBarometerTheme {
                MoodApp()
            }
        }
    }
    
    /**
     * 获取存储的语言代码
     * 这里暂时使用SharedPreferences，实际应该从DataStore获取
     */
    private fun getStoredLanguageCode(context: Context): String {
        val sharedPrefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return sharedPrefs.getString("language", "zh") ?: "zh"
    }
    
    /**
     * 重新创建Activity以应用新的语言设置
     */
    fun recreateWithLanguage(languageCode: String) {
        // 保存新的语言设置
        saveLanguageCode(languageCode)
        // 重新创建Activity
        recreate()
    }
    
    /**
     * 保存语言代码
     * 这里暂时使用SharedPreferences，实际应该使用DataStore
     */
    private fun saveLanguageCode(languageCode: String) {
        val sharedPrefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("language", languageCode).apply()
    }
}

@Preview(showBackground = true)
@Composable
fun MoodAppPreview() {
    HerMoodBarometerTheme {
        MoodApp()
    }
}