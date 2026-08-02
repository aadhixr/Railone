package app.railonexr.android.logic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import app.railonexr.android.BuildConfig
import kotlinx.serialization.json.Json

@Serializable
data class GitHubAsset(
    @SerialName("browser_download_url") val downloadUrl: String,
    val name: String
)

@Serializable
data class GitHubRelease(
    @SerialName("tag_name") val tagName: String,
    @SerialName("html_url") val htmlUrl: String,
    val body: String? = null,
    val assets: List<GitHubAsset> = emptyList()
)

interface GitHubService {
    @GET("repos/aadhixr/Railone/releases")
    suspend fun getAllReleases(): List<GitHubRelease>
}

object UpdateManager {
    var updateAvailable by mutableStateOf<GitHubRelease?>(null)
        private set
    
    var isChecking by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
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
        if (isChecking) return
        isChecking = true
        errorMessage = null
        try {
            val releases = service.getAllReleases()
            val latest = releases.firstOrNull() 
            
            if (latest != null) {
                val latestVer = latest.tagName.replace("v", "").trim()
                val currentVer = BuildConfig.VERSION_NAME.replace("v", "").trim()

                if (latestVer != currentVer) {
                    updateAvailable = latest
                } else {
                    updateAvailable = null
                }
            } else {
                errorMessage = "No releases found on GitHub"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = "Check failed: ${e.localizedMessage ?: "Unknown error"}"
            updateAvailable = null
        } finally {
            isChecking = false
        }
    }
}
