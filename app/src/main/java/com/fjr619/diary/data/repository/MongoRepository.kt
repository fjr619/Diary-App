package com.fjr619.diary.data.repository

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.fjr619.diary.model.Diary
import com.fjr619.diary.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate,SnapshotStateList<Diary>>>

interface MongoRepository {
    fun configureTheRealm()
    fun getAllDiaries(): Flow<Diaries>
    fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<Diary>>
}