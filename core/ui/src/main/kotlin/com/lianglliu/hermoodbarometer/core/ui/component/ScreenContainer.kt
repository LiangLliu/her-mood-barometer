package com.lianglliu.hermoodbarometer.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.core.designsystem.theme.HerMoodBarometerTheme
import com.lianglliu.hermoodbarometer.core.locales.R

/**
 * Unified screen container with a fixed top bar and scrollable content area.
 *
 * Performance: fixed title avoids recomposition overhead; LazyColumn virtualizes content; content
 * area is an independent recomposition scope.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: LazyListScope.() -> Unit,
) {
    val pageTitleDescription = stringResource(R.string.cd_page_title, title)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier =
                                Modifier.semantics { contentDescription = pageTitleDescription },
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = ExtendedTheme.colors.textMuted,
                            )
                        }
                    }
                },
                navigationIcon = navigationIcon ?: {},
                actions = actions ?: {},
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
            )
        },
        floatingActionButton = floatingActionButton ?: {},
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(contentPadding),
            content = content,
        )
    }
}

/** Screen container with a back navigation button. */
@Composable
fun ScreenContainerWithBack(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: LazyListScope.() -> Unit,
) {
    val navigateBackDescription = stringResource(R.string.cd_navigate_back)
    ScreenContainer(
        title = title,
        subtitle = subtitle,
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.semantics { contentDescription = navigateBackDescription },
            ) {
                Icon(
                    imageVector = AppIcons.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                )
            }
        },
        actions = actions,
        floatingActionButton = floatingActionButton,
        contentPadding = contentPadding,
        content = content,
    )
}

/** Simple screen container without scrolling, for pages that don't need a LazyColumn. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleScreenContainer(
    title: String,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    actions: @Composable (RowScope.() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val pageTitleDescription = stringResource(R.string.cd_page_title, title)
    val navigateBackDescription = stringResource(R.string.cd_navigate_back)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.semantics { contentDescription = pageTitleDescription },
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier =
                            Modifier.semantics { contentDescription = navigateBackDescription },
                    ) {
                        Icon(
                            imageVector = AppIcons.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                actions = actions ?: {},
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
            )
        },
        floatingActionButton = floatingActionButton ?: {},
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) { content() }
    }
}

/**
 * Full-screen container with its own Scaffold (hides the bottom navigation bar). Use for detail or
 * edit pages that need full-screen layout.
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
    content: LazyListScope.() -> Unit,
) {
    val pageTitleDescription = stringResource(R.string.cd_page_title, title)
    val navigateBackDescription = stringResource(R.string.cd_navigate_back)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.semantics { contentDescription = pageTitleDescription },
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier =
                            Modifier.semantics { contentDescription = navigateBackDescription },
                    ) {
                        Icon(
                            imageVector = AppIcons.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                actions = actions ?: {},
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
            )
        },
        floatingActionButton = floatingActionButton ?: {},
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(contentPadding),
            content = content,
        )
    }
}

// ==================== Previews ====================

@Preview(showBackground = true)
@Composable
private fun ScreenContainerPreview() {
    HerMoodBarometerTheme {
        ScreenContainer(title = "示例页面") {
            item {
                Text(
                    text = "这是一个示例内容项目 1",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            item {
                Text(
                    text = "这是一个示例内容项目 2",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            item {
                Text(
                    text = "这是一个示例内容项目 3",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FullScreenContainerPreview() {
    HerMoodBarometerTheme {
        FullScreenLazyColumnContainer(title = "全屏页面", onNavigateBack = { /* 预览中无操作 */ }) {
            item {
                Text(
                    text = "这是一个全屏页面的内容",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
            }
            item {
                Text(
                    text = "左上角有返回按钮，没有底部导航栏",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        }
    }
}
