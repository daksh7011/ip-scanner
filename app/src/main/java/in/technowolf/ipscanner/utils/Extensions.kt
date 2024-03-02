package `in`.technowolf.ipscanner.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import `in`.technowolf.ipscanner.R
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

    fun View.toggleKeyboard(shouldShow: Boolean) {
        val insetsController = ViewCompat.getWindowInsetsController(this)
        if (shouldShow) {
            insetsController?.show(WindowInsetsCompat.Type.ime())
        } else {
            insetsController?.hide(WindowInsetsCompat.Type.ime())
        }
    }

    fun View.setVisibility(
        visible: Boolean,
        animate: Boolean = true,
        duration: Long = 500L
    ) {
        if (visible) {
            this.visible(animate, duration)
        } else {
            this.gone(animate, duration)
        }
    }

    fun View.visible(
        animate: Boolean = true,
        duration: Long = 500L
    ) {
        if (animate) {
            animate()
                .setDuration(duration)
                .alpha(1F)
                .setListener(
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            visibility = View.VISIBLE
                        }
                    }
                )
        } else {
            visibility = View.VISIBLE
        }
    }

    fun View.gone(
        animate: Boolean = true,
        duration: Long = 500L
    ) {
        if (animate) {
            animate()
                .setDuration(duration)
                .alpha(0F)
                .setListener(
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            visibility = View.GONE
                        }
                    }
                )
        } else {
            visibility = View.GONE
        }
    }
}

fun String?.orNotAvailable(): String {
    return if (this.isNullOrEmpty() || this.isBlank()) "Information not available" else this
}

fun View.setDebouncedClickListener(
    interval: Int = 1000,
    listener: (view: View) -> Unit
) {
    var lastTapTimestamp: Long = 0
    val debouncedListener =
        View.OnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTapTimestamp > interval) {
                lastTapTimestamp = currentTime
                listener(it)
            }
        }
    this.setOnClickListener(debouncedListener)
}

inline fun View.snackBar(
    message: String,
    length: Int = Snackbar.LENGTH_LONG,
    anchorView: View? = null,
    f: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, message, length)
    if (anchorView != null) snack.anchorView = anchorView
    snack.f()
    snack.show()
}

fun Snackbar.action(
    action: String,
    listener: (View) -> Unit
) {
    setAction(action, listener)
    // Manually setting darker shade in dark theme due to accessibility issues for action text.
    val colorSecondary =
        if (context.isDarkMode()) {
            Color.parseColor("#FF6F00")
        } else {
            context.getColorFromThemeAttr(R.attr.colorSecondary)
        }
    setActionTextColor(colorSecondary)
}

fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun Context.isDarkMode(): Boolean {
    return resources.configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}

fun Context.getColorFromThemeAttr(
    @AttrRes attrInt: Int
): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrInt, typedValue, true)
    return typedValue.data
}

suspend fun <T : Any> safeCall(
    call: suspend () -> T?,
    errorHandler: (Throwable) -> Unit
): T? {
    try {
        return call.invoke()
    } catch (e: Throwable) {
        errorHandler.invoke(e)
    }
    return null
}
