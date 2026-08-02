package app.railonexr.android.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.railonexr.android.R

@Composable
fun MakePaymentScreen(
    source: String,
    dest: String,
    totalFare: String,
    adultCount: Int,
    childCount: Int,
    trainType: String,
    classType: String,
    onBack: () -> Unit
) {
    var showReview by remember { mutableStateOf(false) }

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
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { showReview = true }
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
                                painter = painterResource(id = R.drawable.ic_indian_railway),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

                    // UPI
                    PaymentOptionRow(
                        title = "",
                        icon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_upi_symbol),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_upi_name),
                                    contentDescription = null,
                                    modifier = Modifier.height(16.dp)
                                )
                            }
                        },
                        showChevron = true
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)

                    // Other
                    PaymentOptionRow(
                        title = "Other Payment Methods",
                        icon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_card_payment),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        showChevron = true
                    )
                }
            }
        }
    }

    if (showReview) {
        ReviewPopup(
            source = source,
            dest = dest,
            totalFare = totalFare,
            adultCount = adultCount,
            childCount = childCount,
            trainType = trainType,
            classType = classType,
            onDismiss = { showReview = false }
        )
    }
}

@Composable
fun ReviewPopup(
    source: String,
    dest: String,
    totalFare: String,
    adultCount: Int,
    childCount: Int,
    trainType: String,
    classType: String,
    onDismiss: () -> Unit
) {
    val srcCode = source.split("-").lastOrNull()?.trim() ?: "SMVB"
    val destCode = dest.split("-").lastOrNull()?.trim() ?: "ERS"
    val srcName = source.substringBefore("-").trim().ifEmpty { "SMVT BENGALURU" }
    val destName = dest.substringBefore("-").trim().ifEmpty { "ERNAKULAM JN." }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .clickable(enabled = false) {}
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(srcCode, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                HorizontalDivider(modifier = Modifier.width(30.dp), thickness = 1.dp, color = Color.LightGray)
                                Text("  628 km  ", fontSize = 11.sp, color = Color.Gray)
                                HorizontalDivider(modifier = Modifier.width(30.dp), thickness = 1.dp, color = Color.LightGray)
                            }
                            Text(destCode, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(srcName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(destName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Via: BWT-SA-CBE", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$adultCount Adult, $childCount Child | $classType | $trainType",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text("JOURNEY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                        Spacer(modifier = Modifier.height(16.dp))
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
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Fare Breakup:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Ticket Fare", fontSize = 13.sp, color = Color.Black)
                            Text("₹ $totalFare.0", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("GST", fontSize = 13.sp, color = Color.Black)
                            Text("₹ 0.0", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
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
        if (title.isNotEmpty()) {
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
        } else {
            Box(modifier = Modifier.weight(1f)) {
                icon()
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
