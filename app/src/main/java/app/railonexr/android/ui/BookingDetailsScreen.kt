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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    var timeLeft by remember { mutableLongStateOf(300L) } // 5 minutes in seconds

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
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
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Thank You AADIL MUHAMMED, Happy Journey !",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (ticket != null) {
                TicketCard(ticket, timeLeft)
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
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
fun TicketCard(ticket: Ticket, timeLeft: Long) {
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timerText = String.format("%02d : %02d", minutes, seconds)
    
    val df = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val bookingDate = df.format(Date(ticket.bookedAt))
    val validTillDf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val validTill = validTillDf.format(Date(ticket.validTill))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Dynamic Preview Area (Top part with timer)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFF212121)),
                contentAlignment = Alignment.Center
            ) {
                // Background pattern simulation (Simple)
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Could draw a grid pattern here
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Dynamic preview will close in", color = Color.White, fontSize = 14.sp)
                    Text(timerText, color = Color.Red, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                    Text("Ticket Booking Date & Time", color = Color.Gray, fontSize = 12.sp)
                    Text(bookingDate, color = Color(0xFFFFB300), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("R26728", color = Color.Gray, fontSize = 12.sp)
                    Text("Ticket is Non-Transferable", color = Color.White, fontSize = 12.sp)
                }
                
                // Vertical "INDIAN RAILWAYS" text (simulated)
                Text(
                    "INDIAN RAILWAYS", 
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 4.dp).graphicsLayer(rotationZ = -90f),
                    color = Color.DarkGray, fontSize = 10.sp
                )
                Text(
                    "भारतीय रेल", 
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 4.dp).graphicsLayer(rotationZ = 90f),
                    color = Color.DarkGray, fontSize = 10.sp
                )
            }

            // Ticket Details Area
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Journey Ticket", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(ticket.ticketId, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(ticket.source.substringBefore(" -").trim(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(modifier = Modifier.width(30.dp), thickness = 1.dp, color = Color.Gray)
                    Text(" 628 km ", fontSize = 11.sp, color = Color.Gray)
                    HorizontalDivider(modifier = Modifier.width(30.dp), thickness = 1.dp, color = Color.Gray)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(ticket.destination.substringBefore(" -").trim(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Via", fontSize = 11.sp, color = Color.Gray)
                        Text("RHA", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Passenger", fontSize = 11.sp, color = Color.Gray)
                        Text("${ticket.adults} Adult, ${ticket.children} Child", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Booked on", fontSize = 11.sp, color = Color.Gray)
                        Text(SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(ticket.bookedAt)), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("*Valid Till", fontSize = 11.sp, color = Color.Gray)
                        Text(validTill, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${ticket.classType} | ORDINARY | RETURN | ₹${ticket.fare}",
                    fontWeight = FontWeight.Bold, fontSize = 13.sp
                )
                Text(ticket.irNumber, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Valid for one ret. jrny. till midnight of 09/02/2026", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}
