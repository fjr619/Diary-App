package com.fjr619.diary.presentation.screens.auth

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.fjr619.diary.util.Constants
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Authenticationscreen(
    authenticated: Boolean,
    loadingState: Boolean,
    onTapState: OneTapSignInState,
    messageBarState: MessageBarState,
    onButtonClicked: () -> Unit,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismiss: (String) -> Unit,
    navigateToHome: () -> Unit
) {
    Scaffold {
        ContentWithMessageBar(
            messageBarState = messageBarState,
            errorMaxLines = 5
        ) {
            AuthenticationContent(
                loadingState = loadingState,
                onButtonClicked = onButtonClicked
            )
        }
    }

    OneTapSignInWithGoogle(
        state = onTapState,
        clientId = Constants.CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            onTokenIdReceived(tokenId)
        },
        onDialogDismissed = { message ->
            onDialogDismiss(message)
        })

    LaunchedEffect(key1 = authenticated) {
        if (authenticated) {
            navigateToHome()
        }
    }
}