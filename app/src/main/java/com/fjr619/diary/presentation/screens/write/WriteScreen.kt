package com.fjr619.diary.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.fjr619.diary.model.Diary
import com.fjr619.diary.model.Mood

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    selectedDiary: Diary?,
    uiState: UIState,
    pagerState: PagerState,
    onTitleChanged: (String)-> Unit,
    onDescriptionChanged: (String)-> Unit,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit
) {

    //Update the mood when selecting an existing diary
//    LaunchedEffect(key1 = uiState.mood) {
//        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
//    }

    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = selectedDiary,
                onDeleteConfirmed = onDeleteConfirmed,
                onBackPressed = onBackPressed
            )
        }
    ) {
        WriteContent(
            paddingValues = it,
            title = uiState.title,
            description = uiState.description,
            pagerState = pagerState,
            onTitleChanged = onTitleChanged,
            onDescriptionChanged = onDescriptionChanged
        )
    }
}