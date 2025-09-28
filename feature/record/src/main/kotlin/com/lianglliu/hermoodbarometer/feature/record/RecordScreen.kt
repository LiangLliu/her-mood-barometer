package com.lianglliu.hermoodbarometer.feature.record

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.core.locales.R
import com.lianglliu.hermoodbarometer.core.ui.component.ErrorCard
import com.lianglliu.hermoodbarometer.core.ui.component.ScreenContainer
import com.lianglliu.hermoodbarometer.feature.record.components.EmotionIntensitySelector
import com.lianglliu.hermoodbarometer.feature.record.components.EmotionSelector
import com.lianglliu.hermoodbarometer.feature.record.components.NoteInput
import com.lianglliu.hermoodbarometer.feature.record.components.SaveButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RecordScreen(
    viewModel: RecordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val isSaveEnabled by remember {
        derivedStateOf {
            uiState.selectedEmotion != null && !uiState.isLoading // Also disable when loading
        }
    }

    // Handle success messages with Snackbar
    LaunchedEffect(uiState.showSuccessMessage) {
        if (uiState.showSuccessMessage) {
            keyboardController?.hide() // Hide keyboard on success
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "记录已成功保存！", // Or from stringResource
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.clearSuccessMessage() // Clear after initiating snackbar show
        }
    }

    // Handle error messages with Snackbar
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { errorMessage ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Long // Errors might need longer visibility
                )
            }
        }
    }

    ScreenContainer(
        title = stringResource(R.string.record_title),
        modifier = Modifier.imePadding() // Handles keyboard
    ) {
        item {
            EmotionSelector(
                selectedEmotion = uiState.selectedEmotion,
                userEmotions = uiState.userEmotions,
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

        if (uiState.errorMessage != null && isValidationMessage(uiState.errorMessage!!)) {
            item {
                ErrorCard(
                    message = uiState.errorMessage!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }

        item {
            SaveButton(
                isEnabled = isSaveEnabled,
                isLoading = uiState.isLoading,
                onClick = {
                    keyboardController?.hide()
                    viewModel.saveEmotionRecord()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            )
        }
    }
}

private fun isValidationMessage(errorMessage: String): Boolean {
    return errorMessage == RecordViewModel.VALIDATION_MSG_SELECT_EMOTION ||
            errorMessage.startsWith("情绪强度必须在") ||
            errorMessage.startsWith("备注不能超过")
}

