package com.fjr619.diary.presentation.components.diary

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import java.time.format.TextStyle

@Composable
fun ShowGalleryButton(
    galleryOpened: Boolean,
//    galleryLoading: Boolean,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = if (galleryOpened) "Hide Gallery" else "Show Gallery",
            style = MaterialTheme.typography.bodySmall
        )
    }
}