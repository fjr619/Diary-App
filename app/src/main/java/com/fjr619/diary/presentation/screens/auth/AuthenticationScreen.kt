package com.fjr619.diary.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.fjr619.diary.util.Constants
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import java.lang.Exception

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Authenticationscreen(
    loadingState: Boolean,
    onTapState: OneTapSignInState,
    messageBarState: MessageBarState,
    onButtonClicked: () -> Unit
) {
    Scaffold {
        ContentWithMessageBar(messageBarState = messageBarState) {
            AuthenticationContent(
                loadingState
            ){
                onButtonClicked.invoke()
            }
        }
    }

    OneTapSignInWithGoogle(
        state = onTapState,
        clientId = Constants.CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.e("TAG", "tokenId $tokenId")
            messageBarState.addSuccess("Successfully authentication")
        },
        onDialogDismissed = {message ->
            Log.e("TAG", "message $message")
            messageBarState.addError(Exception(message))
        })
}