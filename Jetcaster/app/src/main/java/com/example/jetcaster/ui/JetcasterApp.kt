/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jetcaster.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import com.example.jetcaster.R
import com.example.jetcaster.ui.home.Home
import com.example.jetcaster.ui.player.PlayerScreen
import com.example.jetcaster.ui.player.PlayerViewModel

@Composable
fun JetcasterApp(
    windowSizeClass: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    appState: JetcasterAppState = rememberJetcasterAppState(),
) {
    if (appState.isOnline) {
        NavHost(
            navController = appState.navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) { backStackEntry ->
                Home(
                    navigateToPlayer = { episodeUri ->
                        appState.navigateToPlayer(episodeUri, backStackEntry)
                    }
                )
            }
            composable(Screen.Player.route) { backStackEntry ->
                val playerViewModel: PlayerViewModel = viewModel(
                    factory = PlayerViewModel.provideFactory(
                        owner = backStackEntry,
                        defaultArgs = backStackEntry.arguments
                    )
                )
                PlayerScreen(
                    playerViewModel,
                    windowSizeClass,
                    displayFeatures,
                    onBackPress = appState::navigateBack
                )
            }
        }
    } else {
        OfflineDialog()
    }
}

@Composable
fun OfflineDialog() {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.connection_error_title)) },
        text = { Text(text = stringResource(R.string.connection_error_message)) },
        confirmButton = {
            TextButton(onClick = {}) {
                Text(stringResource(R.string.retry_label))
            }
        }
    )
}
