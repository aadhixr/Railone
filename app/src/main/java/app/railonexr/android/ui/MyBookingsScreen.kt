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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onTicketClick: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }

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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
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
            Column {
                BookingStatusTabs(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
                RailOneBottomNavigation(
                    selectedLabel = "My Bookings",
                    onNavClick = { screen ->
                        if (screen == Screen.Home) onBack()
                    }
                )
            }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val upcoming = BookingManager.getUpcomingTickets()
            val completed = BookingManager.getCompletedTickets()
            val cancelled = BookingManager.getCancelledTickets()

            if (tickets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.offset(y = (-40).dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_ticket_logo),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No Tickets Found. Swipe down to refresh.", 
                            color = Color.Gray.copy(alpha = 0.8f), 
                            fontSize = 14.sp,
                            fontFamily = RobotoFamily,
                            fontWeight = FontWeight.Light,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (selectedTab == 3) { // All tab - group with headings
                        if (upcoming.isNotEmpty()) {
                            item { StatusHeading("Upcoming (${upcoming.size})", Color(0xFFF9A825)) }
                            items(upcoming) { UpcomingTicketCard(it, onTicketClick) }
                        }
                        if (completed.isNotEmpty()) {
                            item { StatusHeading("Completed (${completed.size})", Color(0xFF4CAF50)) }
                            items(completed) { CompletedTicketCard(it, onTicketClick) }
                        }
                        if (cancelled.isNotEmpty()) {
                            item { StatusHeading("Cancelled (${cancelled.size})", Color.Red) }
                            items(cancelled) { CancelledTicketCard(it, onTicketClick) }
                        }
                    } else {
                        items(tickets) { ticket ->
                            when (ticket.status) {
                                TicketStatus.UPCOMING -> UpcomingTicketCard(ticket, onTicketClick)
                                TicketStatus.COMPLETED -> CompletedTicketCard(ticket, onTicketClick)
                                TicketStatus.CANCELLED -> CancelledTicketCard(ticket, onTicketClick)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusHeading(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

@Composable
fun BookingStatusTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("Upcoming", "Completed", "Cancelled", "All")
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF1F8FF), 
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                
                val activeColor = when(index) {
                    0 -> Color(0xFFF9A825) // Upcoming - Yellow
                    1 -> Color(0xFF4CAF50) // Completed - Green
                    2 -> Color.Red          // Cancelled - Red
                    else -> Color(0xFF005AC1) // All - Blue
                }
                
                val iconRes = if (isSelected) {
                    when(index) {
                        0 -> R.drawable.upcoming_icon
                        1 -> R.drawable.completed_icon
                        2 -> R.drawable.cancelled_icon
                        else -> R.drawable.all_icon
                    }
                } else {
                    R.drawable.ticket_icon_grey
                }
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 4.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .clickable { onTabSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) activeColor else Color.Gray,
                            fontFamily = RobotoFamily
                        )
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
            .aspectRatio(338f / 154f)
            .clickable(
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            ) { onClick(ticket.ticketId) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.yellow_upcoming_template),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = df.format(Date(ticket.bookedAt)),
                color = Color(0xFF6D4C41),
                fontFamily = AvenirFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 18.dp, y = 35.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .offset(y = 82.dp) // Shifted up from 90+ to sit above line
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.source.substringBefore(" -").trim().uppercase(),
                    color = Color.Black,
                    fontFamily = AvenirFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                Text(
                    text = ticket.destination.substringBefore(" -").trim().uppercase(),
                    color = Color.Black,
                    fontFamily = AvenirFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
            
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-40).dp, y = (-26).dp) // Aligned inside bottom slot
                    .fillMaxWidth(0.65f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f).height(38.dp), contentAlignment = Alignment.Center) {
                    Text("Book Again", color = Color(0xFF1565C0), fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = AvenirFamily)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f).height(38.dp), contentAlignment = Alignment.Center) {
                    Text("View Details", color = Color(0xFF1565C0), fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = AvenirFamily)
                }
            }
        }
    }
}

@Composable
fun CompletedTicketCard(ticket: Ticket, onClick: (String) -> Unit) {
    val df = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(338f / 154f)
            .clickable(
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            ) { onClick(ticket.ticketId) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.green_completed_template),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = df.format(Date(ticket.bookedAt)),
                color = Color(0xFF455A64),
                fontFamily = AvenirFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 18.dp, y = 35.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .offset(y = 82.dp)
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.source.substringBefore(" -").trim().uppercase(),
                    color = Color.Black,
                    fontFamily = AvenirFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                Text(
                    text = ticket.destination.substringBefore(" -").trim().uppercase(),
                    color = Color.Black,
                    fontFamily = AvenirFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
            
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-40).dp, y = (-26).dp)
                    .fillMaxWidth(0.65f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f).height(38.dp), contentAlignment = Alignment.Center) {
                    Text("Book Again", color = Color(0xFF1565C0), fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = AvenirFamily)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f).height(38.dp), contentAlignment = Alignment.Center) {
                    Text("View Details", color = Color(0xFF1565C0), fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = AvenirFamily)
                }
            }
        }
    }
}

@Composable
fun CancelledTicketCard(ticket: Ticket, onClick: (String) -> Unit) {
    val df = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(338f / 154f)
            .clickable(
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            ) { onClick(ticket.ticketId) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.red_cancelled_template),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = df.format(Date(ticket.bookedAt)),
                color = Color(0xFFD32F2F),
                fontFamily = AvenirFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 18.dp, y = 35.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .offset(y = 82.dp)
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.source.substringBefore(" -").trim().uppercase(),
                    color = Color.Black,
                    fontFamily = AvenirFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                Text(
                    text = ticket.destination.substringBefore(" -").trim().uppercase(),
                    color = Color.Black,
                    fontFamily = AvenirFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
            
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-40).dp, y = (-26).dp)
                    .fillMaxWidth(0.65f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f).height(38.dp), contentAlignment = Alignment.Center) {
                    Text("Book Again", color = Color(0xFF1565C0), fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = AvenirFamily)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f).height(38.dp), contentAlignment = Alignment.Center) {
                    Text("View Details", color = Color(0xFF1565C0), fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = AvenirFamily)
                }
            }
        }
    }
}
