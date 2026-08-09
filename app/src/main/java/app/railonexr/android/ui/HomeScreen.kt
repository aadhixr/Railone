package app.railonexr.android.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.railonexr.android.ui.theme.RailOneTheme
import app.railonexr.android.R
import app.railonexr.android.BuildConfig
import app.railonexr.android.logic.UpdateManager
import kotlinx.coroutines.launch
import app.railonexr.android.logic.BookingManager
import app.railonexr.android.Screen
import app.railonexr.android.logic.Ticket
import java.text.SimpleDateFormat
import java.util.*

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    RailOneTheme {
        HomeScreen()
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onUnreservedClick: () -> Unit = {},
    onBookingClick: (String) -> Unit = {},
    onBottomNavClick: (Screen) -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    var showMenu by remember { mutableStateOf(false) }
    val updateRelease = UpdateManager.updateAvailable
    
    val latestTicket by remember { 
        derivedStateOf { BookingManager.getUpcomingTickets().firstOrNull() } 
    }

    LaunchedEffect(Unit) {
        UpdateManager.checkForUpdates()
    }

    Scaffold(
        topBar = { 
            RailOneTopBar() 
        },
        bottomBar = { 
            RailOneBottomNavigation(
                selectedLabel = "Home",
                onNavClick = onBottomNavClick,
                onMenuClick = { showMenu = true }
            ) 
        },
        containerColor = Color.White
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp)
            ) {
                // Update Banner
                updateRelease?.let { release ->
                    val cleanTag = release.tagName.startsWith("v", ignoreCase = true).let {
                        if (it) release.tagName else "v${release.tagName}"
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE91E63))
                            .clickable { uriHandler.openUri(release.htmlUrl) }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "New Update Available ($cleanTag)! Tap to download.",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    GreetingSection("Aadil Muhammed")
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    JourneyPlannerSection(onUnreservedClick = onUnreservedClick)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    OfferingsSection()

                    val currentTicket = latestTicket
                    if (currentTicket != null) {
                        Spacer(modifier = Modifier.height(24.dp))
                        UpcomingJourneySection(
                            ticket = currentTicket,
                            onViewDetails = { onBookingClick(currentTicket.ticketId) }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    TriviaSection()
                    Spacer(modifier = Modifier.height(24.dp))
                    SocialMediaSection()
                }
            }

            // Enhanced Menu Dialog
            if (showMenu) {
                androidx.compose.ui.window.Dialog(onDismissRequest = { showMenu = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_logo),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "RailOne",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1A237E)
                            )
                            Text(
                                text = "Current Version: v${BuildConfig.VERSION_NAME}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "GitHub Official Build",
                                fontSize = 10.sp,
                                color = Color.LightGray
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Status indicator or Error
                            val error = UpdateManager.errorMessage
                            if (error != null) {
                                Text(
                                    text = error,
                                    color = Color.Red,
                                    fontSize = 11.sp,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (updateRelease != null) Color(0xFFFFF1F0) else Color(0xFFF6FFED))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (updateRelease != null) Color.Red else Color(0xFF52C41A))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (updateRelease != null) "Update Available" else "Up to date",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (updateRelease != null) Color.Red else Color(0xFF52C41A)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    showMenu = false
                                    scope.launch {
                                        UpdateManager.checkForUpdates()
                                    }
                                },
                                enabled = !UpdateManager.isChecking,
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF005AC1),
                                    contentColor = Color.White
                                )
                            ) {
                                if (UpdateManager.isChecking) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (updateRelease != null) Icons.Default.Download else Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (updateRelease != null) "Download Now" else "Check for Updates",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RailOneTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Language Toggle (Left)
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFE3F2FD)) // Light blue background
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Text("अA", color = Color(0xFF005AC1), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        // RailOne Logo (Center)
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "RailOne Logo",
            modifier = Modifier.height(32.dp),
            contentScale = ContentScale.Fit
        )

        // Notification Icon (Right)
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5))
                .align(Alignment.CenterEnd)
                .border(1.dp, Color(0xFFE0E0E0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp),
                contentAlignment = Alignment.Center
            ) {
                Text("15", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun UpcomingJourneySection(ticket: Ticket, onViewDetails: () -> Unit) {
    val df = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Upcoming Journey",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(338f / 154f) // Responsive aspect ratio for the template
                .clickable(
                    indication = null,
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                ) { onViewDetails() }
        ) {
            // Template Background - FillBounds ensures it occupies the exact box area
            Image(
                painter = painterResource(id = R.drawable.upcoming_template),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            
            Box(modifier = Modifier.fillMaxSize()) {
                // Date - Positioned top-left
                Text(
                    text = df.format(Date(ticket.bookedAt)),
                    color = Color(0xFFE8DFF8),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    fontSize = TicketUIConfig.fontSizeDate,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = TicketUIConfig.dateOffsetStart, y = TicketUIConfig.dateOffsetTop)
                )

                // Stations - Manual vertical placement
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .offset(y = TicketUIConfig.stationsOffsetTop)
                        .padding(horizontal = TicketUIConfig.stationsPaddingHorizontal),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ticket.source.substringBefore(" -").trim().uppercase(),
                        color = Color(0xFFF2EDF8),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = TicketUIConfig.fontSizeStations
                    )

                    Text(
                        text = ticket.destination.substringBefore(" -").trim().uppercase(),
                        color = Color(0xFFF2EDF8),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = TicketUIConfig.fontSizeStations
                    )
                }
                
                // Badge - Positioned bottom-left
                Text(
                    text = "Unreserved",
                    color = Color(0xFFD8FF4A),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = TicketUIConfig.fontSizeBadge,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = TicketUIConfig.badgeOffsetStart, y = -TicketUIConfig.badgeOffsetBottom)
                )
                
                // Buttons - Positioned bottom-right
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = -TicketUIConfig.buttonsOffsetEnd, y = -TicketUIConfig.buttonsOffsetBottom)
                        .fillMaxWidth(0.65f), // Relative width for the button area
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Responsive button areas centered in the template's boxes
                    Box(
                        modifier = Modifier
                            .weight(TicketUIConfig.weightBookAgain)
                            .height(TicketUIConfig.buttonHeight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Book Again",
                            color = Color.White,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = TicketUIConfig.fontSizeButtons,
                            modifier = Modifier.offset(
                                x = TicketUIConfig.buttonLabelOffsetX,
                                y = TicketUIConfig.buttonLabelOffsetY
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(TicketUIConfig.buttonGap))

                    Box(
                        modifier = Modifier
                            .weight(TicketUIConfig.weightViewDetails)
                            .height(TicketUIConfig.buttonHeight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "View Details",
                            color = Color.White,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = TicketUIConfig.fontSizeButtons,
                            modifier = Modifier.offset(
                                x = TicketUIConfig.buttonLabelOffsetX,
                                y = TicketUIConfig.buttonLabelOffsetY
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GreetingSection(name: String) {
    Text(
        text = "Hi, $name!",
        color = Color(0xFF1A237E),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
}

@Composable
fun JourneyPlannerSection(onUnreservedClick: () -> Unit) {
    Column {
        Text(
            text = "Journey Planner",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            JourneyCard(Modifier.weight(1f), "Reserved", Color(0xFFE3F2FD), imageRes = R.drawable.reserved)
            JourneyCard(Modifier.weight(1f), "Unreserved", Color(0xFFF3E5F5), imageRes = R.drawable.unreserved, onClick = onUnreservedClick)
            JourneyCard(Modifier.weight(1f), "Platform", Color(0xFFFFF3E0), imageRes = R.drawable.platform)
        }
    }
}

@Composable
fun JourneyCard(
    modifier: Modifier, 
    title: String, 
    bgColor: Color,
    imageRes: Int,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(16.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            // Using Image instead of Icon for illustrations
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
            fontSize = 13.sp
        )
    }
}

@Composable
fun OfferingsSection() {
    Column {
        Text(
            text = "More Offerings",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
        Spacer(modifier = Modifier.height(20.dp))
        
        val items = listOf(
            OfferingItem("Search Trains", R.drawable.search_train, Color(0xFFFFE4E8)),
            OfferingItem("PNR Status", R.drawable.pnr_status, Color(0xFFE8F5E9)),
            OfferingItem("Coach Position", R.drawable.coach_position, Color(0xFFE3F2FD)),
            OfferingItem("Track Your Train", R.drawable.track_your_train, Color(0xFFFFF3E0)),
            OfferingItem("Order Food", R.drawable.order_food, Color(0xFFE8EAF6)),
            OfferingItem("File Refund", R.drawable.file_refund, Color(0xFFEEEEEE)),
            OfferingItem("Rail Madad", R.drawable.rail_madad, Color(0xFFFBE9E7)),
            OfferingItem("Go To WAVES", R.drawable.go_to_waves, Color(0xFFF3E5F5))
        )

        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            items.chunked(4).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { item ->
                        OfferingCard(item, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

data class OfferingItem(val title: String, val iconRes: Int, val color: Color)

@Composable
fun OfferingCard(item: OfferingItem, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.title,
            fontSize = 11.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
            lineHeight = 14.sp
        )
    }
}

@Composable
fun TriviaSection() {
    Column {
        Text(
            text = "Do You know?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            item { 
                TriviaCard(
                    "First ever passenger train was run between Bori Bandar to Thane on April 16, 1853.",
                    R.drawable.info_1
                ) 
            }
            item { 
                TriviaCard(
                    "Chenab Railway Bridge in Dharot, Jammu & Kashmir is the World's highest Railway Bridge.",
                    R.drawable.info_2
                ) 
            }
            item { 
                TriviaCard(
                    "Noney Bridge is the tallest railway bridge in India with 141 meters.",
                    R.drawable.info_3
                ) 
            }
            item { 
                TriviaCard(
                    "Shree Siddharoodha Swamiji Railway Station Hubballi is world's longest Railway Platform with length of 1505 meters.",
                    R.drawable.info_4
                ) 
            }
            item { 
                TriviaCard(
                    "99% Electrification is achieved in Indian Railways.",
                    R.drawable.info_5
                ) 
            }
        }
    }
}

@Composable
fun TriviaCard(title: String, imageRes: Int) {
    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title, 
                fontSize = 12.sp, 
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A237E),
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun SocialMediaSection() {
    Column {
        Text(
            text = "Follow Us On Social Media Platforms",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        // Banner Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            Text("Social Media Banner", color = Color(0xFF1A237E))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SocialIcon(Icons.Default.Facebook, Color(0xFF1877F2))
            SocialIcon(Icons.Default.Share, Color(0xFF000000)) // Fallback for X
            SocialIcon(Icons.Default.CameraAlt, Color(0xFFE4405F)) // Fallback for Instagram
            SocialIcon(Icons.Default.PlayCircle, Color(0xFFFF0000))
        }
    }
}

@Composable
fun SocialIcon(icon: ImageVector, color: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun RailOneBottomNavigation(
    selectedLabel: String, 
    onNavClick: (Screen) -> Unit,
    onMenuClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = Color(0xFF005AC1),
        tonalElevation = 8.dp
    ) {
        val navItems = listOf(
            Triple("Home", R.drawable.home, Screen.Home),
            Triple("My Bookings", R.drawable.bookings, Screen.MyBookings(3)),
            Triple("You", R.drawable.you, Screen.Home),
            Triple("Menu", R.drawable.menu, Screen.Home)
        )

        navItems.forEach { (label, iconRes, screen) ->
            val isSelected = label == selectedLabel
            val activeColor = Color(0xFFFFA726)
            
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (label == "Menu") {
                        onMenuClick()
                    } else {
                        onNavClick(screen)
                    }
                },
                icon = { 
                    Image(
                        painter = painterResource(id = iconRes), 
                        contentDescription = label,
                        modifier = Modifier.size(26.dp),
                        colorFilter = if (isSelected) androidx.compose.ui.graphics.ColorFilter.tint(activeColor) else null
                    ) 
                },
                label = { 
                    Text(
                        text = label, 
                        color = if (isSelected) activeColor else Color.White,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = activeColor,
                    unselectedIconColor = Color.White,
                    selectedTextColor = activeColor,
                    unselectedTextColor = Color.White,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
