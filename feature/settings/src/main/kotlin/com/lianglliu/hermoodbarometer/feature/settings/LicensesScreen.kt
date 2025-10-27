package com.lianglliu.hermoodbarometer.feature.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lianglliu.hermoodbarometer.core.ui.component.SimpleScreenContainer
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.lianglliu.hermoodbarometer.core.locales.R as localesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LicensesScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SimpleScreenContainer(
        title = stringResource(localesR.string.licenses),
        onNavigateBack = onBackClick,
        modifier = modifier,
    )
    {
        val libraries by produceLibraries(R.raw.aboutlibraries)

        LibrariesContainer(
            libraries = libraries,
            modifier = Modifier.fillMaxSize(),
            showDescription = true,
        )
    }
}
