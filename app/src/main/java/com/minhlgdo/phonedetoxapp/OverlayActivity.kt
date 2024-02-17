package com.minhlgdo.phonedetoxapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import com.minhlgdo.phonedetoxapp.ui.presentation.overlay.OverlayScreen
import com.minhlgdo.phonedetoxapp.ui.theme.PhoneDetoxAppTheme
import com.minhlgdo.phonedetoxapp.viewmodels.OverlayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverlayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel : OverlayViewModel by viewModels()

        setContent {
            PhoneDetoxAppTheme {
                OverlayScreen(viewModel = viewModel)
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // go back to the home screen
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(startMain)
    }


}