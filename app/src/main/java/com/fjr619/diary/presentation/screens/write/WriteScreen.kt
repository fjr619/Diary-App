package com.fjr619.diary.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.fjr619.diary.model.Diary
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    uiState: UIState,
    pagerState: PagerState,
    onTitleChanged: (String)-> Unit,
    onDescriptionChanged: (String)-> Unit,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit,
    moodName: () -> String,
    onSaveClicked: (Diary) -> Unit,
    onDateTimeUpdate: (ZonedDateTime) -> Unit,

    ) {


    
    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = uiState.selectedDiary,
                onDeleteConfirmed = onDeleteConfirmed,
                onBackPressed = onBackPressed,
                moodName = moodName,
                onDateTimeUpdate =  onDateTimeUpdate
            )
        }
    ) {
        WriteContent(
            paddingValues = it,
            uiState = uiState,
//            title = uiState.title,
//            description = uiState.description,
            pagerState = pagerState,
            onTitleChanged = onTitleChanged,
            onDescriptionChanged = onDescriptionChanged,
            onSaveClicked = onSaveClicked
        )
    }
}