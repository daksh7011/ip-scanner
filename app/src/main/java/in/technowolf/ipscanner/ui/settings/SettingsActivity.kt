package `in`.technowolf.ipscanner.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import `in`.technowolf.ipscanner.R
import `in`.technowolf.ipscanner.databinding.SettingsActivityBinding
import `in`.technowolf.ipscanner.utils.ProcessPhoenix
import `in`.technowolf.ipscanner.utils.action
import `in`.technowolf.ipscanner.utils.snackBar

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(
            savedInstanceState: Bundle?,
            rootKey: String?
        ) {
            setPreferencesFromResource(R.xml.settings_screen_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val root = (requireActivity() as SettingsActivity).binding.root
            when (preference.key) {
                "dark_mode" -> {
                    root.snackBar("Changes will be applied next time you launch the app.") {
                        action("Reload") {
                            ProcessPhoenix.triggerRebirth(requireContext())
                        }
                    }
                }

                "crashlytics" -> {
                    val isCrashlyticsEnabled = prefs.getBoolean("crashlytics", true)
                    if (isCrashlyticsEnabled.not()) {
                        root.snackBar("You have opted out from crash logs submission.") {}
                    }
                }
            }
            return super.onPreferenceTreeClick(preference)
        }
    }
}
