package com.fjr619.diary.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.fjr619.diary.DiaryApp
import com.fjr619.diary.model.Diary
import com.fjr619.diary.presentation.components.diary.DiaryHolder
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    diaries: Map<LocalDate, List<Diary>>,
    onclick: (String) -> Unit,
    onShowHideGallery:(LocalDate, ObjectId) -> Unit
) {
    if(diaries.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = paddingValues.calculateTopPadding())
                .navigationBarsPadding()
        ) {
            diaries.forEach { (locaDate, diaries) ->
                stickyHeader(
                    key = locaDate
                ) { 
                    DateHeader(localDate = locaDate)
                }

                items(
                    items = diaries,
                    key = { it._id.toString() }
                ) {
                    DiaryHolder(
                        diary = it,
                        onClick = onclick,
                        onShowHideGallery = onShowHideGallery
                    )
                }
            }
        }
    } else {
        EmptyPage()
    }
}