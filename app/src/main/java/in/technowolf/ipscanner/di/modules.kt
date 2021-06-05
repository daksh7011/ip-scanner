package `in`.technowolf.ipscanner.di

import `in`.technowolf.ipscanner.BuildConfig
import `in`.technowolf.ipscanner.data.IpScannerService
import `in`.technowolf.ipscanner.data.PublicIpService
import `in`.technowolf.ipscanner.ui.MainViewModel
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val IP_SCANNER = "IpScanner"
const val IPIFY = "Ipify"

val retrofitModule = module {
    single { okHttpProvider(get()) }
    single(named(IP_SCANNER)) { retrofitProvider(get()) }
    single(named(IPIFY)) { retrofitProviderForIpify(get()) }
}

val repoModule = module {
    single { get<Retrofit>(named(IP_SCANNER)).create(IpScannerService::class.java) }
    single { get<Retrofit>(named(IPIFY)).create(PublicIpService::class.java) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
}

val appModule = module {
    single {
        ChuckerInterceptor.Builder(get())
            .collector(get())
            .maxContentLength(250000L)
            .alwaysReadResponseBody(false)
            .build()
    }
    single {
        ChuckerCollector(context = get(), showNotification = true)
    }
}

private fun okHttpProvider(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
    okHttpClient.addInterceptor(chuckerInterceptor)
    return okHttpClient.build()
}

private fun retrofitProvider(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}

private fun retrofitProviderForIpify(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.ipify.org")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}
