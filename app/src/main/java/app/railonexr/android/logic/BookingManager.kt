package app.railonexr.android.logic

import androidx.compose.runtime.mutableStateListOf
import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.random.Random

@Serializable
enum class TicketStatus {
    UPCOMING, COMPLETED, CANCELLED
}

@Serializable
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
    val referenceNumber: String = "R26728",
    val distance: String = "628 km",
    val via: String = "RHA",
    var status: TicketStatus = TicketStatus.UPCOMING
) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() > validTill
}

@Serializable
data class RecentSearch(
    val from: String,
    val to: String
)

object BookingManager {
    private val _bookings = mutableStateListOf<Ticket>()
    val bookings: List<Ticket> get() = _bookings
    
    private val _recentSearches = mutableStateListOf<RecentSearch>()
    val recentSearches: List<RecentSearch> get() = _recentSearches
    
    private const val PREFS_NAME = "railone_prefs"
    private const val KEY_TICKETS = "booked_tickets"
    private const val KEY_SEARCHES = "recent_searches"
    private val json = Json { ignoreUnknownKeys = true }

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        // Load Tickets
        prefs.getString(KEY_TICKETS, null)?.let { savedJson ->
            try {
                val loadedTickets = json.decodeFromString<List<Ticket>>(savedJson)
                _bookings.clear()
                _bookings.addAll(loadedTickets)
            } catch (e: Exception) { e.printStackTrace() }
        }
        
        // Load Recent Searches
        prefs.getString(KEY_SEARCHES, null)?.let { savedJson ->
            try {
                val loadedSearches = json.decodeFromString<List<RecentSearch>>(savedJson)
                _recentSearches.clear()
                _recentSearches.addAll(loadedSearches)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    private fun save(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val ticketsJson = json.encodeToString(_bookings.toList())
        val searchesJson = json.encodeToString(_recentSearches.toList())
        prefs.edit()
            .putString(KEY_TICKETS, ticketsJson)
            .putString(KEY_SEARCHES, searchesJson)
            .apply()
    }

    fun addRecentSearch(context: Context, from: String, to: String) {
        if (from.isEmpty() || to.isEmpty()) return
        val newSearch = RecentSearch(from, to)
        _recentSearches.removeAll { it.from == from && it.to == to }
        _recentSearches.add(0, newSearch)
        if (_recentSearches.size > 5) {
            _recentSearches.removeAt(_recentSearches.size - 1)
        }
        save(context)
    }

    fun bookTicket(
        context: Context,
        source: String,
        dest: String,
        fare: String,
        adults: Int,
        children: Int,
        trainType: String,
        classType: String
    ): Ticket {
        val now = System.currentTimeMillis()
        
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
            irNumber = generateIRNumber(),
            referenceNumber = generateReferenceNumber(),
            distance = getDistanceBetweenStations(source, dest),
            via = getViaStations(source, dest)
        )
        _bookings.add(0, newTicket)
        
        // Also add to recent searches when booking
        addRecentSearch(context, source, dest)
        
        save(context)
        return newTicket
    }

    fun cancelTicket(context: Context, ticketId: String) {
        val index = _bookings.indexOfFirst { it.ticketId == ticketId }
        if (index != -1) {
            _bookings[index] = _bookings[index].copy(status = TicketStatus.CANCELLED)
            save(context)
        }
    }

    private fun generateRandomAlphanumeric(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }

    private fun generateIRNumber(): String {
        // Format like IR:19AAAGM0289C1ZG (15 random chars after IR:)
        return "IR:${generateRandomAlphanumeric(15)}"
    }

    private fun generateReferenceNumber(): String {
        // Format: R + 5 random digits (e.g. R26728)
        val digits = (1..5).map { Random.nextInt(10) }.joinToString("")
        return "R$digits"
    }

    fun getDistanceBetweenStations(source: String, dest: String): String {
        val srcCode = source.split("-").lastOrNull()?.trim() ?: ""
        val destCode = dest.split("-").lastOrNull()?.trim() ?: ""
        
        if ((srcCode == "ERS" && destCode == "SMVB") || (srcCode == "SMVB" && destCode == "ERS")) {
            return "616 km"
        }

        // Deterministic random distance based on station names for other routes
        val seed = (source.hashCode() + dest.hashCode()).toLong()
        val random = Random(seed)
        val dist = random.nextInt(50, 1500)
        return "$dist km"
    }

    fun getViaStations(source: String, dest: String): String {
        val srcCode = source.split("-").lastOrNull()?.trim() ?: ""
        val destCode = dest.split("-").lastOrNull()?.trim() ?: ""

        return when {
            srcCode == "ERS" && destCode == "SMVB" -> "PGT-TUP-BWT"
            srcCode == "SMVB" && destCode == "ERS" -> "BYPL-MLO-TPT"
            else -> "RHA" // Default or lookup logic
        }
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
