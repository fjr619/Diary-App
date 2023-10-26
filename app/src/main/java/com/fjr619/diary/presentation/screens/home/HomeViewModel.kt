package com.fjr619.diary.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.diary.data.repository.Diaries
import com.fjr619.diary.data.repository.MongoRepository
import com.fjr619.diary.data.repository.MongoRepositoryImpl
import com.fjr619.diary.model.Diary
import com.fjr619.diary.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mongoRepository: MongoRepository
): ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        Log.e("TAG", "init home view model")
        observeAllDiaries()
    }

    @OptIn(FlowPreview::class)
    private fun observeAllDiaries() {
        diaries.value = RequestState.Loading
        viewModelScope.launch {
            mongoRepository.getAllDiaries().debounce(2000).collectLatest { it ->
                diaries.value = it
            }
        }
    }

    fun updateOpenGallery(localDate: LocalDate, id: ObjectId) {
        when (diaries.value) {
            is RequestState.Success -> {
                val data =
                    (diaries.value as RequestState.Success<Map<LocalDate, SnapshotStateList<Diary>>>).data
                val listDiary = data[localDate]

                listDiary?.let { listDiary ->
                    val diary = listDiary.find {
                        it._id == id
                    }

                    diary?.let { diary ->
                        val indexDiary = listDiary.indexOf(diary)

                        val newDiary = Diary().apply {
                            _id = diary._id
                            ownerId = diary.ownerId
                            mood = diary.mood
                            description = diary.description
                            images = diary.images
                            date = diary.date
                            galleryOpened = !diary.galleryOpened
                        }

                        listDiary.set(indexDiary, newDiary)
                    }

                }
            }

            else -> {}
        }
    }
}

fun <T> Collection<T>.toMutableStateList() = SnapshotStateList<T>().also { it.addAll(this) }
