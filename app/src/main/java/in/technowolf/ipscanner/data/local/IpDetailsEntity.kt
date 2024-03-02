package `in`.technowolf.ipscanner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import `in`.technowolf.ipscanner.data.remote.IpDetailRS

@Entity(tableName = "IpDetails")
data class IpDetailsEntity(
    val status: String?,
    val country: String?,
    val countryCode: String?,
    val region: String?,
    val regionName: String?,
    val city: String?,
    val zip: String?,
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    val isp: String?,
    val org: String?,
    val asnName: String?,
    @PrimaryKey
    val ipAddress: String,
    val message: String? = null,
) {
    fun toIpDetailRS() =
        IpDetailRS(
            status,
            country,
            countryCode,
            region,
            regionName,
            city,
            zip,
            lat,
            lon,
            timezone,
            isp,
            org,
            asnName,
            ipAddress,
            message,
        )
}
