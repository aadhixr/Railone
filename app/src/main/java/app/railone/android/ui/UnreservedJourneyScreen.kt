package app.railone.android.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.railone.android.R

@Composable
fun UnreservedJourneyScreen(
    sourceStation: String,
    destinationStation: String,
    onBack: () -> Unit,
    onBookNow: (String) -> Unit
) {
    var adultCount by remember { mutableIntStateOf(1) }
    var childCount by remember { mutableIntStateOf(0) }
    var selectedTrainType by remember { mutableIntStateOf(0) }

    val baseFare = remember(sourceStation, destinationStation) {
        when {
            (sourceStation.contains("SMVB") || sourceStation.contains("SMVT")) &&
            (destinationStation.contains("ERS") || destinationStation.contains("ERNAKULAM")) -> 215

            (sourceStation.contains("ERS") || sourceStation.contains("ERNAKULAM")) &&
            (destinationStation.contains("SMVB") || destinationStation.contains("SMVT")) -> 210

            else -> 210 // Default
        }
    }

    val totalFare by remember(baseFare, adultCount, childCount) {
        derivedStateOf {
            (baseFare * (adultCount + childCount)).toString()
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
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Unreserved Journey",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "E-Ticket",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Station Summary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F9FF))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        val src = if (sourceStation.isEmpty()) "ERNAKULAM JN. ERS" else sourceStation.replace(" - ", " ")
                        Text(
                            text = if (src.contains(" ")) src.substringBeforeLast(" ") else src,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E),
                            fontSize = 14.sp
                        )
                        Text(
                            text = if (src.contains(" ")) src.substringAfterLast(" ") else "SRC",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        val dest = if (destinationStation.isEmpty()) "SMVT BENGALURU SMVB" else destinationStation.replace(" - ", " ")
                        Text(
                            text = if (dest.contains(" ")) dest.substringBeforeLast(" ") else dest,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E),
                            fontSize = 14.sp
                        )
                        Text(
                            text = if (dest.contains(" ")) dest.substringAfterLast(" ") else "DEST",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Train Type
                SectionHeader("Train Type")
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SelectableChip(
                        text = "MAIL/EXP",
                        selected = selectedTrainType == 0,
                        onClick = { selectedTrainType = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    SelectableChip(
                        text = "SUPERFAST",
                        selected = selectedTrainType == 1,
                        onClick = { selectedTrainType = 1 },
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("OTHERS", fontSize = 12.sp, color = Color.Gray)
                        Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF005AC1))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Ticket Type
                SectionHeader("Ticket Type")
                Spacer(modifier = Modifier.height(12.dp))
                SelectableChip("JOURNEY", true, {}, Modifier.width(100.dp))

                Spacer(modifier = Modifier.height(20.dp))

                // Passenger Selection
                PassengerCounter("Adult", adultCount) { adultCount = (adultCount + it).coerceAtLeast(1) }
                Spacer(modifier = Modifier.height(12.dp))
                PassengerCounter("Child", childCount) { childCount = (childCount + it).coerceAtLeast(0) }
                
                Text(
                    text = "Aged between 5 and 12 years on the day of Travel",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Class
                SectionHeader("Class")
                Spacer(modifier = Modifier.height(12.dp))
                SelectableChip("SECOND", true, {}, Modifier.width(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Avail Concession", fontSize = 14.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Fare Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F9FF))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ConfirmationNumber,
                            contentDescription = null,
                            tint = Color(0xFF005AC1),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Fare",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E),
                            fontSize = 18.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹ $totalFare",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Fare Breakup", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onBookNow(totalFare) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005AC1))
            ) {
                Text("Book Now", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1A237E),
        fontSize = 14.sp
    )
}

@Composable
fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0xFF005AC1) else Color.White)
            .border(1.dp, if (selected) Color(0xFF005AC1) else Color(0xFFE0E0E0), RoundedCornerShape(20.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PassengerCounter(label: String, count: Int, onCountChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, color = Color.Gray)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onCountChange(-1) }) {
                Icon(Icons.Default.Remove, null, tint = Color(0xFF005AC1))
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF005AC1)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = count.toString(), color = Color.White, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = { onCountChange(1) }) {
                Icon(Icons.Default.Add, null, tint = Color(0xFF005AC1))
            }
        }
    }
}
