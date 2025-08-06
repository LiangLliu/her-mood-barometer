package com.lianglliu.hermoodbarometer.ui.screen.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.lianglliu.hermoodbarometer.ui.screen.record.components.EmotionTypeSelector
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
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        PageTitle(title = stringResource(R.string.record_title))
        
        EmotionTypeSelector(
            selectedEmotion = uiState.selectedEmotion,
            onEmotionSelected = { viewModel.updateSelectedEmotion(it) }
        )
        
        EmotionIntensitySelector(
            intensityLevel = uiState.intensityLevel,
            onIntensityChanged = { viewModel.updateIntensity(it) }
        )
        
        NoteInput(
            noteText = uiState.noteText,
            onNoteChanged = { viewModel.updateNote(it) }
        )
        
        SaveButton(
            isEnabled = uiState.selectedEmotion != null,
            isLoading = uiState.isLoading,
            onClick = { viewModel.saveEmotionRecord() },
            modifier = Modifier.fillMaxWidth()
        )
        
        // 错误消息显示
        uiState.errorMessage?.let { errorMessage ->
            ErrorCard(message = errorMessage)
        }
        
        // 成功消息显示
        if (uiState.showSuccessMessage) {
            SuccessCard(message = stringResource(R.string.record_saved_successfully))
        }
    }
}

