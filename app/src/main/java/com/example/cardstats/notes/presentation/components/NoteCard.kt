package com.example.cardstats.notes.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.R
import com.example.cardstats.notes.presentation.NoteViewModel
import com.example.cardstats.ui.theme.BrightRed
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteCard(
    noteId: Int = -1,
    noteName: String = "Name of the note",
    date: String = "17.03.2025",
    mainText: String = "Bluffing too much in early positions is risky...",
    gameName: String = "Poker",
    isIconFilled: Boolean = false,
    modifier: Modifier = Modifier,
    onNoteClicked: (Int) -> Unit = {},
    noteViewModel: NoteViewModel = koinViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Click"
    )

    var localIconFilled by remember { mutableStateOf(isIconFilled) }

    LaunchedEffect(noteId) {
        val note = noteViewModel.getNoteById(noteId)
        if (note != null) {
            localIconFilled = note.isIconFilled
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BrightRed, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onNoteClicked(noteId) },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(R.drawable.note_icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 8.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = noteName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = date,
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = if (mainText.length > 25) "${mainText.take(25)}..." else mainText,
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = gameName,
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(Color(0xFFFDAE02), RoundedCornerShape(24.dp))
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .align(Alignment.Start)
                )
            }

            Icon(
                painter = painterResource(
                    if (localIconFilled) R.drawable.genderless_icon else R.drawable.genderless_icon_filled
                ),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(48.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        localIconFilled = !localIconFilled
                        noteViewModel.updateIconFilled(noteId, localIconFilled)
                    }
                    .padding(start = 8.dp)
            )
        }
    }
}