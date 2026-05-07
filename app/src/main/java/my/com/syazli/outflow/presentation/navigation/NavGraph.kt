package my.com.syazli.outflow.presentation.navigation

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import my.com.syazli.outflow.R
import my.com.syazli.outflow.presentation.screen.add.AddScreen
import my.com.syazli.outflow.presentation.screen.list.ListScreen
import my.com.syazli.outflow.presentation.screen.main.MainScreen
import my.com.syazli.outflow.utils.AuthHelper

@Composable
fun NavGraph(appState: AppState, isFirstLaunch: Boolean) {
    val startDestination = if (isFirstLaunch) Screen.Main.route else Screen.List.route

    NavHost(
        appState.navController,
        startDestination,
        modifier = Modifier.background(colorResource(R.color.dark)),
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(400, easing = EaseInOutCubic)) + fadeIn(animationSpec = tween(400))
            },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it/3 },
                animationSpec = tween(400, easing = EaseInOutCubic)) + fadeOut(animationSpec = tween(400))

            },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it/3},
                animationSpec = tween(400, easing = EaseInOutCubic)) + fadeIn(animationSpec = tween(400)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(400, easing = EaseInOutCubic)) + fadeOut(animationSpec = tween(400))
        }
    ) {


        composable(route = Screen.Main.route) {
            MainScreen(onGetStartedClick = {
                AuthHelper.setAuthState()
                appState.navActions.navigateToList()
            })
        }

        composable(route = Screen.List.route) {
            ListScreen(onAddClick = { type ->
                appState.navActions.navigateToAdd(type)
            }, onEdit = { id ->
                appState.navActions.navigateToEdit(id)
            })
        }

        composable(route = "${Screen.Add.route}/{type}") { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "expense"
            AddScreen(
                type = type,
                onBackClick = { appState.navActions.navigateToBack() }
            )
        }

        composable(route = "${Screen.Edit.route}/edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
            AddScreen(
                type = "",
                transactionId = id,
                onBackClick = { appState.navActions.navigateToBack() }
            )
        }
    }
}
