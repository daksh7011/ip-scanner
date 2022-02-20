package `in`.technowolf.ipscanner.data

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class IpDetailRS(
    @Json(name = "status")
    val status: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "countryCode")
    val countryCode: String,
    @Json(name = "region")
    val region: String,
    @Json(name = "regionName")
    val regionName: String,
    @Json(name = "city")
    val city: String,
    @Json(name = "zip")
    val zip: String,
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "lon")
    val lon: Double,
    @Json(name = "timezone")
    val timezone: String,
    @Json(name = "isp")
    val isp: String,
    @Json(name = "org")
    val org: String,
    @Json(name = "as")
    val asnName: String,
    @Json(name = "query")
    val query: String
)