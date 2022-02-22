package `in`.technowolf.ipscanner.ui

import `in`.technowolf.ipscanner.R
import `in`.technowolf.ipscanner.databinding.ActivityMainBinding
import `in`.technowolf.ipscanner.utils.Extensions.gone
import `in`.technowolf.ipscanner.utils.Extensions.hideKeyboard
import `in`.technowolf.ipscanner.utils.Extensions.toFlagEmoji
import `in`.technowolf.ipscanner.utils.Extensions.visible
import `in`.technowolf.ipscanner.utils.orNotAvailable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        setupObservers()
        setupIpValidationView()
        setupFab()
        hideViews(false)
        binding.tilIpAddress.setEndIconOnClickListener {
            binding.etIpAddress.setText("")
        }
    }

    private fun setupObservers() {
        mainViewModel.ipDetails.observe(this) {
            binding.etIpAddress.setText(it.ipAddress)
            binding.apply {
                idvCountry.setValuesToView(
                    "Country",
                    "${it.country} ${it.countryCode.toFlagEmoji()}",
                )
                idvRegion.setValuesToView("Region", it.regionName.orNotAvailable())
                idvCity.setValuesToView("City", it.city.orNotAvailable())
                idvZipCode.setValuesToView("Zipcode", it.zip.orNotAvailable())
                idvTimeZone.setValuesToView("Timezone", it.timezone.orNotAvailable())
                idvLatLong.setValuesToView("Lat-Long", "${it.lat} , ${it.lon}")
                idvIsp.setValuesToView("ISP", it.isp.orNotAvailable())
                idvOrganization.setValuesToView("Organization", it.org.orNotAvailable())
                idvAsn.setValuesToView("ASN", it.asnName.orNotAvailable())
            }
            hideLoader()
            showViews()
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
                mainViewModel.getIpDetails(ip)
            } else {
                Toast.makeText(this, "Invalid Ip!", Toast.LENGTH_SHORT).show()
            }
            it.hideKeyboard()
            binding.etIpAddress.clearFocus()
            hideViews()
            showLoader()
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

    private fun showLoader() {
        binding.loadingView.bringToFront()
        binding.loadingView.visible(animate = true)
    }

    private fun hideLoader() {
        binding.loadingView.bringToFront()
        binding.loadingView.gone(animate = true)
    }
}
