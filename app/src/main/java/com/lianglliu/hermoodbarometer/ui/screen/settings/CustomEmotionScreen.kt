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
import com.lianglliu.hermoodbarometer.domain.model.CustomEmotion
import com.lianglliu.hermoodbarometer.ui.screen.settings.components.EmojiSelector

/**
 * 自定义情绪管理页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomEmotionScreen(
    onNavigateBack: () -> Unit,
    viewModel: CustomEmotionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedEmotion by remember { mutableStateOf<CustomEmotion?>(null) }
    
    // 处理错误消息
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            viewModel.clearErrorMessage()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.custom_emotions)) },
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
                    contentDescription = stringResource(R.string.add_custom_emotion)
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
            if (uiState.customEmotions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_custom_emotions),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(uiState.customEmotions) { emotion ->
                    CustomEmotionCard(
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
    
    // 添加自定义情绪对话框
    if (showAddDialog) {
        AddEditEmotionDialog(
            emotion = null,
            onConfirm = { name, emoji, description ->
                viewModel.addCustomEmotion(name, description, emoji)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
    
    // 编辑自定义情绪对话框
    if (showEditDialog && selectedEmotion != null) {
        AddEditEmotionDialog(
            emotion = selectedEmotion,
            onConfirm = { name, emoji, description ->
                selectedEmotion?.let { emotion ->
                    viewModel.updateCustomEmotion(emotion.copy(name = name, emoji = emoji, description = description))
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
                            viewModel.deleteCustomEmotion(emotion)
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
private fun CustomEmotionCard(
    emotion: CustomEmotion,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = emotion.emoji,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
                
                Column {
                    Text(
                        text = emotion.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (emotion.description.isNotBlank()) {
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
                        contentDescription = stringResource(R.string.edit)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditEmotionDialog(
    emotion: CustomEmotion?,
    onConfirm: (String, String, String) -> Unit, // name, emoji, description
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(emotion?.name ?: "") }
    var description by remember { mutableStateOf(emotion?.description ?: "") }
    var selectedEmoji by remember { mutableStateOf(emotion?.emoji ?: "😊") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (emotion == null) {
                    stringResource(R.string.add_custom_emotion)
                } else {
                    stringResource(R.string.edit_emotion)
                }
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 情绪名称输入框
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.emotion_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // 描述输入框
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
                
                // 当前选中的emoji显示
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.selected_emoji),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = selectedEmoji,
                        fontSize = 24.sp
                    )
                }
                
                // Emoji选择器
                Text(
                    text = stringResource(R.string.choose_emoji),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // 限制高度以避免对话框过大
                ) {
                    EmojiSelector(
                        selectedEmoji = selectedEmoji,
                        onEmojiSelected = { selectedEmoji = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name, selectedEmoji, description)
                    }
                },
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