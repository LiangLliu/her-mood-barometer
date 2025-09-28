package com.lianglliu.hermoodbarometer.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.lianglliu.hermoodbarometer.core.designsystem.component.CsFloatingActionButton
import com.lianglliu.hermoodbarometer.navigation.MoodNavHost
import com.lianglliu.hermoodbarometer.navigation.TopLevelDestination.RECORD
import com.lianglliu.hermoodbarometer.navigation.TopLevelDestination.SETTINGS
import com.lianglliu.hermoodbarometer.navigation.TopLevelDestination.STATISTICS
import com.lianglliu.hermoodbarometer.util.InAppUpdateResult
import kotlin.reflect.KClass
import com.lianglliu.hermoodbarometer.core.locales.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MoodApp(
    appState: AppState,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val currentDestination = appState.currentDestination
    val currentTopLevelDestination = appState.currentTopLevelDestination

    val inAppUpdateResult = appState.inAppUpdateResult.collectAsStateWithLifecycle().value

    val activity = LocalActivity.current

    val updateAvailableMessage = stringResource(R.string.app_update_available)
    val updateDownloadedMessage = stringResource(R.string.app_update_downloaded)
    val updateText = stringResource(R.string.update)
    val installText = stringResource(R.string.install)

    LaunchedEffect(inAppUpdateResult) {
        when (inAppUpdateResult) {
            is InAppUpdateResult.Available -> {
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = updateAvailableMessage,
                    actionLabel = updateText,
                    duration = Indefinite,
                    withDismissAction = true,
                ) == ActionPerformed
                if (snackbarResult) activity?.let { inAppUpdateResult.startFlexibleUpdate(it, 120) }
            }

            is InAppUpdateResult.Downloaded -> {
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = updateDownloadedMessage,
                    actionLabel = installText,
                    duration = Indefinite,
                ) == ActionPerformed
                if (snackbarResult) inAppUpdateResult.completeUpdate()
            }

            else -> {}
        }
    }

    var previousDestination by remember { mutableStateOf(RECORD) }

    val navigationSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(windowAdaptiveInfo)

    NavigationSuiteScaffold(
        primaryActionContent = {
            if (currentTopLevelDestination != null) {
                // 安全获取 fabTitle 和 fabIcon
                val fabTitleRes = currentTopLevelDestination.fabTitle
                    ?: previousDestination.fabTitle // 使用 elvis 操作符替代 !!
                val fabIconVector = currentTopLevelDestination.fabIcon
                    ?: previousDestination.fabIcon // 使用 elvis 操作符替代 !!

                // 只有当 fabTitleRes 和 fabIconVector 都不为 null 时才显示 FAB
                // 并且 currentTopLevelDestination 不是 SETTINGS
                if (fabTitleRes != null && fabIconVector != null && currentTopLevelDestination != SETTINGS) {
                    CsFloatingActionButton(
                        contentDescriptionRes = fabTitleRes,
                        icon = fabIconVector,
                        onClick = {
                            when (currentTopLevelDestination) {
                                RECORD -> { /* TODO: 定义 RECORD 的 FAB 点击行为 */ }
                                STATISTICS -> { /* TODO: 定义 STATISTICS 的 FAB 点击行为 */ }
                                // SETTINGS 的 FAB 因为上面的 visible 条件不会显示，但以防万一可以留空或移除
                                SETTINGS -> {}
                            }
                        },
                        modifier = Modifier
                            .animateFloatingActionButton(
                                // visible 条件现在移到了外层 if 判断中一部分
                                visible = true, // 因为外层已经判断了 currentTopLevelDestination != SETTINGS
                                alignment = Alignment.BottomEnd,
                            ),
                    )
                }
            }
        },
        navigationItems = {
            appState.topLevelDestinations.forEach { destination ->
                val selected = currentDestination.isRouteInHierarchy(destination.baseRoute)
                NavigationSuiteItem(
                    selected = selected,
                    icon = {
                        val navItemIcon = if (selected) {
                            destination.selectedIcon
                        } else {
                            destination.unselectedIcon
                        }
                        Icon(
                            imageVector = navItemIcon,
                            contentDescription = null,
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destination.iconTextId),
                            maxLines = 1,
                        )
                    },
                    onClick = {
                        if (currentTopLevelDestination != null && currentTopLevelDestination != SETTINGS) {
                            previousDestination = currentTopLevelDestination
                        }
                        appState.navigateToTopLevelDestination(destination)
                    },
                )
            }
        },
        navigationSuiteType = navigationSuiteType,
        navigationItemVerticalArrangement = Arrangement.Center,
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .then(
                            if (navigationSuiteType != NavigationSuiteType.ShortNavigationBarCompact &&
                                currentTopLevelDestination != SETTINGS
                            ) {
                                Modifier.padding(bottom = 100.dp)
                            } else {
                                Modifier
                            },
                        ),
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
        ) { innerPadding ->
            MoodNavHost(
                appState = appState,
                onShowSnackbar = { message, action ->
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = action,
                        duration = Short,
                    ) == ActionPerformed
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            )
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean =
    this?.hierarchy?.any { it.hasRoute(route) } == true