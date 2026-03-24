package com.lianglliu.hermoodbarometer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianglliu.hermoodbarometer.core.designsystem.theme.ExtendedTheme
import com.lianglliu.hermoodbarometer.navigation.TopLevelDestination

@Composable
fun CustomBottomNavBar(
    destinations: List<TopLevelDestination>,
    currentDestination: TopLevelDestination?,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    onFabClick: () -> Unit,
    showFab: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = ExtendedTheme.colors

    Box(modifier = modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.navigationBars)) {
        // The actual Bottom Navigation Bar background
        Box(
            modifier =
                Modifier.align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(84.dp)
                    .background(colors.cardBackground.copy(alpha = 0.85f)) // semi-transparent
                    .drawBehind {
                        drawLine(
                            color = colors.borderLight,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx(),
                        )
                    }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Determine midway for FAB space
                val midPoint = destinations.size / 2

                destinations.forEachIndexed { index, destination ->
                    if (index == midPoint) {
                        Spacer(modifier = Modifier.width(64.dp))
                    }

                    val selected = destination == currentDestination
                    BottomNavItem(
                        destination = destination,
                        selected = selected,
                        onClick = { onNavigateToDestination(destination) },
                    )
                }
            }
        }

        // Overlapping Floating Action Button
        if (showFab) {
            CustomFab(
                onClick = onFabClick,
                // Offset the FAB visually from the top center of the 72dp bar.
                // The outer box wraps tightly around the 72dp inner box because of
                // align(BottomCenter),
                // so TopCenter aligns to the 72dp top. We pull it up 16dp.
                modifier = Modifier.align(Alignment.TopCenter).offset(y = (-16).dp),
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    destination: TopLevelDestination,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = ExtendedTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val navItemDescription = stringResource(destination.iconTextId)

    Column(
        modifier =
            Modifier.semantics {
                    contentDescription = navItemDescription
                    role = Role.Tab
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        // Red indicator above active item
        Box(
            modifier =
                Modifier.width(20.dp)
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(if (selected) colors.accent else Color.Transparent)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = destination.emojiIcon,
            fontSize = 22.sp,
            lineHeight = 22.sp,
            modifier = Modifier.alpha(if (selected) 1f else 0.8f), // Optional active feedback
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = stringResource(destination.titleTextId),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.3.sp,
            color = if (selected) colors.accent else colors.textHint,
        )
    }
}
