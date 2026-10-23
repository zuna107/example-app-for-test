package com.mysima.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mysima.MainActivity
import com.mysima.home.ui.HomeScreen
import com.mysima.theme.MySIMATheme

class HomeActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userName = intent.getStringExtra(EXTRA_USER_NAME).orEmpty()
        val userEmail = intent.getStringExtra(EXTRA_USER_EMAIL).orEmpty()
        viewModel.setUser(userName, userEmail)

        setContent {
            MySIMATheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                HomeScreen(
                    uiState = uiState,
                    onLogoutConfirmed = {
                        val intent = Intent(this@HomeActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                        finish()
                    },
                    onToggleLogoutDialog = { show ->
                        viewModel.toggleLogoutDialog(show)
                    }
                )
            }
        }
    }

    companion object {
        const val EXTRA_USER_NAME = "extra_user_name"
        const val EXTRA_USER_EMAIL = "extra_user_email"

        fun start(context: Context, name: String, email: String) {
            val intent = Intent(context, HomeActivity::class.java).apply {
                putExtra(EXTRA_USER_NAME, name)
                putExtra(EXTRA_USER_EMAIL, email)
            }
            context.startActivity(intent)
        }
    }
}
