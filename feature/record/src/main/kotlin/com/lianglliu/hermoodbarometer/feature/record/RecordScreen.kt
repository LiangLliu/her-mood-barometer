package com.lianglliu.hermoodbarometer.feature.record

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
import com.lianglliu.hermoodbarometer.feature.record.components.DateTimeRow
import com.lianglliu.hermoodbarometer.feature.record.components.EmotionIntensitySelector
import com.lianglliu.hermoodbarometer.feature.record.components.EmotionSelector
import com.lianglliu.hermoodbarometer.feature.record.components.NoteInput
import com.lianglliu.hermoodbarometer.feature.record.components.SaveButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(viewModel: RecordViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val isSaveEnabled by remember {
        derivedStateOf { uiState.selectedEmotion != null && !uiState.isLoading }
    }

    val successMessage = stringResource(R.string.record_saved_successfully)
    LaunchedEffect(uiState.showSuccessMessage) {
        if (uiState.showSuccessMessage) {
            keyboardController?.hide()
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = successMessage,
                    duration = SnackbarDuration.Short,
                )
            }
            viewModel.clearSuccessMessage()
        }
    }

    val errorMessages = uiState.errorMessageResId?.let { stringResource(it) }
    LaunchedEffect(errorMessages) {
        errorMessages?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Long)
            }
        }
    }

    ScreenContainer(
        title = stringResource(R.string.record_title),
        subtitle = stringResource(R.string.record_subtitle),
        modifier = Modifier.imePadding(),
    ) {
        // DateTime pills
        item { DateTimeRow(modifier = Modifier.padding(bottom = 8.dp)) }

        // Emotion selector
        item {
            EmotionSelector(
                selectedEmotion = uiState.selectedEmotion,
                userEmotions = uiState.userEmotions,
                onEmotionSelected = { viewModel.updateSelectedEmotion(it) },
            )
        }

        // Intensity selector
        item {
            EmotionIntensitySelector(
                intensityLevel = uiState.intensityLevel,
                onIntensityChanged = { viewModel.updateIntensity(it) },
            )
        }

        // Note input
        item {
            NoteInput(noteText = uiState.noteText, onNoteChanged = { viewModel.updateNote(it) })
        }

        // Error card
        val errorResId = uiState.errorMessageResId
        if (errorResId != null) {
            item {
                ErrorCard(
                    message = stringResource(errorResId),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                )
            }
        }

        // Save button
        item {
            SaveButton(
                isEnabled = isSaveEnabled,
                isLoading = uiState.isLoading,
                onClick = {
                    keyboardController?.hide()
                    viewModel.saveEmotionRecord()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp),
            )
        }
    }
}
