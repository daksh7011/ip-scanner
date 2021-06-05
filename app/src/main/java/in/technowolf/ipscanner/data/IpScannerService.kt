package `in`.technowolf.ipscanner.data

import retrofit2.http.GET
import retrofit2.http.Path

interface IpScannerService {
    @GET("ip-scrapper/{ip}")
    suspend fun getIpDetails(@Path("ip") ipAddress: String): IpDetailRS

}