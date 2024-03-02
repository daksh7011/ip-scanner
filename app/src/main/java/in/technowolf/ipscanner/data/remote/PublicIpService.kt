package `in`.technowolf.ipscanner.data.remote

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET

interface PublicIpService {
    @GET("?format=json")
    suspend fun getPublicIp(): Response<PublicIpRS>
}

@Keep
@JsonClass(generateAdapter = true)
data class PublicIpRS(
    @Json(name = "ip")
    val ip: String,
)
