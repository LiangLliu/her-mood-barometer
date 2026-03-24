package com.lianglliu.hermoodbarometer.feature.record.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.core.locales.R

@Composable
fun NoteInput(noteText: String, onNoteChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    val colors = ExtendedTheme.colors

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, colors.borderLight, RoundedCornerShape(20.dp))
                .background(colors.cardBackground)
                .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = stringResource(R.string.note_optional),
            style = MaterialTheme.typography.labelSmall,
            color = colors.textMuted,
            letterSpacing = 1.sp,
        )

        OutlinedTextField(
            value = noteText,
            onValueChange = onNoteChanged,
            placeholder = {
                Text(
                    stringResource(R.string.note_placeholder),
                    color = colors.textHint,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(14.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.accent,
                    unfocusedBorderColor = colors.borderLight,
                    cursorColor = colors.accent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
            textStyle = MaterialTheme.typography.bodyMedium,
        )
    }
}
