package `in`.technowolf.ipscanner.ui.home

import `in`.technowolf.ipscanner.R
import `in`.technowolf.ipscanner.databinding.ActivityHomeBinding
import `in`.technowolf.ipscanner.ui.about.AboutActivity
import `in`.technowolf.ipscanner.ui.settings.SettingsActivity
import `in`.technowolf.ipscanner.utils.Extensions.gone
import `in`.technowolf.ipscanner.utils.Extensions.toFlagEmoji
import `in`.technowolf.ipscanner.utils.Extensions.toggleKeyboard
import `in`.technowolf.ipscanner.utils.Extensions.visible
import `in`.technowolf.ipscanner.utils.capitalize
import `in`.technowolf.ipscanner.utils.orNotAvailable
import `in`.technowolf.ipscanner.utils.snackBar
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding: ActivityHomeBinding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        applyPreferences()
        setupObservers()
        setupIpValidationView()
        setupBottomBar()
        setupTextInputLayout()
        setupFab()
        hideViews(false)
    }

    private fun applyPreferences() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this@HomeActivity)

        val isDarkMode =
            if (prefs.getBoolean("dark_mode", true)) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(isDarkMode)

        val isCrashlyticsEnabled = prefs.getBoolean("crashlytics", true)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isCrashlyticsEnabled)

        val isAutoFetchEnabled = prefs.getBoolean("auto_fetch_ip", true)
        if (isAutoFetchEnabled) {
            homeViewModel.getIpDetails()
        } else {
            hideLoader()
            binding.etIpAddress.requestFocus()
        }
    }

    private fun setupObservers() {
        homeViewModel.errorLiveData.observe(this) {
            binding.root.snackBar(it, anchorView = binding.fabFetchDetails) {}
        }
        homeViewModel.ipDetails.observe(this) {
            if (it.message.isNullOrEmpty().not()) {
                binding.root.snackBar(
                    "${it.ipAddress} is ${it.message?.capitalize()}",
                    anchorView = binding.fabFetchDetails
                ) {}
                hideViews()
            } else {
                val country = if (it.country.isNullOrEmpty()) it.country.orNotAvailable()
                else "${it.country} ${it.countryCode?.toFlagEmoji()}"
                binding.etIpAddress.setText(it.ipAddress)
                binding.apply {
                    idvCountry.setValuesToView("Country", country)
                    idvRegion.setValuesToView("Region", it.regionName.orNotAvailable())
                    idvCity.setValuesToView("City", it.city.orNotAvailable())
                    idvZipCode.setValuesToView("Zipcode", it.zip.orNotAvailable())
                    idvTimeZone.setValuesToView("Timezone", it.timezone.orNotAvailable())
                    idvLatLong.setValuesToView("Lat-Long", "${it.lat} , ${it.lon}")
                    idvIsp.setValuesToView("ISP", it.isp.orNotAvailable())
                    idvOrganization.setValuesToView("Organization", it.org.orNotAvailable())
                    idvAsn.setValuesToView("ASN", it.asnName.orNotAvailable())
                }
                showViews()
            }
            hideLoader()
        }
    }

    private fun setupIpValidationView() {
        binding.ipValidationView.ipEditText = binding.etIpAddress
        binding.ipValidationView.enabledColor = R.color.colorPrimary
    }

    private fun setupFab() {
        binding.fabFetchDetails.setOnClickListener {
            val ip =
                binding.etIpAddress.text.toString().ifEmpty { "" }
            if (binding.ipValidationView.isIpValid) {
                homeViewModel.getIpDetails(ip)
                showLoader()
            } else {
                Toast.makeText(this, "Invalid Ip!", Toast.LENGTH_SHORT).show()
            }
            hideViews()
            it.toggleKeyboard(shouldShow = false)
            binding.etIpAddress.clearFocus()
        }
    }

    private fun showViews() {
        binding.idvCountry.visible()
        binding.idvRegion.visible()
        binding.idvCity.visible()
        binding.idvZipCode.visible()
        binding.idvTimeZone.visible()
        binding.idvLatLong.visible()
        binding.idvIsp.visible()
        binding.idvOrganization.visible()
        binding.idvAsn.visible()
    }

    private fun hideViews(animate: Boolean = true) {
        binding.idvCountry.gone(animate)
        binding.idvRegion.gone(animate)
        binding.idvCity.gone(animate)
        binding.idvZipCode.gone(animate)
        binding.idvTimeZone.gone(animate)
        binding.idvLatLong.gone(animate)
        binding.idvIsp.gone(animate)
        binding.idvOrganization.gone(animate)
        binding.idvAsn.gone(animate)
    }

    private fun setupTextInputLayout() {
        binding.tilIpAddress.setEndIconOnClickListener {
            binding.etIpAddress.setText("")
        }
    }

    private fun setupBottomBar() {
        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_bar_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }
                R.id.bottom_bar_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun showLoader() {
        binding.loadingView.bringToFront()
        binding.loadingView.visible(animate = true)
    }

    private fun hideLoader() {
        binding.loadingView.bringToFront()
        binding.loadingView.gone(animate = true)
    }
}
