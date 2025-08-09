package com.lianglliu.hermoodbarometer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lianglliu.hermoodbarometer.data.preferences.PreferencesManager
import com.lianglliu.hermoodbarometer.ui.LocaleManager
import com.lianglliu.hermoodbarometer.ui.MoodApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * 现代化主Activity
 * 集成了Edge-to-Edge设计和性能优化
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    @Inject
    lateinit var preferencesManager: PreferencesManager
    
    private var isPreloadComplete: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // 安装系统 SplashScreen，尽早展示启动画面
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { !isPreloadComplete }
        super.onCreate(savedInstanceState)
        
        // 启用Edge-to-Edge设计
        enableEdgeToEdge()
        
        // 在首帧渲染前同步设置夜间模式和语言，避免闪烁与错误语言
        applyInitialThemeBlocking()
        applyInitialLocaleBlocking()

        // 预加载完成，允许离开Splash
        isPreloadComplete = true

        // 预热应用组件
        warmupApplication()
        
        setContent {
            // 由应用内的单一来源状态驱动主题（见 MoodApp 内部）
            MoodApp()
        }
    }

    /**
     * 在设置Compose内容之前，阻塞式获取一次主题偏好并应用到AppCompatDelegate，
     * 确保首帧就处在正确的深/浅色模式，避免由默认“跟随系统”导致的可见切换。
     */
    private fun applyInitialThemeBlocking() {
        try {
            runBlocking {
                val theme = preferencesManager.theme.first()
                val mode = when (theme) {
                    "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                    "light" -> AppCompatDelegate.MODE_NIGHT_NO
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        } catch (e: Exception) {
            // 安全兜底，异常时遵循系统
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    /**
     * 在首帧前同步应用已保存语言，确保冷启动即为用户所选语言。
     */
    private fun applyInitialLocaleBlocking() {
        try {
            runBlocking {
                val language = preferencesManager.language.first()
                LocaleManager.setAppLanguage(this@MainActivity, language)
            }
        } catch (_: Exception) {
            // 忽略异常，保持系统语言
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
}

@Preview(showBackground = true)
@Composable
fun MoodAppPreview() {
    MoodApp()
}