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
import androidx.compose.ui.text.font.Font
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
            BookingStatusTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
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
fun BookingStatusTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("Upcoming", "Completed", "Cancelled", "All")
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF1F8FF), // Light blue background
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
                            painter = painterResource(
                                id = if (isSelected) R.drawable.ticket_icon_yellow else R.drawable.ticket_icon_grey
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFFFFA726) else Color(0xFF757575),
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
        
        Box(modifier = Modifier.fillMaxSize()) {
            // Date - Positioned top-left
            Text(
                text = df.format(Date(ticket.bookedAt)),
                color = Color(0xFFE8DFF8),
                fontFamily = AvenirFamily,
                fontWeight = FontWeight.Light,
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
                    fontFamily = AvenirFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = TicketUIConfig.fontSizeStations
                )

                Text(
                    text = ticket.destination.substringBefore(" -").trim().uppercase(),
                    color = Color(0xFFF2EDF8),
                    fontFamily = AvenirFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = TicketUIConfig.fontSizeStations
                )
            }
            
            // Badge - Positioned bottom-left
            Text(
                text = "Unreserved",
                color = Color(0xFFC6E0B4), // Green, Accent 6, Lighter 60%
                fontFamily = AvenirFamily,
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
                        fontFamily = AvenirFamily,
                        fontWeight = FontWeight.Light,
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
                        fontFamily = AvenirFamily,
                        fontWeight = FontWeight.Light,
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

@Composable
fun CompletedTicketCard(ticket: Ticket, onClick: (String) -> Unit) {
    val df = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(ticket.ticketId) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("Unreserved", color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                Text("--- ${ticket.distance} ---", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(1f))
                Text(ticket.destination.substringBefore(" -").trim(), fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
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
}

@Composable
fun RailOneBottomNavigation(selectedLabel: String, onNavClick: (Screen) -> Unit) {
    Surface(
        color = Color(0xFF005AC1), // Solid professional blue
        modifier = Modifier.fillMaxWidth().height(72.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navItems = listOf(
                Triple("Home", R.drawable.home, Screen.Home),
                Triple("My Bookings", R.drawable.bookings, Screen.MyBookings(3)),
                Triple("You", R.drawable.you, Screen.Home),
                Triple("Menu", R.drawable.menu, Screen.Home)
            )

            navItems.forEach { (label, iconRes, screen) ->
                val isSelected = label == selectedLabel
                
                // --- FULL CONTROL PANEL FOR EACH ITEM ---
                data class ItemConfig(
                    val iW: androidx.compose.ui.unit.Dp, val iH: androidx.compose.ui.unit.Dp, 
                    val iX: androidx.compose.ui.unit.Dp, val iY: androidx.compose.ui.unit.Dp, 
                    val itX: androidx.compose.ui.unit.Dp, val itY: androidx.compose.ui.unit.Dp, 
                    val space: androidx.compose.ui.unit.Dp, val lSize: androidx.compose.ui.unit.TextUnit
                )

                val cfg = when(label) {
                    "Home" ->        ItemConfig(26.dp, 26.dp, 0.dp, 0.dp, 0.dp, 0.dp, 4.dp, 10.sp)
                    "My Bookings" -> ItemConfig(26.dp, 26.dp, 0.dp, 0.dp, 0.dp, 0.dp, 4.dp, 10.sp)
                    "You" ->         ItemConfig(26.dp, 26.dp, 0.dp, 0.dp, 0.dp, 0.dp, 4.dp, 10.sp)
                    "Menu" ->        ItemConfig(26.dp, 26.dp, 0.dp, 0.dp, 0.dp, 0.dp, 4.dp, 10.sp)
                    else ->          ItemConfig(26.dp, 26.dp, 0.dp, 0.dp, 0.dp, 0.dp, 4.dp, 10.sp)
                }
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .offset(x = cfg.itX, y = cfg.itY)
                        .clickable(
                            indication = null,
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                        ) {
                            onNavClick(screen)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = iconRes), 
                        contentDescription = label,
                        modifier = Modifier
                            .size(width = cfg.iW, height = cfg.iH)
                            .offset(x = cfg.iX, y = cfg.iY)
                    ) 
                    Spacer(modifier = Modifier.height(cfg.space))
                    Text(
                        text = label, 
                        color = Color.White,
                        fontSize = cfg.lSize,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    ) 
                }
            }
        }
    }
}
