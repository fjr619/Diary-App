package com.fjr619.diary.data.repository

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.fjr619.diary.model.Diary
import com.fjr619.diary.util.Constants.APP_ID
import com.fjr619.diary.util.RequestState
import com.fjr619.diary.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId

object MongoRepositoryImpl : MongoRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser

    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query<Diary>(query = "ownerId == $0", user.id),
                        name = "User's Diaries"
                    )
                }.log(LogLevel.ALL).build()
            realm = Realm.open(config)
        }
    }

    override fun getAllDiaries(): Flow<Diaries> {
        return if (user != null) {
            try {
                realm.query<Diary>(query = "ownerId == $0", user.id)
                    .sort(property = "date", Sort.DESCENDING)
                    .asFlow()
                    .flowOn(Dispatchers.IO)
                    .map { result ->
                        val temp = result.list
                            .groupBy {
                                it.date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }

                        val newResult = mutableMapOf<LocalDate, SnapshotStateList<Diary>>()
                        temp.entries.forEach { map ->
                            newResult[map.key] = SnapshotStateList<Diary>().also {
                                it.addAll(map.value)
                            }
                        }

                        RequestState.Success(
                            data = newResult
                        )
                    }

            } catch (e: Exception) {
                flow {
                    emit(RequestState.Error(e))
                }
            }
        } else {
            flow {
                emit(RequestState.Error(UserNotAuthenticatedException()))
            }
        }
    }
}

private class UserNotAuthenticatedException : Exception("User is not logged in")