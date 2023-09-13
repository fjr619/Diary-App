package com.fjr619.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.fjr619.diary.navigation.Screen
import com.fjr619.diary.navigation.SetupNavGraph
import com.fjr619.diary.ui.theme.DiaryTheme
import com.fjr619.diary.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var mongoApp: App


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DiaryTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = getStartDestination(),
                    navController = navController,
                    mongoApp = mongoApp
                )
            }
        }
    }

    private fun getStartDestination(): String {
        val user = mongoApp.currentUser
        return if (user != null && user.loggedIn) Screen.Home.route
        else Screen.Authentication.route
    }
}

