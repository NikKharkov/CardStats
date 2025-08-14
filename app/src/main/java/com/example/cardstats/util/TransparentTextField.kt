package com.example.cardstats.util

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransparentTextField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    placeholderText: String = "",
    readOnly: Boolean = false,
    trailingIconAvailable: Boolean = false,
    modifier: Modifier = Modifier,
    onIconClicked: () -> Unit = {},
    containerColor: Color = Color.Transparent,
    maxLines: Int = 1,
    iconColor: Color = Color.White,
    keyboardType: KeyboardType = KeyboardType.Text,
    @DrawableRes icon: Int = 0
) {
    val focusManager: FocusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        trailingIcon = {
            if (trailingIconAvailable) {
                IconButton(onClick = onIconClicked) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = iconColor
                    )
                }
            }
        },
        textStyle = TextStyle(
            fontSize = 20.sp
        ),
        placeholder = {
            Text(
                text = placeholderText,
                fontSize = 20.sp,
                color = Color.White
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions {
            focusManager.clearFocus()
        },
        shape = RoundedCornerShape(20.dp),
        readOnly = readOnly,
        modifier = modifier,
        maxLines = maxLines,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor
        )
    )
}