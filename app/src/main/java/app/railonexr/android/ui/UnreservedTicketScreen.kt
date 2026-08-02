package app.railonexr.android.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.railonexr.android.ui.theme.RailOneTheme

@Composable
fun UnreservedTicketScreen(
    sourceStation: String,
    destinationStation: String,
    onClose: () -> Unit,
    onFromClick: () -> Unit,
    onToClick: () -> Unit,
    onProceedToBook: () -> Unit,
    onSwap: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedStationType by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Unreserved E-Ticket",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(32.dp)
                        .border(1.dp, Color(0xFFE3F2FD), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF005AC1),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
        ) {
            item {
                // Normal / Season Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(4.dp)
                ) {
                    TabButton(
                        modifier = Modifier.weight(1f),
                        text = "Normal",
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    TabButton(
                        modifier = Modifier.weight(1f),
                        text = "Season",
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                // Outside / At Station Toggles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ToggleButton(
                        modifier = Modifier.weight(1f),
                        text = "Outside Station",
                        selected = selectedStationType == 0,
                        onClick = { selectedStationType = 0 },
                        icon = Icons.Outlined.Info
                    )
                    ToggleButton(
                        modifier = Modifier.weight(1f),
                        text = "At Station",
                        selected = selectedStationType == 1,
                        onClick = { selectedStationType = 1 },
                        icon = Icons.Outlined.Info
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                // Station Selection
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            StationField(
                                label = "From",
                                value = sourceStation.ifEmpty { "Source" },
                                onClick = onFromClick
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            StationField(
                                label = "To",
                                value = destinationStation.ifEmpty { "Destination" },
                                onClick = onToClick
                            )
                        }
                        
                        // Swap Button
                        IconButton(
                            onClick = onSwap,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 4.dp)
                                .offset(y = (-4).dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE3F2FD))
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap",
                                tint = Color(0xFF005AC1),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }

            item {
                // Action Buttons
                Button(
                    onClick = onProceedToBook,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF005AC1),
                        contentColor = Color.White
                    )
                ) {
                    Text("Proceed To Book", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(27.dp),
                    border = BorderStroke(1.dp, Color(0xFF005AC1))
                ) {
                    Text("Check Upcoming Trains", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF005AC1))
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }

            item {
                // Recent Searches
                Text(
                    text = "Recent Searches",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E),
                    fontSize = 16.sp
                )
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
            
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    item { RecentSearchCard("ERNAKULAM JN., ERS", "SMVT BENGALURU, SMVB") }
                    item { RecentSearchCard("CHANGANASERI, CGY", "ERNAKULAM JN., ERS") }
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnreservedTicketPreview() {
    RailOneTheme {
        UnreservedTicketScreen(
            sourceStation = "",
            destinationStation = "",
            onClose = {},
            onFromClick = {},
            onToClick = {},
            onProceedToBook = {}
        )
    }
}

@Composable
fun TabButton(modifier: Modifier, text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Color.White else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color(0xFF005AC1) else Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ToggleButton(
    modifier: Modifier, 
    text: String, 
    selected: Boolean, 
    onClick: () -> Unit,
    icon: ImageVector
) {
    Row(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (selected) Color(0xFF005AC1) else Color.White)
            .border(1.dp, if (selected) Color(0xFF005AC1) else Color(0xFFE0E0E0), RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Gray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) Color.White else Color.Gray,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun StationField(label: String, value: String, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Text(text = label, color = Color(0xFF03A9F4), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Train,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = value,
                color = if (value == "Source" || value == "Destination") Color.LightGray else Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 1.dp)
    }
}

@Composable
fun RecentSearchCard(from: String, to: String) {
    Card(
        modifier = Modifier.width(220.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = from, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
            Icon(
                imageVector = Icons.Default.Route, 
                contentDescription = null, 
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF005AC1)
            )
            Text(text = to, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
        }
    }
}
