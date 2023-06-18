package `in`.technowolf.ipscanner.ui.about

import `in`.technowolf.ipscanner.BuildConfig
import `in`.technowolf.ipscanner.databinding.ActivityAboutBinding
import `in`.technowolf.ipscanner.utils.Constants
import `in`.technowolf.ipscanner.utils.setDebouncedClickListener
import `in`.technowolf.ipscanner.utils.snackBar
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
    }

    private fun setupUi() {
        binding.tvAppVersion.text =
            String.format("%s (%s)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
        binding.lvvRepo.setDebouncedClickListener {
            launchBrowserWithUrl(Constants.REPO_LINK)
        }
        binding.lvvIssueTracker.setDebouncedClickListener {
            launchBrowserWithUrl(Constants.ISSUE_TRACKER)
        }
        binding.lvvSupportDevelopment.setDebouncedClickListener {
            launchBrowserWithUrl(Constants.DONATE)
        }
        binding.lvvRateApp.setDebouncedClickListener {
            launchBrowserWithUrl("", isPlayStoreLink = true)
        }
        binding.lvvDeveloperTwitter.setDebouncedClickListener {
            launchBrowserWithUrl(Constants.DEVELOPER_TWITTER)
        }
        binding.lvvDeveloperBlog.setDebouncedClickListener {
            launchBrowserWithUrl(Constants.DEVELOPER_BLOG)
        }
        binding.lvvPrivacyPolicy.setDebouncedClickListener {
            launchBrowserWithUrl(Constants.PRIVACY_POLICY)
        }
        binding.lvvTermsAndConditions.setDebouncedClickListener {
            launchBrowserWithUrl(Constants.TERMS_AND_CONDITION)
        }
        binding.lvvLicenses.setDebouncedClickListener {
            it.snackBar("To be developed!") {}
        }

    }

    private fun launchBrowserWithUrl(url: String, isPlayStoreLink: Boolean = false) {
        if (isPlayStoreLink.not()) {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                startActivity(this)
            }
        } else {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }
    }
}
