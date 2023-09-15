package com.fjr619.diary.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.fjr619.diary.R
import com.fjr619.diary.data.repository.Diaries
import com.fjr619.diary.util.RequestState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    diaries: Diaries,
    drawerState: DrawerState,
    onMenuClicked: () -> Unit,
    onDateSelected: () -> Unit,
    onSignoutClicked: () -> Unit,
    navigateToWrite: () -> Unit,
) {
    var padding by remember {
        mutableStateOf(PaddingValues())
    }
    NavigationDrawer(
        drawerState = drawerState,
        onSignoutClicked = onSignoutClicked
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    onMenuClicked = onMenuClicked,
                    onDateSelected = onDateSelected
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(
                        end = padding.calculateEndPadding(LayoutDirection.Ltr)
                    ),
                    onClick = { navigateToWrite() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "New Diary Icon"
                    )
                }
            },
            content = {
                padding = it
                when (diaries) {
                    is RequestState.Success -> {
                        HomeContent(
                            viewModel = viewModel,
                            paddingValues = it,
                            diaries = diaries.data,
                            onclick = {})
                    }

                    is RequestState.Error -> {
                        EmptyPage(
                            title = "Error",
                            subtitle = "${diaries.error.message}"
                        )
                    }

                    is RequestState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    else -> {

                    }
                }

            }
        )
    }
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignoutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        content = content,
        drawerContent = {
            ModalDrawerSheet {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Image"
                )

                NavigationDrawerItem(
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    label = {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = "Google Logo",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Sign Out",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    selected = false,
                    onClick = onSignoutClicked
                )
            }
        }
    )
}

@Composable
@Preview
fun NavigationDrawerPreview() {
    NavigationDrawer(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
        onSignoutClicked = { /*TODO*/ }) {

    }
}