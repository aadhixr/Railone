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
import app.railonexr.android.logic.TicketStatus
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BookingDetailsScreen(
    ticketId: String,
    onBack: () -> Unit
) {
    val ticket = remember(ticketId) {
        BookingManager.getTicketById(ticketId)
    }

    var timeLeft by remember {
        mutableLongStateOf(300L)
    }

    // 5 minute timer
    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }

        // Automatically close when timer reaches 00:00
        onBack()
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF005AC1))
                    .statusBarsPadding()
                    .padding(
                        horizontal = 8.dp,
                        vertical = 12.dp
                    ),
                contentAlignment = Alignment.CenterStart
            ) {

                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 48.dp)
                ) {

                    Text(
                        text = "Booking Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        text = "Mobile: 7561801904",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier.align(
                        Alignment.CenterEnd
                    )
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }
            }
        },

        containerColor = Color(0xFFF5F5F5)

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                ),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (ticket != null) {

                if (
                    ticket.isExpired ||
                    ticket.status == TicketStatus.COMPLETED ||
                    ticket.status == TicketStatus.CANCELLED
                ) {

                    ExpiredTicketDetails(ticket)

                } else {

                    Text(
                        text = "Thank You AADIL MUHAMMED, Happy Journey !",

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                top = 12.dp,
                                bottom = 12.dp
                            ),

                        textAlign = TextAlign.Start,
                        fontFamily = RobotoFamily,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    TicketCardPakka(
                        ticket = ticket,
                        timeLeft = timeLeft
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // =====================================================
            // NON-REFUNDABLE NOTE
            // =====================================================

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 14.dp,
                        vertical = 2.dp
                    ),

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
                        .padding(
                            horizontal = 13.dp,
                            vertical = 6.dp
                        ),

                    color = Color(0xFFD32F2F),
                    fontFamily = RobotoFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp
                )
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            // =====================================================
            // BOOK CONNECTING JOURNEY
            // =====================================================

            OutlinedButton(

                onClick = onBack,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(40.dp),

                shape = RoundedCornerShape(24.dp),

                border = BorderStroke(
                    1.dp,
                    Color(0xFF005AC1)
                )

            ) {

                Text(
                    text = "Book Connecting Journey",
                    color = Color(0xFF005AC1),
                    fontWeight = FontWeight.Bold,
                    fontFamily = RobotoFamily,
                    fontSize = 12.sp
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}


// =============================================================
// EXPIRED / COMPLETED / CANCELLED TICKET
// CODE-GENERATED TICKET DESIGN
// =============================================================

@Composable
fun ExpiredTicketDetails(
    ticket: Ticket
) {

    // =========================================================
    // BOOKING DATE
    // =========================================================

    val df = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    )

    val bookingDate = df.format(
        Date(ticket.bookedAt)
    )


    // =========================================================
    // COLOURS
    // =========================================================

    val ticketBlue = Color(0xFFC7EAF0)
    val textBlack = Color(0xFF151515)
    val textGray = Color(0xFF777777)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // =====================================================
        // TICKET
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1080f / 315f)
        ) {

            // =================================================
            // TICKET SHAPE
            // =================================================

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                val width = size.width
                val height = size.height

                // ---------------------------------------------
                // CORNER SIZE
                // ---------------------------------------------

                val cornerRadius = 26.dp.toPx()

                // ---------------------------------------------
                // NOTCH SIZE
                // ---------------------------------------------

                val notchRadius = 25.dp.toPx()

                // ---------------------------------------------
                // NOTCH LEFT / RIGHT POSITION
                //
                // Increase 125 -> notch moves LEFT
                // Decrease 125 -> notch moves RIGHT
                // ---------------------------------------------

                val notchX =
                    width - 125.dp.toPx()


                // =================================================
                // PATH
                // =================================================

                val path =
                    androidx.compose.ui.graphics.Path().apply {

                        // -----------------------------------------
                        // TOP LEFT
                        // -----------------------------------------

                        moveTo(
                            cornerRadius,
                            0f
                        )

                        // -----------------------------------------
                        // TOP EDGE
                        // -----------------------------------------

                        lineTo(
                            notchX - notchRadius,
                            0f
                        )

                        // -----------------------------------------
                        // TOP NOTCH
                        // -----------------------------------------

                        cubicTo(

                            notchX - notchRadius,
                            notchRadius * 0.65f,

                            notchX + notchRadius,
                            notchRadius * 0.65f,

                            notchX + notchRadius,
                            0f
                        )

                        // -----------------------------------------
                        // TOP RIGHT EDGE
                        // -----------------------------------------

                        lineTo(
                            width - cornerRadius,
                            0f
                        )

                        // -----------------------------------------
                        // TOP RIGHT CORNER
                        // -----------------------------------------

                        quadraticBezierTo(
                            width,
                            0f,
                            width,
                            cornerRadius
                        )

                        // -----------------------------------------
                        // RIGHT SIDE
                        // -----------------------------------------

                        lineTo(
                            width,
                            height - cornerRadius
                        )

                        // -----------------------------------------
                        // BOTTOM RIGHT CORNER
                        // -----------------------------------------

                        quadraticBezierTo(
                            width,
                            height,
                            width - cornerRadius,
                            height
                        )

                        // -----------------------------------------
                        // BOTTOM EDGE
                        // -----------------------------------------

                        lineTo(
                            notchX + notchRadius,
                            height
                        )

                        // -----------------------------------------
                        // BOTTOM NOTCH
                        // -----------------------------------------

                        cubicTo(

                            notchX + notchRadius,
                            height - notchRadius * 0.65f,

                            notchX - notchRadius,
                            height - notchRadius * 0.65f,

                            notchX - notchRadius,
                            height
                        )

                        // -----------------------------------------
                        // BOTTOM LEFT EDGE
                        // -----------------------------------------

                        lineTo(
                            cornerRadius,
                            height
                        )

                        // -----------------------------------------
                        // BOTTOM LEFT CORNER
                        // -----------------------------------------

                        quadraticBezierTo(
                            0f,
                            height,
                            0f,
                            height - cornerRadius
                        )

                        // -----------------------------------------
                        // LEFT SIDE
                        // -----------------------------------------

                        lineTo(
                            0f,
                            cornerRadius
                        )

                        // -----------------------------------------
                        // TOP LEFT CORNER
                        // -----------------------------------------

                        quadraticBezierTo(
                            0f,
                            0f,
                            cornerRadius,
                            0f
                        )

                        close()
                    }


                // =================================================
                // DRAW TICKET
                // =================================================

                drawPath(
                    path = path,
                    color = ticketBlue
                )
            }


            // =====================================================
            // TICKET CONTENT
            // =====================================================

            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                // =================================================
                // JOURNEY
                // Current: line ~510
                // =================================================

                Text(
                    text = "JOURNEY",

                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(
                            start = 25.dp,
                            top = 24.dp
                        ),

                    fontFamily = AvenirFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = textBlack,
                    letterSpacing = 0.sp
                )


                // =================================================
                // UTS ID
                // Current: line ~531
                // =================================================

                Text(
                    text = ticket.ticketId,

                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(
                            end = 40.dp,
                            top = 26.dp
                        ),

                    fontFamily = AvenirFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = textBlack,
                    letterSpacing = 0.sp
                )


                // =================================================
                // SOURCE
                // Current: line ~552
                // =================================================

                Text(
                    text = ticket.source
                        .substringBefore(" -")
                        .trim()
                        .uppercase(),

                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(
                            start = 25.dp,
                            top = 50.dp
                        ),

                    fontFamily = AvenirFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = textBlack,
                    letterSpacing = 0.sp
                )


                // =================================================
                // DESTINATION
                // Current: line ~576
                // =================================================

                Text(
                    text = ticket.destination
                        .substringBefore(" -")
                        .trim()
                        .uppercase(),

                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(
                            end = 35.dp,
                            top = 50.dp
                        ),

                    fontFamily = AvenirFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = textBlack,
                    letterSpacing = 0.sp
                )


                // =================================================
                // VIA
                // Current: line ~599
                // =================================================

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(
                            start = 25.dp,
                            bottom = 15.dp
                        )
                ) {

                    Text(
                        text = "Via",

                        fontFamily = AvenirFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = textGray,
                        letterSpacing = 0.sp
                    )

                    Text(
                        text = ticket.via.ifBlank {
                            "---"
                        },

                        fontFamily = AvenirFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = textBlack,
                        letterSpacing = 0.sp
                    )
                }


                // =================================================
                // BOOKED ON
                // Current: line ~634
                // =================================================

                Column(

                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = 40.dp,
                            bottom = 12.dp
                        ),

                    horizontalAlignment =
                        Alignment.End

                ) {

                    Text(
                        text = "Booked on",

                        fontFamily = AvenirFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = textGray,
                        letterSpacing = 0.sp
                    )

                    Text(
                        text = bookingDate,

                        fontFamily = AvenirFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = textBlack,
                        letterSpacing = 0.sp
                    )
                }
            }
        }


        // =====================================================
        // SPACE BELOW TICKET
        // =====================================================

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // =====================================================
        // DETAILS BELOW TICKET
        // =====================================================

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),

            horizontalAlignment =
                Alignment.Start

        ) {

            Text(
                text = "Ticket Expired",

                fontFamily = AvenirFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text =
                    "Passenger(s) : ${ticket.adults} Adult , ${ticket.children} Child",

                fontFamily = AvenirFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text =
                    "${ticket.classType} | ORDINARY | JOURNEY | ₹${ticket.fare}.00",

                fontFamily = AvenirFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
// =============================================================
// ACTIVE TICKET WITH TIMER
// =============================================================

@Composable
fun TicketCardPakka(
    ticket: Ticket,
    timeLeft: Long
) {

    // =========================================================
    // TIMER
    // =========================================================

    val minutes =
        timeLeft / 60

    val seconds =
        timeLeft % 60

    val timerText =
        String.format(
            Locale.getDefault(),
            "%02d : %02d",
            minutes,
            seconds
        )


    // =========================================================
    // DATES
    // =========================================================

    val dfDisplay =
        SimpleDateFormat(
            "dd MMM yyyy, HH:mm",
            Locale.getDefault()
        )

    val bookingDateDisplay =
        dfDisplay.format(
            Date(ticket.bookedAt)
        )


    val dfNumeric =
        SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        )

    val bookingDateNumeric =
        dfNumeric.format(
            Date(ticket.bookedAt)
        )


    val validTillDate =
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        ).format(
            Date(ticket.validTill)
        )


    val validTillTime =
        SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(
            Date(ticket.validTill)
        )


    // =========================================================
    // COLOURS
    // =========================================================

    val charcoal =
        Color(0xFF333333)

    val lightGray =
        Color(0xFFBDBDBD)

    val warmGold =
        Color(0xFFFFB300)

    val redOrange =
        Color(0xFFFF3D00)


    // =========================================================
    // MAIN ACTIVE TICKET
    // =========================================================

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .height(580.dp)
    ) {

        // =====================================================
        // ACTIVE TICKET TEMPLATE
        // =====================================================

        Image(

            painter = painterResource(
                id = R.drawable.active_ticket_timer_template
            ),

            contentDescription = null,

            modifier = Modifier.fillMaxSize(),

            contentScale = ContentScale.FillBounds
        )


        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // =================================================
            // TOP TIMER SECTION
            // =================================================

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .offset(y = 10.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center

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


            // =================================================
            // JOURNEY DETAILS
            // =================================================

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 255.dp)
                    .padding(horizontal = 24.dp),

                horizontalArrangement =
                    Arrangement.SpaceBetween

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


            // =================================================
            // STATIONS
            // =================================================

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 290.dp)
                    .padding(horizontal = 24.dp),

                verticalAlignment =
                    Alignment.CenterVertically

            ) {

                Text(
                    text = ticket.source
                        .substringBefore(" -")
                        .trim()
                        .uppercase(),

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

                    modifier = Modifier.padding(
                        horizontal = 4.dp
                    )
                )

                Text(
                    text = ticket.destination
                        .substringBefore(" -")
                        .trim()
                        .uppercase(),

                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = charcoal,
                    fontFamily = RobotoFamily,

                    modifier = Modifier.weight(1f),

                    textAlign = TextAlign.End
                )
            }


            // =================================================
            // VIA & PASSENGER
            // =================================================

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 328.dp)
                    .padding(horizontal = 24.dp),

                horizontalArrangement =
                    Arrangement.SpaceBetween

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
                    horizontalAlignment =
                        Alignment.End
                ) {

                    Text(
                        text = "Passenger",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF9E9E9E),
                        fontFamily = RobotoFamily
                    )

                    Text(
                        text =
                            "${ticket.adults} Adult, ${ticket.children} Child",

                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = charcoal,
                        fontFamily = RobotoFamily,
                        letterSpacing = 0.sp
                    )
                }
            }


            // =================================================
            // BOOKED ON & VALIDITY
            // =================================================

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 388.dp)
                    .padding(horizontal = 24.dp),

                horizontalArrangement =
                    Arrangement.SpaceBetween

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
                    horizontalAlignment =
                        Alignment.End
                ) {

                    Text(
                        text = "Validity",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF9E9E9E),
                        fontFamily = RobotoFamily
                    )

                    Text(
                        text =
                            "$validTillDate $validTillTime",

                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = charcoal,
                        fontFamily = RobotoFamily
                    )
                }
            }


            // =================================================
            // FARE & IR NUMBER
            // =================================================

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 452.dp)
                    .padding(horizontal = 24.dp)

            ) {

                Text(
                    text =
                        "${ticket.classType} | ORDINARY | RETURN | ₹${ticket.fare}.00",

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


            // =================================================
            // FOOTER
            // =================================================

            Text(

                text =
                    "Valid for one ret. jrny. till midnight of $validTillDate",

                fontSize = 9.sp,
                color = Color.Gray,
                fontFamily = RobotoFamily,

                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 538.dp)
                    .padding(horizontal = 24.dp)
            )
        }
    }
}