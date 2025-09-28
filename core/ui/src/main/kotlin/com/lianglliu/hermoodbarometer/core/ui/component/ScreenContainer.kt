package com.lianglliu.hermoodbarometer.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.ArrowBack
import com.lianglliu.hermoodbarometer.core.designsystem.theme.HerMoodBarometerTheme
import com.lianglliu.hermoodbarometer.core.locales.R

/**
 * 统一的屏幕容器组件
 * 提供固定标题栏和可滚动内容区域，确保性能最优和用户体验一致性
 *
 * 性能优化:
 * - 固定标题避免重组开销
 * - LazyColumn 内容虚拟化
 * - 内容区域独立重组域
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: LazyListScope.() -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.semantics {
                            contentDescription = "页面标题: $title"
                        }
                    )
                },
                navigationIcon = navigationIcon ?: { },
                actions = actions ?: { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = floatingActionButton ?: { },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(contentPadding),
            content = content
        )
    }
}

/**
 * 带返回按钮的屏幕容器
 * 用于需要返回导航的页面
 */
@Composable
fun ScreenContainerWithBack(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: LazyListScope.() -> Unit
) {
    ScreenContainer(
        title = title,
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.semantics {
                    contentDescription = "返回上一页"
                }
            ) {
                Icon(
                    imageVector = AppIcons.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = actions,
        floatingActionButton = floatingActionButton,
        contentPadding = contentPadding,
        content = content
    )
}

/**
 * 简单页面容器（无滚动）
 * 用于不需要滚动的简单页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleScreenContainer(
    title: String,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    actions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.semantics {
                            contentDescription = "页面标题: $title"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.semantics {
                            contentDescription = "返回上一页"
                        }
                    ) {
                        Icon(
                            imageVector = AppIcons.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = actions ?: { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = floatingActionButton ?: { },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            content()
        }
    }
}

/**
 * 全屏页面容器（独立 Scaffold，不显示底部导航栏）
 * 用于需要全屏显示的页面，如详情页、编辑页等
 *
 * 性能优化:
 * - 独立的 Scaffold，避免与主应用 Scaffold 嵌套
 * - 固定标题栏，避免滚动时的重组开销
 * - 内容区域独立重组域
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenLazyColumnContainer(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: LazyListScope.() -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.semantics {
                            contentDescription = "页面标题: $title"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.semantics {
                            contentDescription = "返回上一页"
                        }
                    ) {
                        Icon(
                            imageVector = AppIcons.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = actions ?: { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = floatingActionButton ?: { },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(contentPadding),
            content = content
        )
    }
}

// ==================== 预览函数 ====================

@Preview(showBackground = true)
@Composable
private fun ScreenContainerPreview() {
    HerMoodBarometerTheme {
        ScreenContainer(
            title = "示例页面"
        ) {
            item {
                Text(
                    text = "这是一个示例内容项目 1",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            item {
                Text(
                    text = "这是一个示例内容项目 2",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            item {
                Text(
                    text = "这是一个示例内容项目 3",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FullScreenContainerPreview() {
    HerMoodBarometerTheme {
        FullScreenLazyColumnContainer(
            title = "全屏页面",
            onNavigateBack = { /* 预览中无操作 */ }
        ) {
            item {
                Text(
                    text = "这是一个全屏页面的内容",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            item {
                Text(
                    text = "左上角有返回按钮，没有底部导航栏",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}