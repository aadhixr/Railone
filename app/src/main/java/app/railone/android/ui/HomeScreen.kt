package app.railone.android.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.railone.android.ui.theme.RailOneTheme
import app.railone.android.R
import app.railone.android.logic.UpdateManager
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    RailOneTheme {
        HomeScreen()
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onUnreservedClick: () -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        UpdateManager.checkForUpdates()
    }

    Scaffold(
        topBar = { 
            RailOneTopBar(
                onRefreshClick = {
                    scope.launch {
                        UpdateManager.checkForUpdates()
                    }
                }
            ) 
        },
        bottomBar = { RailOneBottomNavigation() },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            // Update Banner
            UpdateManager.updateAvailable?.let { release ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE91E63))
                        .clickable { uriHandler.openUri(release.htmlUrl) }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "New Update Available (${release.tagName})! Tap to download.",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                GreetingSection("Aadil Muhammed")
                Spacer(modifier = Modifier.height(24.dp))
                JourneyPlannerSection(onUnreservedClick = onUnreservedClick)
                Spacer(modifier = Modifier.height(24.dp))
                OfferingsSection()
                Spacer(modifier = Modifier.height(24.dp))
                TriviaSection()
                Spacer(modifier = Modifier.height(24.dp))
                SocialMediaSection()
            }
        }
    }
}

@Composable
fun RailOneTopBar(onRefreshClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Language Toggle (Left)
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFE3F2FD)) // Light blue background
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Text("अA", color = Color(0xFF005AC1), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        // RailOne Logo (Center)
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "RailOne Logo",
            modifier = Modifier.height(32.dp),
            contentScale = ContentScale.Fit
        )

        // Icons Area (Right)
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Refresh Icon for Updates
            IconButton(
                onClick = onRefreshClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
                    .border(1.dp, Color(0xFFE0E0E0), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Check for Updates",
                    modifier = Modifier.size(22.dp),
                    tint = Color(0xFF005AC1)
                )
            }

            // Notification Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
                    .border(1.dp, Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .align(Alignment.TopEnd)
                        .offset(x = 2.dp, y = (-2).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("15", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun GreetingSection(name: String) {
    Text(
        text = "Hi, $name!",
        color = Color(0xFF1A237E),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
}

@Composable
fun JourneyPlannerSection(onUnreservedClick: () -> Unit) {
    Column {
        Text(
            text = "Journey Planner",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            JourneyCard(Modifier.weight(1f), "Reserved", Color(0xFFE3F2FD), imageRes = R.drawable.reserved)
            JourneyCard(Modifier.weight(1f), "Unreserved", Color(0xFFF3E5F5), imageRes = R.drawable.unreserved, onClick = onUnreservedClick)
            JourneyCard(Modifier.weight(1f), "Platform", Color(0xFFFFF3E0), imageRes = R.drawable.platform)
        }
    }
}

@Composable
fun JourneyCard(
    modifier: Modifier, 
    title: String, 
    bgColor: Color,
    imageRes: Int,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(16.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            // Using Image instead of Icon for illustrations
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
            fontSize = 13.sp
        )
    }
}

@Composable
fun OfferingsSection() {
    Column {
        Text(
            text = "More Offerings",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
        Spacer(modifier = Modifier.height(20.dp))
        
        val items = listOf(
            OfferingItem("Search Trains", Icons.Outlined.Route, Color(0xFFFFE4E8)),
            OfferingItem("PNR Status", Icons.Outlined.ConfirmationNumber, Color(0xFFE8F5E9)),
            OfferingItem("Coach Position", Icons.Outlined.Train, Color(0xFFE3F2FD)),
            OfferingItem("Track Your Train", Icons.Outlined.MyLocation, Color(0xFFFFF3E0)),
            OfferingItem("Order Food", Icons.Outlined.Restaurant, Color(0xFFE8EAF6)),
            OfferingItem("File Refund", Icons.AutoMirrored.Outlined.AssignmentReturn, Color(0xFFEEEEEE)),
            OfferingItem("Rail Madad", Icons.Outlined.Handshake, Color(0xFFFBE9E7)),
            OfferingItem("Go To WAVES", Icons.Outlined.Layers, Color(0xFFF3E5F5))
        )

        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            items.chunked(4).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { item ->
                        OfferingCard(item, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

data class OfferingItem(val title: String, val icon: ImageVector, val color: Color)

@Composable
fun OfferingCard(item: OfferingItem, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(item.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = if (item.title == "Search Trains") Color(0xFFE91E63) else Color(0xFF5C6BC0),
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.title,
            fontSize = 11.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
            lineHeight = 14.sp
        )
    }
}

@Composable
fun TriviaSection() {
    Column {
        Text(
            text = "Do You know?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            item { 
                TriviaCard(
                    "First ever passenger train was run between Bori Bandar to Thane on April 16, 1853.",
                    ""
                ) 
            }
            item { 
                TriviaCard(
                    "Chenab Railway Bridge in Dharot, Jammu & Kashmir is the World's highest Railway Bridge.",
                    ""
                ) 
            }
            item { 
                TriviaCard(
                    "Noney Bridge is the tallest railway bridge in India with 141 meters.",
                    ""
                ) 
            }
        }
    }
}

@Composable
fun TriviaCard(title: String, subtitle: String) {
    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title, 
                fontSize = 12.sp, 
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A237E),
                lineHeight = 16.sp
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle, 
                    fontSize = 12.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = Color(0xFF005AC1)
                )
            }
        }
    }
}

@Composable
fun SocialMediaSection() {
    Column {
        Text(
            text = "Follow Us On Social Media Platforms",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        // Banner Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            Text("Social Media Banner", color = Color(0xFF1A237E))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SocialIcon(Icons.Default.Facebook, Color(0xFF1877F2))
            SocialIcon(Icons.Default.Share, Color(0xFF000000)) // Fallback for X
            SocialIcon(Icons.Default.CameraAlt, Color(0xFFE4405F)) // Fallback for Instagram
            SocialIcon(Icons.Default.PlayCircle, Color(0xFFFF0000))
        }
    }
}

@Composable
fun SocialIcon(icon: ImageVector, color: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun RailOneBottomNavigation() {
    NavigationBar(
        containerColor = Color(0xFF005AC1),
    ) {
        val navItems = listOf(
            Triple("Home", Icons.Default.Home, true),
            Triple("My Bookings", Icons.Default.Book, false),
            Triple("You", Icons.Default.Person, false),
            Triple("Menu", Icons.Default.Menu, false)
        )

        navItems.forEach { (label, icon, selected) ->
            NavigationBarItem(
                selected = selected,
                onClick = {},
                icon = { 
                    Icon(
                        imageVector = icon, 
                        contentDescription = label,
                        tint = if (selected) Color.White else Color.White.copy(alpha = 0.7f)
                    ) 
                },
                label = { 
                    Text(
                        text = label, 
                        color = if (selected) Color.White else Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                    indicatorColor = Color.White.copy(alpha = 0.2f)
                )
            )
        }
    }
}
