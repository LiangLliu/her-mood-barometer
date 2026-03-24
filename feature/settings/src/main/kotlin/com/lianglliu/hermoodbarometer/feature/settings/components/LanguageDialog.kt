package com.lianglliu.hermoodbarometer.feature.settings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.lianglliu.hermoodbarometer.core.designsystem.component.CsAlertDialog
import com.lianglliu.hermoodbarometer.core.designsystem.icon.AppIcons
import com.lianglliu.hermoodbarometer.core.designsystem.icon.outlined.Language
import com.lianglliu.hermoodbarometer.core.locales.R as localesR
import com.lianglliu.hermoodbarometer.core.model.data.Language

@Composable
fun Language.displayName(): String =
    when (this) {
        Language.SYSTEM -> stringResource(localesR.string.language_system)
        Language.ENGLISH -> stringResource(localesR.string.language_english)
        Language.JAPANESE -> stringResource(localesR.string.language_japanese)
        Language.KOREAN -> stringResource(localesR.string.language_korean)
        Language.CHINESE_SIMPLIFIED -> stringResource(localesR.string.language_chinese_simplified)
        Language.CHINESE_TRADITIONAL -> stringResource(localesR.string.language_chinese_traditional)
    }

@Composable
fun LanguageDialog(
    language: String,
    availableLanguages: List<Language>,
    onLanguageClick: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var languageState by rememberSaveable { mutableStateOf(language) }

    CsAlertDialog(
        titleRes = localesR.string.language,
        confirmButtonTextRes = localesR.string.ok,
        dismissButtonTextRes = localesR.string.cancel,
        icon = AppIcons.Outlined.Language,
        onConfirm = {
            onLanguageClick(languageState)
            onDismiss()
        },
        onDismiss = onDismiss,
        modifier = modifier,
    ) {
        LazyColumn(Modifier.selectableGroup()) {
            items(availableLanguages) { language ->
                val selected = language.code == languageState
                Box(Modifier.clip(RoundedCornerShape(18.dp))) {
                    Row(
                        modifier =
                            Modifier.fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = selected,
                                    onClick = { languageState = language.code },
                                    role = Role.RadioButton,
                                )
                                .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = selected, onClick = null)
                        Text(
                            text = language.displayName(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp),
                        )
                    }
                }
            }
        }
    }
}
