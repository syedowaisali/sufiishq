package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.PlayerDataProvider
import pk.sufiishq.app.helpers.Screen
import pk.sufiishq.app.ui.components.NavigationHost
import pk.sufiishq.app.ui.components.Player

@Composable
fun MainView(playerDataProvider: PlayerDataProvider) {
    val matColors = MaterialTheme.colors
    val navController = rememberNavController()

    Surface(color = matColors.background) {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = matColors.background,
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.hp_logo),
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Screen.Help.route)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_help_outline_24),
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            bottomBar = {
                Player(matColors = matColors, playerDataProvider = playerDataProvider)
            },
            content = { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    NavigationHost(playerDataProvider, navController)
                }
            }
        )
    }
}