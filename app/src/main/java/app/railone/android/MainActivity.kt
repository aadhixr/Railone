package app.railone.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import app.railone.android.ui.HomeScreen
import app.railone.android.ui.SearchStationScreen
import app.railone.android.ui.SplashScreen
import app.railone.android.ui.UnreservedJourneyScreen
import app.railone.android.ui.UnreservedTicketScreen
import app.railone.android.ui.theme.RailOneTheme

sealed class Screen {
    object Splash : Screen()
    object Home : Screen()
    object UnreservedTicket : Screen()
    data class SearchStation(val isSource: Boolean) : Screen()
    object UnreservedJourney : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RailOneTheme {
                val focusManager = LocalFocusManager.current
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
                var sourceStation by remember { mutableStateOf("") }
                var destinationStation by remember { mutableStateOf("") }

                when (val screen = currentScreen) {
                    is Screen.Splash -> {
                        SplashScreen(onTimeout = { currentScreen = Screen.Home })
                    }
                    is Screen.Home -> {
                        HomeScreen(
                            onUnreservedClick = { currentScreen = Screen.UnreservedTicket }
                        )
                    }
                    is Screen.UnreservedTicket -> {
                        UnreservedTicketScreen(
                            sourceStation = sourceStation,
                            destinationStation = destinationStation,
                            onClose = { currentScreen = Screen.Home },
                            onFromClick = { currentScreen = Screen.SearchStation(isSource = true) },
                            onToClick = { currentScreen = Screen.SearchStation(isSource = false) },
                            onProceedToBook = { 
                                focusManager.clearFocus()
                                currentScreen = Screen.UnreservedJourney 
                            }
                        )
                    }
                    is Screen.UnreservedJourney -> {
                        UnreservedJourneyScreen(
                            sourceStation = sourceStation,
                            destinationStation = destinationStation,
                            onBack = { currentScreen = Screen.UnreservedTicket }
                        )
                    }
                    is Screen.SearchStation -> {
                        SearchStationScreen(
                            isSource = screen.isSource,
                            onClose = { currentScreen = Screen.UnreservedTicket },
                            onStationSelected = { station: String ->
                                if (screen.isSource) {
                                    sourceStation = station
                                } else {
                                    destinationStation = station
                                }
                                focusManager.clearFocus()
                                currentScreen = Screen.UnreservedTicket
                            }
                        )
                    }
                }
            }
        }
    }
}
