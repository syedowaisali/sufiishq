package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.ui.screen.DashboardView
import pk.sufiishq.app.ui.screen.HelpView
import pk.sufiishq.app.ui.screen.PlaylistView
import pk.sufiishq.app.ui.screen.TracksView
import pk.sufiishq.app.viewmodels.KalamViewModel
import pk.sufiishq.app.viewmodels.PlaylistViewModel

@Composable
fun NavigationHost(playerDataProvider: PlayerDataProvider, navController: NavHostController) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

            // dashboard screen
            composable(Screen.Dashboard.route) {

                val kalamViewModel = hiltViewModel<KalamViewModel>()
                val playlistViewModel = hiltViewModel<PlaylistViewModel>()

                DashboardView(navController = navController, kalamViewModel, playlistViewModel)
            }

            // tracks screen
            composable(
                Screen.Tracks.route + "/{${Screen.Tracks.PARAM_TRACK_TYPE}}/{${Screen.Tracks.PARAM_TITLE}}/{${Screen.Tracks.PARAM_PLAYLIST_ID}}",
                arguments = listOf(
                    navArgument(Screen.Tracks.PARAM_TRACK_TYPE) {
                        type = NavType.StringType
                    },
                    navArgument(Screen.Tracks.PARAM_TITLE) {
                        type = NavType.StringType
                    },
                    navArgument(Screen.Tracks.PARAM_PLAYLIST_ID) {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { backStackEntry ->

                val trackType = backStackEntry.arguments?.getString(Screen.Tracks.PARAM_TRACK_TYPE)
                    ?: Screen.Tracks.ALL
                val title = backStackEntry.arguments?.getString(Screen.Tracks.PARAM_TITLE)
                    ?: stringResource(id = R.string.all)
                val playlistId =
                    backStackEntry.arguments?.getInt(Screen.Tracks.PARAM_PLAYLIST_ID) ?: 0

                val kalamViewModel = hiltViewModel<KalamViewModel>()
                val playlistViewModel = hiltViewModel<PlaylistViewModel>()

                TracksView(
                    playerDataProvider = playerDataProvider,
                    kalamDataProvider = kalamViewModel,
                    playlistDataProvider = playlistViewModel,
                    trackType = trackType,
                    title = title,
                    playlistId = playlistId,
                )
            }

            // playlist screen
            composable(Screen.Playlist.route) {
                val playlistViewModel = hiltViewModel<PlaylistViewModel>()
                PlaylistView(
                    playlistDataProvider = playlistViewModel,
                    navController = navController
                )
            }

            // help screen
            composable(Screen.Help.route) {
                HelpView()
            }
        }
    }

}