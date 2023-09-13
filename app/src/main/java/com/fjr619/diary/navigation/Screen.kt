package com.fjr619.diary.navigation

import com.fjr619.diary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    data object Authentication: Screen(route = "authentication_screen")
    data object Home: Screen(route = "home_screen")
    data object Write: Screen(route = "write_screen?$WRITE_SCREEN_ARGUMENT_KEY={$WRITE_SCREEN_ARGUMENT_KEY}") {
        fun passDiaryId(diaryId: String) = this.route.replace("{$WRITE_SCREEN_ARGUMENT_KEY}", diaryId)
    }
}
