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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.railonexr.android.MainActivity
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

    val tickets = when (selectedTab) {
        0 -> BookingManager.getUpcomingTickets()
        1 -> BookingManager.getCompletedTickets()
        2 -> BookingManager.getCancelledTickets()
        else -> BookingManager.bookings
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
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(ticket.ticketId) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF9575CD), Color(0xFF7E57C2))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(df.format(Date(ticket.bookedAt)), color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(ticket.source.substringBefore(" -").trim(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(ticket.destination.substringBefore(" -").trim(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Unreserved", color = Color(0xFFC6FF00), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Button(
                            onClick = { },
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                        ) {
                            Text("Book Again", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onClick(ticket.ticketId) },
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                        ) {
                            Text("View Details", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompletedTicketCard(ticket: Ticket, onClick: (String) -> Unit) {
    val df = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(ticket.ticketId) },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.3f)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF3E5F5))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("Unreserved", color = Color(0xFF7B1FA2), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Text("UTS: ${ticket.utsId}", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Ticket Type", fontSize = 10.sp, color = Color.Gray)
                    Text(ticket.trainType.substringBefore("/"), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Booking Date", fontSize = 10.sp, color = Color.Gray)
                    Text(df.format(Date(ticket.bookedAt)), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(ticket.source.substringBefore(" -").trim(), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text("--- 628 km ---", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                Text(ticket.destination.substringBefore(" -").trim(), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFF5F5F5))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Book Again", color = Color(0xFF005AC1), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color(0xFFF5F5F5)))
                TextButton(
                    onClick = { onClick(ticket.ticketId) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Details", color = Color(0xFF005AC1), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
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
