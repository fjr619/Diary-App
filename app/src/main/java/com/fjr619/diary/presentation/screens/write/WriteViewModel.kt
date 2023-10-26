package com.fjr619.diary.presentation.screens.write

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.diary.data.repository.MongoRepository
import com.fjr619.diary.model.Diary
import com.fjr619.diary.model.Mood
import com.fjr619.diary.util.Constants
import com.fjr619.diary.util.RequestState
import com.fjr619.diary.util.toRealmInstant
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime
import javax.inject.Inject

data class UIState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
    val updateedDateTime: RealmInstant? = null
)

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val mongoRepository: MongoRepository,
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
                mongoRepository.getSelectedDiary(ObjectId.invoke(it))
                    .catch {
                        emit(RequestState.Error(Exception("Diary is already deleted")))
                    }
                    .collect { diary ->
                        withContext(Dispatchers.Main) {
                            if (diary is RequestState.Success) {
                                Log.e("TAG", "fetch sukses ${diary.data._id.toHexString()}")
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

    fun upsertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("TAG", "id ${uiState.selectedDiaryId} ${diary._id.toHexString()}")
            if (uiState.selectedDiaryId != null) {
                updateDiary(diary = diary, onSuccess = onSuccess, onError = onError)
            } else {
                insertDiary(diary = diary, onSuccess = onSuccess, onError = onError)
            }
        }
    }

    private suspend fun insertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val result = mongoRepository.insertDiary(diary = diary.apply {
            if (uiState.updateedDateTime != null) {
                date = uiState.updateedDateTime!!
            }
        })
        if (result is RequestState.Success) {
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            withContext(Dispatchers.Main) {
                onError(result.error.message.toString())
            }
        }

    }

    private suspend fun updateDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val result = mongoRepository.updateDiary(diary = diary.apply {
            this._id = ObjectId.invoke(uiState.selectedDiaryId!!)
            this.date = if (uiState.updateedDateTime != null) {
                uiState.updateedDateTime!!
            } else {
                uiState.selectedDiary!!.date
            }
        })
        if (result is RequestState.Success) {
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            Log.e("TAG", "error ${result.error.message.toString()}")
            withContext(Dispatchers.Main) {
                onError(result.error.message.toString())
            }
        }
    }

    fun deleteDiary(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.selectedDiaryId?.let {
                val result = mongoRepository.deleteDiary(ObjectId.invoke(it))
                if (result is RequestState.Success) {
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } else if (result is RequestState.Error) {
                    withContext(Dispatchers.Main) {
                        onError(result.error.message.toString())

                    }
                }
            } ?: kotlin.run {

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

    fun updateDateTime(zonedDateTime: ZonedDateTime) {
        uiState = uiState.copy(
            updateedDateTime = zonedDateTime.toInstant().toRealmInstant()
        )
    }
}

