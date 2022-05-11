package `in`.technowolf.ipscanner.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IpScannerService {
    @GET("ip/{ip}")
    suspend fun getIpDetails(@Path("ip") ipAddress: String): Response<IpDetailRS>
}
