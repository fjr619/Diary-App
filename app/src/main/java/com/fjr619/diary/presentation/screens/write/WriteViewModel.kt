package com.fjr619.diary.presentation.screens.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.diary.data.repository.MongoRepositoryImpl
import com.fjr619.diary.model.Diary
import com.fjr619.diary.model.Mood
import com.fjr619.diary.util.Constants
import com.fjr619.diary.util.RequestState
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

data class UIState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral
)

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var uiState by mutableStateOf(UIState())
        private set

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }

    fun getDiaryIdArgument() {
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(key = Constants.WRITE_SCREEN_ARGUMENT_KEY)
        )
    }

    private fun fetchSelectedDiary() {
        uiState.selectedDiaryId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                MongoRepositoryImpl.getSelectedDiary(ObjectId.invoke(it))
                    .collect { diary ->
                        withContext(Dispatchers.Main) {
                            if (diary is RequestState.Success) {
                                setSelectedDiary(diary.data)
                                setTitle(diary.data.title)
                                setDesc(diary.data.description)
                                setMood(Mood.valueOf(diary.data.mood))
                            }
                        }
                    }

            }
        }
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun setDesc(desc: String) {
        uiState = uiState.copy(description = desc)
    }

    fun setMood(mood: Mood) {
        uiState = uiState.copy(mood = mood)
    }

    fun setSelectedDiary(diary: Diary) {
        uiState = uiState.copy(
            selectedDiary = diary
        )
    }
}

