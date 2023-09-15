package com.fjr619.diary.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.diary.data.repository.Diaries
import com.fjr619.diary.data.repository.MongoRepositoryImpl
import com.fjr619.diary.util.RequestState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        Log.e("TAG", "init home view model")
        observeAllDiaries()
    }

    private fun observeAllDiaries() {
        viewModelScope.launch {
            MongoRepositoryImpl.getAllDiaries().collectLatest {
                diaries.value = it
            }
        }
    }
}