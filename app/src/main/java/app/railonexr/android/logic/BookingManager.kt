package app.railonexr.android.logic

import androidx.compose.runtime.mutableStateListOf
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

enum class TicketStatus {
    UPCOMING, COMPLETED, CANCELLED
}

data class Ticket(
    val utsId: String,
    val ticketId: String,
    val bookedAt: Long,
    val validTill: Long,
    val source: String,
    val destination: String,
    val fare: String,
    val adults: Int,
    val children: Int,
    val trainType: String,
    val classType: String,
    val irNumber: String,
    var status: TicketStatus = TicketStatus.UPCOMING
) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() > validTill
}

object BookingManager {
    private val _bookings = mutableStateListOf<Ticket>()
    val bookings: List<Ticket> get() = _bookings

    fun bookTicket(
        source: String,
        dest: String,
        fare: String,
        adults: Int,
        children: Int,
        trainType: String,
        classType: String
    ): Ticket {
        val now = System.currentTimeMillis()
        
        // Validity is until midnight of the next day
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        
        val newTicket = Ticket(
            utsId = generateRandomAlphanumeric(10),
            ticketId = generateRandomAlphanumeric(10),
            bookedAt = now,
            validTill = calendar.timeInMillis,
            source = source,
            destination = dest,
            fare = fare,
            adults = adults,
            children = children,
            trainType = trainType,
            classType = classType,
            irNumber = generateIRNumber()
        )
        _bookings.add(0, newTicket)
        return newTicket
    }

    private fun generateRandomAlphanumeric(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }

    private fun generateIRNumber(): String {
        // Format like IR:19AAAGM0289C1ZG
        return "IR:${generateRandomAlphanumeric(13)}"
    }

    fun getUpcomingTickets(): List<Ticket> {
        return _bookings.filter { !it.isExpired && it.status == TicketStatus.UPCOMING }
    }

    fun getCompletedTickets(): List<Ticket> {
        return _bookings.filter { it.isExpired || it.status == TicketStatus.COMPLETED }
    }

    fun getCancelledTickets(): List<Ticket> {
        return _bookings.filter { it.status == TicketStatus.CANCELLED }
    }

    fun getTicketById(id: String): Ticket? {
        return _bookings.find { it.ticketId == id }
    }
}
