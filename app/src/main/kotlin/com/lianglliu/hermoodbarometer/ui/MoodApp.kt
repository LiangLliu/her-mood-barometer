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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.lianglliu.hermoodbarometer.MainActivityViewModel
import com.lianglliu.hermoodbarometer.QuickRecordSaveState
import com.lianglliu.hermoodbarometer.core.designsystem.component.CsFloatingActionButton
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.navigation.MoodNavHost
import com.lianglliu.hermoodbarometer.navigation.TopLevelDestination.DIARY
import com.lianglliu.hermoodbarometer.navigation.TopLevelDestination.SETTINGS
import com.lianglliu.hermoodbarometer.util.InAppUpdateResult
import kotlin.reflect.KClass

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MoodApp(
    appState: AppState,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    viewModel: MainActivityViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val currentDestination = appState.currentDestination
    val currentTopLevelDestination = appState.currentTopLevelDestination

    val showQuickRecordDialog by viewModel.showQuickRecordDialog.collectAsStateWithLifecycle()
    val quickRecordSaveState by viewModel.quickRecordSaveState.collectAsStateWithLifecycle()

    val inAppUpdateResult = appState.inAppUpdateResult.collectAsStateWithLifecycle().value

    val activity = LocalActivity.current

    val updateAvailableMessage = stringResource(R.string.app_update_available)
    val updateDownloadedMessage = stringResource(R.string.app_update_downloaded)
    val updateText = stringResource(R.string.update)
    val installText = stringResource(R.string.install)
    val recordSavedMessage = stringResource(R.string.record_saved_successfully)

    LaunchedEffect(inAppUpdateResult) {
        when (inAppUpdateResult) {
            is InAppUpdateResult.Available -> {
                val snackbarResult =
                    snackbarHostState.showSnackbar(
                        message = updateAvailableMessage,
                        actionLabel = updateText,
                        duration = Indefinite,
                        withDismissAction = true,
                    ) == ActionPerformed
                if (snackbarResult) activity?.let { inAppUpdateResult.startFlexibleUpdate(it, 120) }
            }

            is InAppUpdateResult.Downloaded -> {
                val snackbarResult =
                    snackbarHostState.showSnackbar(
                        message = updateDownloadedMessage,
                        actionLabel = installText,
                        duration = Indefinite,
                    ) == ActionPerformed
                if (snackbarResult) inAppUpdateResult.completeUpdate()
            }

            else -> {}
        }
    }

    // Handle quick record save state
    LaunchedEffect(quickRecordSaveState) {
        val saveState = quickRecordSaveState
        when (saveState) {
            is QuickRecordSaveState.Success -> {
                snackbarHostState.showSnackbar(message = recordSavedMessage, duration = Short)
            }
            is QuickRecordSaveState.Error -> {
                snackbarHostState.showSnackbar(message = saveState.message, duration = Short)
            }
            else -> {}
        }
    }

    var previousDestination by remember { mutableStateOf(DIARY) }

    val navigationSuiteType =
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(windowAdaptiveInfo)

    NavigationSuiteScaffold(
        primaryActionContent = {
            if (currentTopLevelDestination != null) {
                // 安全获取 fabTitle 和 fabIcon
                val fabTitleRes =
                    currentTopLevelDestination.fabTitle
                        ?: previousDestination.fabTitle // 使用 elvis 操作符替代 !!
                val fabIconVector =
                    currentTopLevelDestination.fabIcon
                        ?: previousDestination.fabIcon // 使用 elvis 操作符替代 !!

                // 只有当 fabTitleRes 和 fabIconVector 都不为 null 时才显示 FAB
                // 并且 currentTopLevelDestination 不是 SETTINGS
                if (
                    fabTitleRes != null &&
                        fabIconVector != null &&
                        currentTopLevelDestination != SETTINGS
                ) {
                    CsFloatingActionButton(
                        contentDescriptionRes = fabTitleRes,
                        icon = fabIconVector,
                        onClick = { viewModel.showQuickRecordDialog() },
                        modifier =
                            Modifier.animateFloatingActionButton(
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
                        val navItemIcon =
                            remember(selected) {
                                if (selected) {
                                    destination.selectedIcon
                                } else {
                                    destination.unselectedIcon
                                }
                            }
                        Icon(imageVector = navItemIcon, contentDescription = null)
                    },
                    label = {
                        val labelText = stringResource(destination.iconTextId)
                        Text(text = labelText, maxLines = 1)
                    },
                    onClick =
                        remember(currentTopLevelDestination, destination) {
                            {
                                if (
                                    currentTopLevelDestination != null &&
                                        currentTopLevelDestination != SETTINGS
                                ) {
                                    previousDestination = currentTopLevelDestination
                                }
                                appState.navigateToTopLevelDestination(destination)
                            }
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
                    modifier =
                        Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                            .then(
                                if (
                                    navigationSuiteType !=
                                        NavigationSuiteType.ShortNavigationBarCompact &&
                                        currentTopLevelDestination != SETTINGS
                                ) {
                                    Modifier.padding(bottom = 100.dp)
                                } else {
                                    Modifier
                                }
                            ),
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier.semantics { testTagsAsResourceId = true },
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
                modifier =
                    Modifier.fillMaxSize()
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                        ),
            )
        }
    }

    // Quick Record Dialog
    if (showQuickRecordDialog) {
        // Pre-resolve emotion names
        val emotionHappy = stringResource(R.string.emotion_happy)
        val emotionCalm = stringResource(R.string.emotion_calm)
        val emotionTouched = stringResource(R.string.emotion_touched)
        val emotionAnxious = stringResource(R.string.emotion_anxious)
        val emotionWronged = stringResource(R.string.emotion_wronged)
        val emotionTired = stringResource(R.string.emotion_tired)

        QuickRecordDialog(
            onDismiss = { viewModel.hideQuickRecordDialog() },
            onConfirm = { emotion, weather, activities, note, dateTime ->
                // Map emotion enum to name
                val emotionName =
                    when (emotion) {
                        Emotion.HAPPY -> emotionHappy
                        Emotion.CALM -> emotionCalm
                        Emotion.TOUCHED -> emotionTouched
                        Emotion.ANXIOUS -> emotionAnxious
                        Emotion.WRONGED -> emotionWronged
                        Emotion.TIRED -> emotionTired
                    }

                // Map emotion enum to ID and save
                viewModel.saveQuickRecord(
                    emotionId = emotion.predefinedId,
                    emotionName = emotionName,
                    emotionEmoji = emotion.emoji,
                    weather = weather?.name,
                    activities = activities.map { it.name },
                    note = note,
                    timestamp = dateTime,
                )
            },
        )
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean =
    this?.hierarchy?.any { it.hasRoute(route) } == true
