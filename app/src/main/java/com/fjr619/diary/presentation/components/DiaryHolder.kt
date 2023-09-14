package com.fjr619.diary.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DateHeader(localDate: LocalDate) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = String.format("%02d", localDate.dayOfMonth),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Light
                )
            )
            Text(
                text = localDate.dayOfWeek.toString().take(3),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Light
                )
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = localDate.month.toString().lowercase().replaceFirstChar { it.titlecase() },
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Light
                )
            )
            Text(
                text = localDate.year.toString(),
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.38f
                ),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Light
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DateHeaderPreview() {
    DateHeader(localDate = LocalDate.now())
}