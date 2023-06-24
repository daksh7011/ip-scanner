package `in`.technowolf.ipscanner.di

import `in`.technowolf.ipscanner.data.remote.IpScannerService
import `in`.technowolf.ipscanner.data.remote.PublicIpService
import `in`.technowolf.ipscanner.ui.home.HomeViewModel
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

const val IP_SCANNER = "IpScanner"
const val IPIFY = "Ipify"

val retrofitModule = module {
    single { okHttpProvider(get()) }
    single(named(IP_SCANNER)) { retrofitProvider(get(), "https://technowolf.in/") }
    single(named(IPIFY)) { retrofitProvider(get(), "https://api.ipify.org") }
}

val repoModule = module {
    single { get<Retrofit>(named(IP_SCANNER)).create(IpScannerService::class.java) }
    single { get<Retrofit>(named(IPIFY)).create(PublicIpService::class.java) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
}

val appModule = module {
    single {
        ChuckerInterceptor.Builder(get())
            .collector(get())
            .maxContentLength(chuckerMaxContentLength)
            .alwaysReadResponseBody(false)
            .build()
    }
    single {
        ChuckerCollector(context = get(), showNotification = true)
    }
}

val databaseModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideIpDetailsDao(get()) }
}

private const val chuckerMaxContentLength = 250000L
