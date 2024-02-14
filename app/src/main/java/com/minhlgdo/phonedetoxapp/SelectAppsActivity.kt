package com.minhlgdo.phonedetoxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.minhlgdo.phonedetoxapp.data.repository.PhoneAppRepository
import com.minhlgdo.phonedetoxapp.ui.presentation.select_apps.SelectAppsScreen
import com.minhlgdo.phonedetoxapp.ui.theme.PhoneDetoxAppTheme
import com.minhlgdo.phonedetoxapp.viewmodels.SelectAppsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectAppsActivity : ComponentActivity() {
    @Inject
    lateinit var phoneAppRepository: PhoneAppRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhoneDetoxAppTheme {
                val viewModel = hiltViewModel<SelectAppsViewModel>()
                SelectAppsScreen(viewModel = viewModel, onBack = { finish() })
            }
        }
    }



}
