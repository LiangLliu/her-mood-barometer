package com.lianglliu.hermoodbarometer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.lianglliu.hermoodbarometer.data.preferences.PreferencesManager
import com.lianglliu.hermoodbarometer.ui.MoodApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 现代化主Activity
 * 集成了Edge-to-Edge设计和性能优化
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    @Inject
    lateinit var preferencesManager: PreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 启用Edge-to-Edge设计
        enableEdgeToEdge()
        
        // 预热应用组件
        warmupApplication()
        
        setContent {
            // 由应用内的单一来源状态驱动主题（见 MoodApp 内部）
            MoodApp()
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