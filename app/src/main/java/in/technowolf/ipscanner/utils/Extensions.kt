package `in`.technowolf.ipscanner.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Locale

object Extensions {

    /**
     * This method is to change the country code like "us" into flag.
     * 1. It first checks if the string consists of only 2 characters: ISO 3166-1 alpha-2
     * two-letter country codes (https://en.wikipedia.org/wiki/Regional_Indicator_Symbol).
     * 2. It then checks if both characters are alphabet
     * do nothing if it doesn't fulfil the 2 checks
     * caveat: if you enter an invalid 2 letter country code, say "XX", it will pass the 2
     * checks, and it will return unknown result
     */

    fun String.toFlagEmoji(): String {
        // 1. It first checks if the string consists of only 2 characters: ISO 3166-1 alpha-2
        // two-letter country codes (https://en.wikipedia.org/wiki/Regional_Indicator_Symbol).
        if (this.length != 2) {
            return this
        }

        // upper case is important because we are calculating offset
        val countryCodeCaps =
            this.uppercase(Locale.getDefault())
        val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
        val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

        // 2. It then checks if both characters are alphabet
        if (!countryCodeCaps[0].isLetter() || !countryCodeCaps[1].isLetter()) {
            return this
        }

        return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
    }

    fun <T> MutableLiveData<T>.readOnly(): LiveData<T> = this

    fun View.showKeyboard() {
        val insetsController = ViewCompat.getWindowInsetsController(this)
        insetsController?.show(WindowInsetsCompat.Type.ime())
    }

    fun View.hideKeyboard() {
        val insetsController = ViewCompat.getWindowInsetsController(this)
        insetsController?.hide(WindowInsetsCompat.Type.ime())
    }

    fun View.visible(animate: Boolean = true) {
        if (animate) {
            animate()
                .setDuration(500L)
                .alpha(1F)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = View.VISIBLE
                    }
                })
        } else {
            visibility = View.VISIBLE
        }
    }

    fun View.gone(animate: Boolean = true) {
        if (animate) {
            animate()
                .setDuration(500L)
                .alpha(0F)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = View.GONE
                    }
                })
        } else {
            visibility = View.GONE
        }
    }
}

fun String.orNotAvailable(): String {
    return if (this.isEmpty() || this.isBlank()) "Information not available" else this
}
