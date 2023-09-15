package com.fjr619.diary.presentation.components.diary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fjr619.diary.model.Diary
import com.fjr619.diary.model.Mood
import com.fjr619.diary.ui.theme.Elevation
import com.fjr619.diary.util.toInstant
import io.realm.kotlin.ext.realmListOf

@Composable
fun DiaryHolder(
    diary: Diary,
    onClick: (String) -> Unit
) {
    var localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    var galleryOpened by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            }
        ) {
            onClick(diary._id.toString())
        }
    ) {
        Spacer(modifier = Modifier.width(14.dp))

        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1
        ) {}

        Spacer(modifier = Modifier.width(20.dp))

        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) {
                        it.size.height.toDp()
                    }
                },
            tonalElevation = Elevation.Level1
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                DiaryHeader(moodName = diary.mood, time = diary.date.toInstant())
                Text(
                    modifier = Modifier.padding(14.dp),
                    text = diary.description,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                if (diary.images.isNotEmpty()) {
                    ShowGalleryButton(
                        galleryOpened = galleryOpened,
                        onClick = { galleryOpened = !galleryOpened })
                }

                AnimatedVisibility(visible = galleryOpened) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Gallery(images = diary.images)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun DiaryHolderPreview() {
    DiaryHolder(Diary()
        .apply {
            title = "My Diary"
            description =
                "adkpaowdkapodkaopdkapoddakdopadkadpokdpaodad afkawpeof a[pwoekf aw[poef ka[wopefk a[wopefk a[pwoef ka[wopef ka[pwoefk ap[woefk ap[woefk a[wpoef kawp[oefk aop[we"
            mood = Mood.Angry.name
            images = realmListOf("", "")
        }) {

    }
}