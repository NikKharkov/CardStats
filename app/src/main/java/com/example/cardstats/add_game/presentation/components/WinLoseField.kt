package com.example.cardstats.add_game.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cardstats.util.TransparentTextField

@Composable
fun WinLoseField(
    isWin: Boolean,
    onToggle: () -> Unit,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    TransparentTextField(
        placeholderText = if (isWin) "Win" else "Lose",
        readOnly = true,
        containerColor = if (isWin) Color(0xFF4A883D) else Color(0xFFED5B5B),
        icon = icon,
        trailingIconAvailable = true,
        onIconClicked = onToggle,
        modifier = modifier
    )
}