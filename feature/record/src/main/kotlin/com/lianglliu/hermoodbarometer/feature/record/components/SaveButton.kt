package com.lianglliu.hermoodbarometer.feature.record.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.core.locales.R

@Composable
fun SaveButton(
    isEnabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentColor = ExtendedTheme.colors.accent

    Button(
        onClick = onClick,
        modifier =
            modifier
                .fillMaxWidth()
                .height(48.dp)
                .shadow(
                    elevation = if (isEnabled) 12.dp else 0.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = accentColor.copy(alpha = 0.3f),
                    spotColor = accentColor.copy(alpha = 0.3f),
                ),
        enabled = isEnabled && !isLoading,
        shape = RoundedCornerShape(14.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = Color.White,
                disabledContainerColor = accentColor.copy(alpha = 0.5f),
                disabledContentColor = Color.White.copy(alpha = 0.7f),
            ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = stringResource(R.string.save_record),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.3.sp,
            )
        }
    }
}
