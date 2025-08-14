package com.example.cardstats.loading.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.Alignment

data class ScreenContent(
    val text: String,
    val alignment: Alignment,
    val ofSetDp: Int,
    @DrawableRes val linesImage: Int,
    @DrawableRes val mainImage: Int
)
