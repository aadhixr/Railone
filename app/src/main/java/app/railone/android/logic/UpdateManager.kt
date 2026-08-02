package app.railone.android.logic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import okhttp3.MediaType
import okhttp3.MediaType.   Companion.toMediaType
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import app.railone.android.BuildConfig
import kotlinx.serialization.json.Json

@Serializable
data class GitHubRelease(
    @SerialName("tag_name") val tagName: String,
    @SerialName("html_url") val htmlUrl: String,
    val body: String? = null
)

interface GitHubService {
    @GET("repos/aadhixr/Railone/releases/latest")
    suspend fun getLatestRelease(): GitHubRelease
}

object UpdateManager {
    var updateAvailable by mutableStateOf<GitHubRelease?>(null)
        private set

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()

    private val service = retrofit.create(GitHubService::class.java)

    suspend fun checkForUpdates() {
        try {
            val latest = service.getLatestRelease()
            
            // Clean up version strings (remove 'v', spaces, etc.)
            val latestVer = latest.tagName.replace("v", "").trim()
            val currentVer = BuildConfig.VERSION_NAME.replace("v", "").trim()

            // Only show if the versions are different
            // In a better system, you'd check if latestVer > currentVer
            if (latestVer != currentVer) {
                updateAvailable = latest
            } else {
                updateAvailable = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
