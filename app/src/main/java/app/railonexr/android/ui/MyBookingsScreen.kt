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
    TicketShapeCard(
        ticket = ticket, 
        color = Color(0xFFFBC02D), // Yellow for Upcoming
        statusText = "Upcoming",
        statusColor = Color(0xFF7B1FA2),
        statusBg = Color(0xFFEAD9F4),
        onClick = onClick
    )
}

@Composable
fun CompletedTicketCard(ticket: Ticket, onClick: (String) -> Unit) {
    TicketShapeCard(
        ticket = ticket, 
        color = Color(0xFF20A36A), // Green for Completed
        statusText = "Boarded",
        statusColor = Color(0xFF2E7D32),
        statusBg = Color(0xFFE8F5E9),
        onClick = onClick
    )
}

@Composable
fun CancelledTicketCard(ticket: Ticket, onClick: (String) -> Unit) {
    TicketShapeCard(
        ticket = ticket, 
        color = Color(0xFFEF2222), // Red for Cancelled
        statusText = "Cancelled",
        statusColor = Color.Red,
        statusBg = Color(0xFFFFEBEE),
        onClick = onClick
    )
}

@Composable
fun TicketShapeCard(
    ticket: Ticket,
    color: Color,
    statusText: String,
    statusColor: Color,
    statusBg: Color,
    onClick: (String) -> Unit
) {
    val df = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1000f / 365f)
            .padding(vertical = 4.dp)
            .clickable(
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            ) { onClick(ticket.ticketId) }
    ) {
        // SVG-Style Path Drawing
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scaleX = size.width / 1000f
            val scaleY = size.height / 365f
            
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(24 * scaleX, 2 * scaleY)
                lineTo(976 * scaleX, 2 * scaleY)
                quadraticBezierTo(998 * scaleX, 2 * scaleY, 998 * scaleX, 24 * scaleY)
                lineTo(998 * scaleX, 150 * scaleY)
                cubicTo(965 * scaleX, 150 * scaleY, 945 * scaleX, 164 * scaleY, 945 * scaleX, 182.5f * scaleY)
                cubicTo(945 * scaleX, 201 * scaleY, 965 * scaleX, 215 * scaleY, 998 * scaleX, 215 * scaleY)
                lineTo(998 * scaleX, 341 * scaleY)
                quadraticBezierTo(998 * scaleX, 363 * scaleY, 976 * scaleX, 363 * scaleY)
                lineTo(24 * scaleX, 363 * scaleY)
                quadraticBezierTo(2 * scaleX, 363 * scaleY, 2 * scaleX, 341 * scaleY)
                lineTo(2 * scaleX, 215 * scaleY)
                cubicTo(35 * scaleX, 215 * scaleY, 55 * scaleX, 201 * scaleY, 55 * scaleX, 182.5f * scaleY)
                cubicTo(55 * scaleX, 164 * scaleY, 35 * scaleX, 150 * scaleY, 2 * scaleX, 150 * scaleY)
                lineTo(2 * scaleX, 24 * scaleY)
                quadraticBezierTo(2 * scaleX, 2 * scaleY, 24 * scaleX, 2 * scaleY)
                close()
            }
            
            drawPath(path = path, color = Color(0xFFFAFAFA))
            drawPath(path = path, color = color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2 * scaleX))
            
            // Perforation Line
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(55 * scaleX, 182.5f * scaleY),
                end = androidx.compose.ui.geometry.Offset(945 * scaleX, 182.5f * scaleY),
                strokeWidth = 1 * scaleX,
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(8 * scaleX, 8 * scaleX), 0f)
            )
        }
        
        // Content overlay
        Box(modifier = Modifier.fillMaxSize()) {
            // Status Badge
            Box(
                modifier = Modifier
                    .offset(x = 25.dp, y = 18.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(statusBg)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (statusText == "Upcoming") "Unreserved" else statusText, 
                    color = statusColor, 
                    fontSize = 14.sp, 
                    fontWeight = FontWeight.Bold,
                    fontFamily = AvenirFamily
                )
            }
            
            Text(
                text = "UTS: ${ticket.utsId}",
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 22.dp, end = 25.dp),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF666666),
                fontFamily = AvenirFamily
            )
            
            // Ticket Info
            Column(modifier = Modifier.padding(start = 25.dp, top = 65.dp)) {
                Text("Ticket Type", fontSize = 11.sp, color = Color.Gray, fontFamily = AvenirFamily)
                Text("JOURNEY", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = AvenirFamily)
            }
            
            Column(modifier = Modifier.align(Alignment.TopEnd).padding(top = 65.dp, end = 25.dp), horizontalAlignment = Alignment.End) {
                Text("Booking Date", fontSize = 11.sp, color = Color.Gray, fontFamily = AvenirFamily)
                Text(df.format(Date(ticket.bookedAt)), fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = AvenirFamily)
            }
            
            // Stations
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center).padding(horizontal = 25.dp).offset(y = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.source.substringBefore(" -").trim().uppercase(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = AvenirFamily,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "— ${ticket.distance} —",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = AvenirFamily,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(
                    text = ticket.destination.substringBefore(" -").trim().uppercase(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = AvenirFamily,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {}) {
                    Text("Book Again", color = Color(0xFF005AC1), fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = AvenirFamily)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFFE0E0E0)))
                TextButton(onClick = { onClick(ticket.ticketId) }) {
                    Text("View Details", color = Color(0xFF005AC1), fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = AvenirFamily)
                }
            }
        }
    }
}
