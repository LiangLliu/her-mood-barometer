package com.lianglliu.hermoodbarometer.ui.screen.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.ui.components.ErrorCard
import com.lianglliu.hermoodbarometer.ui.components.PageTitle
import com.lianglliu.hermoodbarometer.ui.components.SuccessCard
import com.lianglliu.hermoodbarometer.ui.screen.record.components.EmotionIntensitySelector
import com.lianglliu.hermoodbarometer.ui.screen.record.components.EmotionSelector
import com.lianglliu.hermoodbarometer.ui.screen.record.components.NoteInput
import com.lianglliu.hermoodbarometer.ui.screen.record.components.SaveButton

/**
 * 情绪记录页面
 * 用户记录当前情绪的主页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    viewModel: RecordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 优化保存按钮使能状态计算，避免不必要的重组
    val isSaveEnabled by remember {
        derivedStateOf {
            uiState.selectedEmotion != null
        }
    }

    // 处理成功消息
    LaunchedEffect(uiState.showSuccessMessage) {
        if (uiState.showSuccessMessage) {
            viewModel.clearSuccessMessage()
        }
    }

    // 处理错误消息
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            // 可以在这里显示Snackbar或其他错误提示
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            PageTitle(title = stringResource(R.string.record_title))
        }

        item {
            EmotionSelector(
                selectedEmotion = uiState.selectedEmotion,
                customEmotions = uiState.customEmotions,
                onEmotionSelected = { viewModel.updateSelectedEmotion(it) }
            )
        }

        item {
            EmotionIntensitySelector(
                intensityLevel = uiState.intensityLevel,
                onIntensityChanged = { viewModel.updateIntensity(it) }
            )
        }

        item {
            NoteInput(
                noteText = uiState.noteText,
                onNoteChanged = { viewModel.updateNote(it) }
            )
        }

        item {
            SaveButton(
                isEnabled = isSaveEnabled,
                isLoading = uiState.isLoading,
                onClick = { viewModel.saveEmotionRecord() },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 错误消息显示
        uiState.errorMessage?.let { errorMessage ->
            item {
                ErrorCard(message = errorMessage)
            }
        }

        // 成功消息显示
        if (uiState.showSuccessMessage) {
            item {
                SuccessCard(message = stringResource(R.string.record_saved_successfully))
            }
        }
    }
}

