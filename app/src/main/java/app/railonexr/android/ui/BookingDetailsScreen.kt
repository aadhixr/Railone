package app.railonexr.android.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.railonexr.android.R
import app.railonexr.android.logic.BookingManager
import app.railonexr.android.logic.Ticket
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BookingDetailsScreen(
    ticketId: String,
    onBack: () -> Unit
) {
    val ticket = remember(ticketId) { BookingManager.getTicketById(ticketId) }
    var timeLeft by remember { mutableLongStateOf(300L) } // 5 minutes

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        onBack() // Automatically close when timer hits 00:00
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
                Column(modifier = Modifier.padding(start = 48.dp)) {
                    Text("Booking Details", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Mobile: 7561801904", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Default.Share, "Share", tint = Color.White)
                }
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (ticket != null) {
                if (ticket.isExpired) {
                    ExpiredTicketDetails(ticket)
                } else {
                    Text(
                        text = "Thank You AADIL MUHAMMED, Happy Journey !",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    
                    TicketCardPakka(ticket, timeLeft)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F0))
            ) {
                Text(
                    text = "Note: This ticket is non refundable. Ticket is stored locally on the device. Please do not change your handset or perform factory reset.",
                    color = Color.Red,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF005AC1))
            ) {
                Text("Book Connecting Journey", color = Color(0xFF005AC1), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ExpiredTicketDetails(ticket: Ticket) {
    val df = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    val bookingDate = df.format(Date(ticket.bookedAt))
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("JOURNEY", fontSize = 12.sp, color = Color.Gray)
                    Text(ticket.ticketId, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(ticket.source.substringBefore(" -").trim(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(ticket.destination.substringBefore(" -").trim(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Via", fontSize = 11.sp, color = Color.Gray)
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Booked on", fontSize = 11.sp, color = Color.Gray)
                        Text(bookingDate, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
                Text("---", fontSize = 12.sp, color = Color.Black)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Ticket Expired", 
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color.Gray, 
            fontSize = 13.sp, 
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                "Passenger(s) : ${ticket.adults} Adult , ${ticket.children} Child",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${ticket.classType} | ORDINARY | JOURNEY | ₹${ticket.fare}.00",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun TicketCardPakka(ticket: Ticket, timeLeft: Long) {
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timerText = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds)
    
    val dfDisplay = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val bookingDateDisplay = dfDisplay.format(Date(ticket.bookedAt))
    
    val dfNumeric = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val bookingDateNumeric = dfNumeric.format(Date(ticket.bookedAt))
    
    val validTillDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(ticket.validTill))
    val validTillTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(ticket.validTill))

    val charcoal = Color(0xFF333333)
    val lightGray = Color(0xFFBDBDBD)
    val warmGold = Color(0xFFFFB300)
    val redOrange = Color(0xFFFF3D00)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(580.dp) 
    ) {
        // Full Ticket Template Background (Labels are built-in)
        Image(
            painter = painterResource(id = R.drawable.active_ticket_timer_template),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Section (Timer area)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .padding(top = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Dynamic preview will close in", 
                    color = Color.White, 
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = timerText, 
                    color = redOrange,
                    fontSize = 62.sp, 
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "Ticket Booking Date & Time", 
                    color = lightGray, 
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = bookingDateDisplay, 
                    color = warmGold, 
                    fontSize = 21.sp, 
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "R26728", 
                    color = lightGray, 
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Ticket is Non-Transferable", 
                    color = Color.White, 
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif
                )
            }

            // Bottom Section (Journey Details) - Strictly aligned with Image 1
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Journey Ticket", 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 15.sp, 
                        color = charcoal,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        text = ticket.ticketId, 
                        fontWeight = FontWeight.SemiBold, 
                        fontSize = 15.sp, 
                        color = charcoal,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = ticket.source.substringBefore(" -").trim().uppercase(), 
                        fontWeight = FontWeight.Black, 
                        fontSize = 15.sp, 
                        color = charcoal,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "— 628 km —", 
                        fontSize = 11.sp, 
                        color = Color.Gray, 
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text = ticket.destination.substringBefore(" -").trim().uppercase(), 
                        fontWeight = FontWeight.Black, 
                        fontSize = 15.sp, 
                        color = charcoal,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Via", fontSize = 11.sp, color = Color(0xFF9E9E9E), fontFamily = FontFamily.SansSerif)
                        Text("RHA", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = charcoal, fontFamily = FontFamily.SansSerif)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Passenger", fontSize = 11.sp, color = Color(0xFF9E9E9E), fontFamily = FontFamily.SansSerif)
                        Text("${ticket.adults} Adult, ${ticket.children} Child", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = charcoal, fontFamily = FontFamily.SansSerif)
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Booked on", fontSize = 11.sp, color = Color(0xFF9E9E9E), fontFamily = FontFamily.SansSerif)
                        Text(bookingDateNumeric, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = charcoal, fontFamily = FontFamily.SansSerif)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("*Valid Till", fontSize = 11.sp, color = Color(0xFF9E9E9E), fontFamily = FontFamily.SansSerif)
                        Text("$validTillDate $validTillTime", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = charcoal, fontFamily = FontFamily.SansSerif)
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = "${ticket.classType} | ORDINARY | RETURN | ₹${ticket.fare}.00",
                    fontWeight = FontWeight.Bold, 
                    fontSize = 14.sp,
                    color = charcoal,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = ticket.irNumber, 
                    fontWeight = FontWeight.SemiBold, 
                    fontSize = 14.sp, 
                    color = charcoal,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Valid for one ret. jrny. till midnight of $validTillDate", 
                    fontSize = 11.sp, 
                    color = Color.Gray,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}
