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
import androidx.compose.ui.text.font.Font
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

val RobotoFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
                        textAlign = TextAlign.Start,
                        fontFamily = RobotoFamily,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    
                    TicketCardPakka(ticket, timeLeft)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 2.dp),   // was 5.dp
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF1F1)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Text(
                    text = "Note: This ticket is non refundable. Ticket is stored locally on the device. Please do not change your handset or perform factory reset.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 13.dp, vertical = 6.dp),   // was 12.dp
                    color = Color(0xFFD32F2F),
                    fontFamily = RobotoFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))   // was 16.dp

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF005AC1))
            ) {
                Text(
                    "Book Connecting Journey",
                    color = Color(0xFF005AC1),
                    fontWeight = FontWeight.Bold,
                    fontFamily = RobotoFamily,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))   // was 32.dp
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
            .padding(horizontal = 2.dp)
            .height(580.dp) 
    ) {
        // Full Ticket Template Background
        Image(
            painter = painterResource(id = R.drawable.active_ticket_timer_template),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        
        Box(modifier = Modifier.fillMaxSize()) {
            // Top Section (Timer area) - Precisely positioned
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .offset(y = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Dynamic preview will close in",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = RobotoFamily
                )
                Text(
                    text = timerText,
                    color = redOrange,
                    fontSize = 62.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = RobotoFamily
                )
                Text(
                    text = "Ticket Booking Date & Time",
                    color = lightGray,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = RobotoFamily
                )
                Text(
                    text = bookingDateDisplay,
                    color = warmGold,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = RobotoFamily
                )
                Text(
                    text = ticket.referenceNumber,
                    color = lightGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = RobotoFamily
                )
                Text(
                    text = "Ticket is Non-Transferable",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = RobotoFamily
                )
            }

            // ==========================
            // JOURNEY DETAILS - PRECISION ALIGNMENT
            // ==========================

            // Journey Ticket & ID row - Moved down to clear black area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 260.dp)
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Journey Ticket",
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = charcoal,
                    fontFamily = RobotoFamily
                )
                Text(
                    text = ticket.ticketId,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = charcoal,
                    fontFamily = RobotoFamily
                )
            }

            // Stations Row - Aligned with middle horizontal space
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 295.dp)
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.source.substringBefore(" -").trim().uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = charcoal,
                    fontFamily = RobotoFamily,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "— ${ticket.distance} —",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    fontFamily = RobotoFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(
                    text = ticket.destination.substringBefore(" -").trim().uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = charcoal,
                    fontFamily = RobotoFamily,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            // Via & Passenger row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 329.dp)
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Via",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF9E9E9E),
                        fontFamily = RobotoFamily
                    )

                    Text(
                        text = ticket.via,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = charcoal,
                        fontFamily = RobotoFamily,
                        letterSpacing = 0.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Passenger",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF9E9E9E),
                        fontFamily = RobotoFamily
                    )

                    Text(
                        text = "${ticket.adults} Adult, ${ticket.children} Child",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = charcoal,
                        fontFamily = RobotoFamily,
                        letterSpacing = 0.sp
                    )
                }
            }

            // Booked on & Validity row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 385.dp)
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Booked on",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF9E9E9E),
                        fontFamily = RobotoFamily
                    )
                    Text(
                        text = bookingDateNumeric,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = charcoal,
                        fontFamily = RobotoFamily
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Validity",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF9E9E9E),
                        fontFamily = RobotoFamily
                    )
                    Text(
                        text = "$validTillDate $validTillTime",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = charcoal,
                        fontFamily = RobotoFamily
                    )
                }
            }

            // Fare & IR Number Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 450.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "${ticket.classType} | ORDINARY | RETURN | ₹${ticket.fare}.00",
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = charcoal,
                    fontFamily = RobotoFamily
                )
                Text(
                    text = ticket.irNumber,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = charcoal,
                    fontFamily = RobotoFamily
                )
            }


            // Footer Text
            Text(
                text = "Valid for one ret. jrny. till midnight of $validTillDate",
                fontSize = 9.sp,
                color = Color.Gray,
                fontFamily = RobotoFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 535.dp)
                    .padding(horizontal = 24.dp)
            )
        }
    }
}
