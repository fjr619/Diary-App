package com.fjr619.diary.navigation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fjr619.diary.model.Diary
import com.fjr619.diary.model.Mood
import com.fjr619.diary.presentation.components.DisplayAlertDialog
import com.fjr619.diary.presentation.screens.auth.AuthenticationViewModel
import com.fjr619.diary.presentation.screens.auth.Authenticationscreen
import com.fjr619.diary.presentation.screens.home.HomeScreen
import com.fjr619.diary.presentation.screens.home.HomeViewModel
import com.fjr619.diary.presentation.screens.write.WriteScreen
import com.fjr619.diary.presentation.screens.write.WriteViewModel
import com.fjr619.diary.util.Constants
import com.fjr619.diary.util.RequestState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            },
            onDataLoaded = onDataLoaded
        )
        homeRoute(
            navigateToWrite = { navController.navigate(Screen.Write.route) },
            navigateToWriteWithArgs = { navController.navigate(Screen.Write.passDiaryId(it)) },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            },
            onDataLoaded = onDataLoaded
        )
        writeRoute(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = hiltViewModel()
        val loadingState by viewModel.loadingState
        val authenticated by viewModel.authenticated

        val onTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }

        Authenticationscreen(
            authenticated = authenticated,
            loadingState = loadingState,
            onTapState = onTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                onTapState.open()
                viewModel.setLoading(true)
            },
            onTokenIdReceived = {
                viewModel.signInWithMongoAtlas(it,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated!")
                        viewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(Exception(it))
                        viewModel.setLoading(false)
                    })
            },
            onDialogDismiss = {
                messageBarState.addError(Exception(it))
                viewModel.setLoading(false)
            },
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs:(String) -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        val diaries = viewModel.diaries
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by rememberSaveable {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = diaries) {
            if (diaries.value !is RequestState.Loading) {
                onDataLoaded()
            }
        }

        HomeScreen(
            diaries = diaries.value,
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            onDateSelected = {},
            onSignoutClicked = {
                signOutDialogOpened = true
            },
            navigateToWrite = navigateToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs,
            onShowHideGallery = viewModel::updateOpenGallery
        )

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to sign Out from your Google Account?",
            dialogOpened = signOutDialogOpened,
            onDialogClosed = { signOutDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    App.create(Constants.APP_ID).currentUser?.let {
                        it.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRoute(
//    selectedDiary: Diary?,
//    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit
) {
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
        val viewModel: WriteViewModel = viewModel()
        val uiState = viewModel.uiState

        LaunchedEffect(key1 = uiState) {
            Log.i("TAG", "selected ${uiState.selectedDiaryId}")
        }

        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = {
                Mood.values().size
            }
        )

        WriteScreen(
            selectedDiary = null,
            pagerState = pagerState,
            onDeleteConfirmed = { },
            onBackPressed = onBackPressed
        )
    }
}