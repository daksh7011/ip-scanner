package `in`.technowolf.ipscanner.di

import `in`.technowolf.ipscanner.data.core.IpScannerDatabase
import `in`.technowolf.ipscanner.data.local.IpDetailsDao
import android.app.Application
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun okHttpProvider(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
    okHttpClient.addInterceptor(chuckerInterceptor)
    return okHttpClient.build()
}

fun retrofitProvider(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}

fun provideDatabase(application: Application): IpScannerDatabase {
    return Room.databaseBuilder(application, IpScannerDatabase::class.java, "IpScanner")
        .build()
}

fun provideIpDetailsDao(ipScannerDatabase: IpScannerDatabase): IpDetailsDao {
    return ipScannerDatabase.getIpDetailsDao()
}
