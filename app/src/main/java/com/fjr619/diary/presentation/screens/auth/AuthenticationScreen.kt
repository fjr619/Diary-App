package com.fjr619.diary.presentation.screens.auth

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Authenticationscreen(
    loadingState: Boolean,
    onButtonClicked: () -> Unit
) {
    Scaffold {
        AuthenticationContent(
            loadingState
        ){
            onButtonClicked
        }
    }
}