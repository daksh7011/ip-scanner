package `in`.technowolf.ipscanner

import `in`.technowolf.ipscanner.di.*
import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IpScanner : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)

        startKoin {
            androidContext(this@IpScanner)
            modules(listOf(
                retrofitModule,
                repoModule,
                viewModelModule,
                appModule,
                databaseModule
            ))
        }
    }

}