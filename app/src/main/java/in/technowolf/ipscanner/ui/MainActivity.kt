package `in`.technowolf.ipscanner.ui

import `in`.technowolf.ipscanner.databinding.ActivityMainBinding
import `in`.technowolf.ipscanner.utils.Extensions.toFlagEmoji
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        binding.fabFetchDetails.setOnClickListener {
            mainViewModel.getIpDetails(binding.etIpAddress.text.toString())
        }
    }

    private fun setupUI() {
        setupObservers()
    }

    private fun setupObservers() {
        mainViewModel.ipDetails.observe(this, {
            binding.etIpAddress.setText(it.query)
            binding.apply {
                tvContinent.text = it.continent
                tvCountry.text = String.format("%s %s", it.countryCode.toFlagEmoji(), it.country)
            }
        })
    }
}