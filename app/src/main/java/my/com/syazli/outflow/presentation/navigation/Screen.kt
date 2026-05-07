package my.com.syazli.outflow.presentation.navigation

sealed class Screen (val route: String) {
    object Main : Screen("main")
    object List : Screen("list")
    object Add : Screen("add")
    object Edit : Screen("add/edit")

}