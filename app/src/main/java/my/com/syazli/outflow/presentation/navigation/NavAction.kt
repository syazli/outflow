package my.com.syazli.outflow.presentation.navigation

import android.util.Log
import androidx.navigation.NavHostController

class NavAction(private val navController: NavHostController) {

    fun navigateToList() {
        navController.navigate(Screen.List.route) {
            popUpTo(Screen.Main.route) {
                inclusive = true
            }
        }
    }

    fun navigateToAdd(type: String) {
        navController.navigate("${Screen.Add.route}/$type")
    }

    fun navigateToEdit(id: Int) {
        Log.d("NavAction", "navigateToEdit: $id")
        navController.navigate("${Screen.Edit.route}/edit/$id")
    }

    fun navigateToBack() {
        navController.popBackStack()
    }
}