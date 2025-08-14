package com.example.cardstats.util

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cardstats.ui.theme.MainYellow

@Composable
fun CustomIconButton(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    isSelected: Boolean = false,
    color: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            alpha.animateTo(
                targetValue = 0.5f,
                animationSpec = tween(durationMillis = 300)
            )
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300)
            )
            isPressed = false
        }
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .background(
                color = if (isSelected) MainYellow else Color.Transparent,
                shape = CircleShape
            )
            .alpha(alpha.value)
            .clickable(
                interactionSource = null,
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(40.dp)
        )
    }
}