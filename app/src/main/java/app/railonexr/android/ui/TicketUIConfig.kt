package app.railonexr.android.ui

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Control Panel for Ticket UI Alignment and Sizing.
 * Change these values to manually position text on the "Upcoming Journey" card.
 */
object TicketUIConfig {
    // --- Date Placement ---
    val dateOffsetTop = 35.dp
    val dateOffsetStart = 18.dp

    // --- Stations Placement ---
    // Controls the vertical height of the station names line.
    val stationsOffsetTop = 76.dp
    val stationsPaddingHorizontal = 18.dp

    // --- "Unreserved" Badge Placement ---
    val badgeOffsetBottom = 35.dp
    val badgeOffsetStart = 18.dp

    // --- Buttons Placement ---
    val buttonsOffsetBottom = 30.dp
    val buttonsOffsetEnd = 34.dp
    val buttonGap = 8.dp
    
    // Height of the clickable hit areas for the buttons.
    val buttonHeight = 38.dp

    // Fine-tune the position of the text labels INSIDE the buttons.
    val buttonLabelOffsetY = 0.dp // Move text UP (-) or DOWN (+)
    val buttonLabelOffsetX = 10.dp // Move text LEFT (-) or RIGHT (+)

    // --- Text Sizes (sp) ---
    val fontSizeDate = 14.sp
    val fontSizeStations = 16.sp
    val fontSizeBadge = 16.sp
    val fontSizeButtons = 14.sp

    // --- Button Width Ratios (Advanced) ---
    const val weightBookAgain = 1.05f
    const val weightViewDetails = 0.85f
}
