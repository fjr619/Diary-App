package com.fjr619.diary.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fjr619.diary.presentation.screens.auth.Authenticationscreen
import com.fjr619.diary.util.Constants
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = startDestination ) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute() {
    composable(route = Screen.Authentication.route) {
        val onTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        Authenticationscreen(
            loadingState = onTapState.opened,
            onTapState = onTapState,
            messageBarState = messageBarState
        ) {
            Log.e("TAG", "clicked")
            onTapState.open()
        }
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screen.Write.route,
        arguments = listOf(
            navArgument(name = Constants.WRITE_SCREEN_ARGUMENT_KEY) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {

    }
}