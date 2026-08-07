package app.railonexr.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import app.railonexr.android.ui.HomeScreen
import app.railonexr.android.ui.SearchStationScreen
import app.railonexr.android.ui.SplashScreen
import app.railonexr.android.ui.UnreservedJourneyScreen
import app.railonexr.android.ui.UnreservedTicketScreen
import app.railonexr.android.ui.MakePaymentScreen
import app.railonexr.android.ui.BookingDetailsScreen
import app.railonexr.android.ui.MyBookingsScreen
import app.railonexr.android.ui.theme.RailOneTheme
import app.railonexr.android.logic.BookingManager

sealed class Screen {
    object Splash : Screen()
    object Home : Screen()
    object UnreservedTicket : Screen()
    data class SearchStation(val isSource: Boolean) : Screen()
    object UnreservedJourney : Screen()
    data class MakePayment(
        val source: String, 
        val dest: String, 
        val totalFare: String,
        val adultCount: Int,
        val childCount: Int,
        val trainType: String,
        val classType: String
    ) : Screen()
    data class BookingDetails(val ticketId: String) : Screen()
    data class MyBookings(val initialTab: Int = 3) : Screen() // 3 is "All"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BookingManager.init(this)
        enableEdgeToEdge()
        setContent {
            RailOneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
                                onUnreservedClick = { currentScreen = Screen.UnreservedTicket },
                                onBookingClick = { ticketId -> currentScreen = Screen.BookingDetails(ticketId) },
                                onBottomNavClick = { navScreen -> currentScreen = navScreen }
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
                                },
                                onSwap = {
                                    val temp = sourceStation
                                    sourceStation = destinationStation
                                    destinationStation = temp
                                },
                                onRecentSearchClick = { from, to ->
                                    sourceStation = from
                                    destinationStation = to
                                }
                            )
                        }
                        is Screen.UnreservedJourney -> {
                            UnreservedJourneyScreen(
                                sourceStation = sourceStation,
                                destinationStation = destinationStation,
                                onBack = { currentScreen = Screen.UnreservedTicket },
                                onBookNow = { fare, adults, children, train, cls ->
                                    currentScreen = Screen.MakePayment(
                                        source = sourceStation,
                                        dest = destinationStation,
                                        totalFare = fare,
                                        adultCount = adults,
                                        childCount = children,
                                        trainType = train,
                                        classType = cls
                                    )
                                }
                            )
                        }
                        is Screen.MakePayment -> {
                            MakePaymentScreen(
                                source = screen.source,
                                dest = screen.dest,
                                totalFare = screen.totalFare,
                                adultCount = screen.adultCount,
                                childCount = screen.childCount,
                                trainType = screen.trainType,
                                classType = screen.classType,
                                onBack = { currentScreen = Screen.UnreservedJourney },
                                onSuccess = { ticketId ->
                                    currentScreen = Screen.BookingDetails(ticketId)
                                }
                            )
                        }
                        is Screen.BookingDetails -> {
                            BookingDetailsScreen(
                                ticketId = screen.ticketId,
                                onBack = { currentScreen = Screen.Home }
                            )
                        }
                        is Screen.MyBookings -> {
                            MyBookingsScreen(
                                initialTab = screen.initialTab,
                                onBack = { currentScreen = Screen.Home },
                                onTicketClick = { ticketId ->
                                    currentScreen = Screen.BookingDetails(ticketId)
                                },
                                onBottomNavClick = { navScreen -> currentScreen = navScreen }
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
}
