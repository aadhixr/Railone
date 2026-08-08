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
import androidx.compose.ui.text.font.Font
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

val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

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
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "RailOne Logo",
            modifier = Modifier.height(30.dp),
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
                .width(338.dp)
                .height(154.dp) // Locked size for the template aspect ratio
                .align(Alignment.CenterHorizontally)
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
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 18.dp)
            ) {
                // Date - Positioned top-left
                Text(
                    text = df.format(Date(ticket.bookedAt)),
                    color = Color(0xFFE8DFF8),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(24.dp)) // Aligns stations with the lines
                
                // Stations - Precise alignment on template lines
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ticket.source.substringBefore(" -").trim().uppercase(),
                        color = Color(0xFFF2EDF8),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )

                    Text(
                        text = ticket.destination.substringBefore(" -").trim().uppercase(),
                        color = Color(0xFFF2EDF8),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Bottom Row - Aligns with the boxes
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Unreserved",
                        color = Color(0xFFD8FF4A),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        // Invisible areas for clicking, text is centered in the template's boxes
                        Box(
                            modifier = Modifier
                                .width(116.dp)
                                .height(35.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Book Again",
                                color = Color.White,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        }
                        
                        Box(
                            modifier = Modifier
                                .width(89.dp)
                                .height(35.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "View Details",
                                color = Color.White,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        }
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
            OfferingItem("Search Trains", Icons.Outlined.Route, Color(0xFFFFE4E8)),
            OfferingItem("PNR Status", Icons.Outlined.ConfirmationNumber, Color(0xFFE8F5E9)),
            OfferingItem("Coach Position", Icons.Outlined.Train, Color(0xFFE3F2FD)),
            OfferingItem("Track Your Train", Icons.Outlined.MyLocation, Color(0xFFFFF3E0)),
            OfferingItem("Order Food", Icons.Outlined.Restaurant, Color(0xFFE8EAF6)),
            OfferingItem("File Refund", Icons.AutoMirrored.Outlined.AssignmentReturn, Color(0xFFEEEEEE)),
            OfferingItem("Rail Madad", Icons.Outlined.Handshake, Color(0xFFFBE9E7)),
            OfferingItem("Go To WAVES", Icons.Outlined.Layers, Color(0xFFF3E5F5))
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

data class OfferingItem(val title: String, val icon: ImageVector, val color: Color)

@Composable
fun OfferingCard(item: OfferingItem, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(item.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = if (item.title == "Search Trains") Color(0xFFE91E63) else Color(0xFF5C6BC0),
                modifier = Modifier.size(32.dp)
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
                    ""
                ) 
            }
            item { 
                TriviaCard(
                    "Chenab Railway Bridge in Dharot, Jammu & Kashmir is the World's highest Railway Bridge.",
                    ""
                ) 
            }
            item { 
                TriviaCard(
                    "Noney Bridge is the tallest railway bridge in India with 141 meters.",
                    ""
                ) 
            }
        }
    }
}

@Composable
fun TriviaCard(title: String, subtitle: String) {
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
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title, 
                fontSize = 12.sp, 
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A237E),
                lineHeight = 16.sp
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle, 
                    fontSize = 12.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = Color(0xFF005AC1)
                )
            }
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
    ) {
        val navItems = listOf(
            Triple("Home", Icons.Default.Home, Screen.Home),
            Triple("My Bookings", Icons.Default.Book, Screen.MyBookings(3)),
            Triple("You", Icons.Default.Person, Screen.Home),
            Triple("Menu", Icons.Default.Menu, Screen.Home)
        )

        navItems.forEach { (label, icon, screen) ->
            val isSelected = label == selectedLabel
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
                    Icon(
                        imageVector = icon, 
                        contentDescription = label,
                        tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                    ) 
                },
                label = { 
                    Text(
                        text = label, 
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                    indicatorColor = Color.White.copy(alpha = 0.2f)
                )
            )
        }
    }
}
