package my.com.syazli.outflow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import my.com.syazli.outflow.presentation.navigation.NavGraph
import my.com.syazli.outflow.presentation.navigation.rememberAppState
import my.com.syazli.outflow.utils.AuthHelper

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isFirstLaunch = !AuthHelper.getAuthState()

        setContent {
            val appState = rememberAppState()
            MaterialTheme {
                NavGraph(appState, isFirstLaunch)
            }
        }
    }
}