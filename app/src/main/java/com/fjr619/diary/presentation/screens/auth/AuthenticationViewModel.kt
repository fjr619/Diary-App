package com.fjr619.diary.presentation.screens.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.diary.util.Constants
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel: ViewModel() {

    init {
        Log.e("TAG", "init AuthenticationViewModel")
    }
    var loadingState = mutableStateOf(false)
        private set

    fun setLoading(loading: Boolean) {
        loadingState.value = loading
    }

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: (Boolean) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(Constants.APP_ID)
                        .login(
                            Credentials.jwt(tokenId)
                        ).loggedIn
                }
                withContext(Dispatchers.Main) {
                    onSuccess(result)
                }
            }catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }
}