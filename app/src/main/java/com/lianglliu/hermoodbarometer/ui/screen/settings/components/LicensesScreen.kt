package com.lianglliu.hermoodbarometer.ui.screen.settings.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lianglliu.hermoodbarometer.R
import com.lianglliu.hermoodbarometer.ui.components.SimpleScreenContainer
import com.mikepenz.aboutlibraries.ui.compose.android.rememberLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LicensesScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SimpleScreenContainer(
        title = stringResource(R.string.licenses),
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
    {
        val libraries by rememberLibraries(R.raw.aboutlibraries)
        LibrariesContainer(
            libraries = libraries,
            modifier = Modifier.fillMaxSize(),
            showDescription = true,
        )
    }
}
