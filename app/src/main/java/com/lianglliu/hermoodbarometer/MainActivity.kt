package com.lianglliu.hermoodbarometer

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.lianglliu.hermoodbarometer.data.preferences.PreferencesManager
import com.lianglliu.hermoodbarometer.ui.LocaleManager
import com.lianglliu.hermoodbarometer.ui.MoodApp
import com.lianglliu.hermoodbarometer.ui.theme.HerMoodBarometerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 现代化主Activity
 * 集成了Edge-to-Edge设计和性能优化
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
        
        // 启用Edge-to-Edge设计
        enableEdgeToEdge()
        
        // 预热应用组件
        warmupApplication()
        
        setContent {
            HerMoodBarometerTheme {
                MoodApp()
            }
        }
    }
    
    /**
     * 预热应用组件
     * 在后台预加载关键组件以提升性能
     */
    private fun warmupApplication() {
        lifecycleScope.launch {
            try {
                // 预热数据库连接
                // 这里可以执行一些轻量级的数据库操作
                
                // 预加载用户偏好设置
                preferencesManager.language
                
                // 预热Compose组件（如果需要）
                // ComponentActivity默认会预热Compose
                
            } catch (e: Exception) {
                // 记录启动错误但不阻止应用启动
                android.util.Log.e("MainActivity", "Warmup failed", e)
            }
        }
    }
    
    /**
     * 获取存储的语言代码
     * 从SharedPreferences快速读取，避免启动时的异步延迟
     */
    private fun getStoredLanguageCode(context: Context): String {
        val sharedPrefs = context.getSharedPreferences("mood_preferences", Context.MODE_PRIVATE)
        return sharedPrefs.getString("language", "zh") ?: "zh"
    }
    
    /**
     * 重新创建Activity以应用新的语言设置
     */
    fun recreateWithLanguage(languageCode: String) {
        saveLanguageCode(languageCode)
        recreate()
    }
    
    /**
     * 保存语言代码到DataStore和SharedPreferences
     */
    private fun saveLanguageCode(languageCode: String) {
        // 同步保存到SharedPreferences（用于下次启动）
        val sharedPrefs = getSharedPreferences("mood_preferences", Context.MODE_PRIVATE)
        sharedPrefs.edit { putString("language", languageCode) }
        
        // 异步保存到DataStore（用于ViewModel）
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