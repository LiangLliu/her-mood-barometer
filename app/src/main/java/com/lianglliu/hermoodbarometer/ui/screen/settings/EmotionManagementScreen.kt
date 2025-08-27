package com.lianglliu.hermoodbarometer.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.domain.model.Emotion
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.EmojiSelector

/**
 * 情绪管理页面
 * 管理用户创建的情绪
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionManagementScreen(
    onNavigateBack: () -> Unit,
    viewModel: EmotionManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedEmotion by remember { mutableStateOf<Emotion?>(null) }
    
    // 处理错误消息
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            viewModel.clearErrorMessage()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.manage_emotions)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_emotion)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiState.userEmotions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_user_emotions),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(uiState.userEmotions) { emotion ->
                    EmotionCard(
                        emotion = emotion,
                        onEdit = {
                            selectedEmotion = emotion
                            showEditDialog = true
                        },
                        onDelete = {
                            selectedEmotion = emotion
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }
    
    // 添加情绪对话框
    if (showAddDialog) {
        AddEditEmotionDialog(
            emotion = null,
            onConfirm = { name, emoji, description ->
                viewModel.addEmotion(name, emoji, description)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
    
    // 编辑情绪对话框
    if (showEditDialog && selectedEmotion != null) {
        AddEditEmotionDialog(
            emotion = selectedEmotion,
            onConfirm = { name, emoji, description ->
                selectedEmotion?.let { emotion ->
                    viewModel.updateEmotion(emotion.copy(name = name, emoji = emoji, description = description))
                }
                showEditDialog = false
                selectedEmotion = null
            },
            onDismiss = {
                showEditDialog = false
                selectedEmotion = null
            }
        )
    }
    
    // 删除确认对话框
    if (showDeleteDialog && selectedEmotion != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_emotion)) },
            text = { Text(stringResource(R.string.confirm_delete_emotion)) },
            confirmButton = {
                Button(
                    onClick = {
                        selectedEmotion?.let { emotion ->
                            viewModel.deleteEmotion(emotion)
                        }
                        showDeleteDialog = false
                        selectedEmotion = null
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun EmotionCard(
    emotion: Emotion,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = emotion.emoji,
                    fontSize = 32.sp
                )
                Column {
                    Text(
                        text = emotion.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (emotion.description.isNotEmpty()) {
                        Text(
                            text = emotion.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_emotion)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_emotion)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddEditEmotionDialog(
    emotion: Emotion?,
    onConfirm: (name: String, emoji: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(emotion?.name ?: "") }
    var emoji by remember { mutableStateOf(emotion?.emoji ?: "😊") }
    var description by remember { mutableStateOf(emotion?.description ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (emotion == null) stringResource(R.string.add_emotion) else stringResource(R.string.edit_emotion)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.emotion_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                EmojiSelector(
                    selectedEmoji = emoji,
                    onEmojiSelected = { emoji = it }
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.emotion_description)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, emoji, description) },
                enabled = name.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
