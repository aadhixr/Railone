package org.cris.aikyam.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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

@Composable
fun SearchStationScreen(
    isSource: Boolean,
    onClose: () -> Unit,
    onStationSelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val stations = listOf(
        StationInfo("SMVT BENGALURU - SMVB", "KARNATAKA"),
        StationInfo("ERNAKULAM JN. - ERS", "KOCHI / ERNAKULAM, KERALA"),
        StationInfo("CHANGANASERI - CGY", "KERALA"),
        StationInfo("KSR BENGALURU - SBC", "KARNATAKA"),
        StationInfo("KAYANKULAM - KYJ", "KERALA"),
        StationInfo("BHOPAL JN. - BPL", "BHOPAL, MADHYA PRADESH"),
        StationInfo("ERNAKULAM TOWN - ERN", "KOCHI / ERNAKULAM, KERALA")
    )

    val filteredStations by remember(searchQuery) {
        derivedStateOf {
            if (searchQuery.isBlank()) {
                stations
            } else {
                stations.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                    it.location.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, Color(0xFFE3F2FD), CircleShape)
                        .align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF005AC1),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "Search Station",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
            }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (isSource) "Source" else "Destination",
                color = Color(0xFF1A237E),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("Select Station", color = Color.LightGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                trailingIcon = { Icon(Icons.Default.Mic, contentDescription = null, tint = Color.Gray) },
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4DB6AC),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = Color(0xFF1A237E),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recent Station Searches",
                    color = Color(0xFF1A237E),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(filteredStations) { station ->
                    StationItem(station) {
                        onStationSelected(station.name)
                    }
                }
            }
        }
    }
}

data class StationInfo(val name: String, val location: String)

@Composable
fun StationItem(station: StationInfo, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = station.name,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                fontSize = 14.sp
            )
            Text(
                text = station.location,
                color = Color.LightGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Icon(
            imageVector = Icons.Default.NorthEast,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(16.dp)
        )
    }
}
