package com.lianglliu.hermoodbarometer

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lianglliu.hermoodbarometer.data.preferences.PreferencesManager
import com.lianglliu.hermoodbarometer.ui.LocaleManager
import com.lianglliu.hermoodbarometer.ui.MoodApp
import com.lianglliu.hermoodbarometer.ui.theme.HerMoodBarometerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.content.edit

/**
 * 主Activity
 * 应用的入口点
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var preferencesManager: PreferencesManager
    
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
     * 从DataStore获取语言设置
     */
    private fun getStoredLanguageCode(context: Context): String {
        // 使用SharedPreferences作为备用方案，确保语言设置能正确读取
        val sharedPrefs = context.getSharedPreferences("mood_preferences", Context.MODE_PRIVATE)
        return sharedPrefs.getString("language", "zh") ?: "zh"
    }
    
    /**
     * 重新创建Activity以应用新的语言设置
     */
    fun recreateWithLanguage(languageCode: String) {
        // 保存新的语言设置到DataStore
        saveLanguageCode(languageCode)
        // 重新创建Activity
        recreate()
    }
    
    /**
     * 保存语言代码到DataStore和SharedPreferences
     */
    private fun saveLanguageCode(languageCode: String) {
        // 同时保存到SharedPreferences（用于Activity启动时读取）和DataStore（用于ViewModel）
        val sharedPrefs = getSharedPreferences("mood_preferences", Context.MODE_PRIVATE)
        sharedPrefs.edit { putString("language", languageCode) }
        
        // 使用协程保存语言设置到DataStore
        CoroutineScope(Dispatchers.IO).launch {
            preferencesManager.setLanguage(languageCode)
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