package app.railonexr.android.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.railonexr.android.MainActivity
import app.railonexr.android.R
import app.railonexr.android.Screen
import app.railonexr.android.logic.BookingManager
import app.railonexr.android.logic.Ticket
import app.railonexr.android.logic.TicketStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MyBookingsScreen(
    initialTab: Int,
    onBack: () -> Unit,
    onTicketClick: (String) -> Unit,
    onBottomNavClick: (Screen) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    val tabs = listOf("Upcoming", "Completed", "Cancelled", "All")

    val tickets by remember(selectedTab) {
        derivedStateOf {
            when (selectedTab) {
                0 -> BookingManager.getUpcomingTickets()
                1 -> BookingManager.getCompletedTickets()
                2 -> BookingManager.getCancelledTickets()
                else -> BookingManager.bookings
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF005AC1))
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                }
                Text(
                    "My Bookings", 
                    color = Color.White, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 48.dp)
                )
                IconButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Default.SwapVert, "Sort", tint = Color.White)
                }
            }
        },
        bottomBar = {
            RailOneBottomNavigation(
                selectedLabel = "My Bookings",
                onNavClick = onBottomNavClick
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF5F9FF),
                contentColor = Color(0xFF005AC1),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF005AC1)
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            if (tickets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Inbox, 
                            null, 
                            modifier = Modifier.size(64.dp), 
                            tint = Color.LightGray
                        )
                        Text(
                            "No Tickets Found. Swipe down to refresh.", 
                            color = Color.Gray, 
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tickets) { ticket ->
                        if (ticket.isExpired || ticket.status == TicketStatus.COMPLETED) {
                            CompletedTicketCard(ticket, onTicketClick)
                        } else {
                            UpcomingTicketCard(ticket, onTicketClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UpcomingTicketCard(ticket: Ticket, onClick: (String) -> Unit) {
    val df = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(338f / 154f) // Responsive aspect ratio for the template
            .clickable(
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            ) { onClick(ticket.ticketId) }
    ) {
        // Template Background
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
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.weight(0.45f)) // Pushed Stations lower to align with template line
            
            // Stations
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
                    fontSize = 18.sp
                )

                Text(
                    text = ticket.destination.substringBefore(" -").trim().uppercase(),
                    color = Color(0xFFF2EDF8),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bottom Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp), // Reduced padding for better button centering
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Unreserved",
                    color = Color(0xFFD8FF4A),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1.05f)
                            .height(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Book Again",
                            color = Color.White,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .weight(0.85f)
                            .height(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "View Details",
                            color = Color.White,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
        }
    }

@Composable
fun CompletedTicketCard(ticket: Ticket, onClick: (String) -> Unit) {
    val df = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
    
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(ticket.ticketId) },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF4CAF50)), // Solid green border
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF3E5F5))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Unreserved", color = Color(0xFF7B1FA2), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("UTS: ${ticket.utsId}", fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(14.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Ticket Type", fontSize = 12.sp, color = Color.Gray)
                        Text("JOURNEY", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Booking Date", fontSize = 12.sp, color = Color.Gray)
                        Text(df.format(Date(ticket.bookedAt)), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(14.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(ticket.source.substringBefore(" -").trim(), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("--- 628 km ---", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(ticket.destination.substringBefore(" -").trim(), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Dashed Divider Container to help alignment
                Box(
                    modifier = Modifier.fillMaxWidth().height(1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
                        val strokeWidth = 1.dp.toPx()
                        val dashWidth = 10f
                        val gapWidth = 10f
                        var x = 0f
                        while (x < size.width) {
                            drawLine(
                                color = Color.LightGray,
                                start = androidx.compose.ui.geometry.Offset(x, 0f),
                                end = androidx.compose.ui.geometry.Offset(x + dashWidth, 0f),
                                strokeWidth = strokeWidth
                            )
                            x += dashWidth + gapWidth
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = { },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Book Again", color = Color(0xFF005AC1), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFF5F5F5)))
                    TextButton(
                        onClick = { onClick(ticket.ticketId) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("View Details", color = Color(0xFF005AC1), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
        
        // Side cutouts - Fixed relative position
        // Since the card has grown, we adjust the offset or use a better centering strategy.
        // For now, I'll fine-tune the Y based on the new content height.
        // Approx: Header(30) + Spacing(14) + Info(40) + Spacing(14) + Stations(30) + Spacing(20) = 148dp
        // The padding(18.dp) adds to the top. So around 166dp.
        Box(
            modifier = Modifier
                .size(20.dp)
                .offset(x = (-10).dp, y = 168.dp) 
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color(0xFF4CAF50), CircleShape)
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .size(20.dp)
                .offset(x = 10.dp, y = 168.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color(0xFF4CAF50), CircleShape)
                .align(Alignment.TopEnd)
        )
    }
}

@Composable
fun RailOneBottomNavigation(selectedLabel: String, onNavClick: (Screen) -> Unit) {
    NavigationBar(
        containerColor = Color(0xFF005AC1),
    ) {
        val navItems = listOf(
            Triple("Home", Icons.Default.Home, Screen.Home),
            Triple("My Bookings", Icons.Default.Book, Screen.MyBookings(3)),
            Triple("You", Icons.Default.Person, Screen.Home), // Placeholder
            Triple("Menu", Icons.Default.Menu, Screen.Home) // Handled by callback in HomeScreen
        )

        navItems.forEach { (label, icon, screen) ->
            val isSelected = label == selectedLabel
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavClick(screen) },
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
