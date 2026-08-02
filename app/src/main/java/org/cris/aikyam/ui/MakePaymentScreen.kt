package org.cris.aikyam.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cris.aikyam.R

@Composable
fun MakePaymentScreen(
    source: String,
    dest: String,
    totalFare: String,
    onBack: () -> Unit
) {
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
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Make Payment",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 48.dp)
                )
            }
        },
        containerColor = Color(0xFFF8F9FF)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Summary Header
            val srcCode = source.split("-").lastOrNull()?.trim() ?: "SMVB"
            val destCode = dest.split("-").lastOrNull()?.trim() ?: "ERS"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = srcCode,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = destCode,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E),
                        fontSize = 14.sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Pay ₹ $totalFare",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Review ⌄",
                        color = Color(0xFF1A237E),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Options List
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE3F2FD))
            ) {
                Column {
                    // R-Wallet
                    PaymentOptionRow(
                        title = "R-Wallet",
                        subtitle = "₹ 0.60",
                        trailingContent = {
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Insufficient Balance", color = Color.Red, fontSize = 11.sp)
                                Text("+ Add Money", color = Color(0xFF005AC1), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        },
                        icon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_logo),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp).clip(CircleShape)
                            )
                        }
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

                    // UPI
                    PaymentOptionRow(
                        title = "UPI",
                        icon = {
                            Icon(Icons.Default.DoubleArrow, contentDescription = null, tint = Color.Green)
                        },
                        showChevron = true
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

                    // Other
                    PaymentOptionRow(
                        title = "Other Payment Methods",
                        icon = {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = Color.Gray)
                        },
                        showChevron = true
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentOptionRow(
    title: String,
    subtitle: String? = null,
    icon: @Composable () -> Unit,
    trailingContent: (@Composable () -> Unit)? = null,
    showChevron: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF8F9FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray,
                fontSize = 14.sp
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = Color(0xFF00695C),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        if (trailingContent != null) {
            trailingContent()
        }

        if (showChevron) {
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}
