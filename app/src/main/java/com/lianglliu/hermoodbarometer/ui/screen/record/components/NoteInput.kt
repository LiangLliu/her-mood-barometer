package com.lianglliu.hermoodbarometer.ui.screen.record.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.R

/**
 * 备注输入组件
 */
@Composable
fun NoteInput(
    noteText: String,
    onNoteChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.add_note),
                style = MaterialTheme.typography.titleMedium
            )
            
            OutlinedTextField(
                value = noteText,
                onValueChange = onNoteChanged,
                placeholder = { Text(stringResource(R.string.note_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
        }
    }
} 