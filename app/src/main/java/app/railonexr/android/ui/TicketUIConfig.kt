package app.railonexr.android.ui

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Control Panel for Ticket UI Alignment and Sizing.
 * Change these values to adjust the "Upcoming Journey" card across the whole app.
 */
object TicketUIConfig {
    // --- Vertical Placement ---
    
    // Controls how low the station names are. 
    // Increase this (e.g., 0.50f) to push names DOWN.
    const val stationVerticalWeight = 0.45f
    
    // Adjusts the bottom vertical alignment of the buttons row.
    // Decrease this (e.g., 2.dp) to push buttons DOWN.
    val bottomRowPadding = 5.dp
    
    // Sets the height of the button hit areas.
    val buttonHeight = 38.dp

    // --- Text Sizes (sp) ---
    val fontSizeDate = 14.sp
    val fontSizeStations = 18.sp
    val fontSizeBadge = 18.sp
    val fontSizeButtons = 14.sp

    // --- Button Width Ratios ---
    const val weightBookAgain = 1.05f
    const val weightViewDetails = 0.85f
}
