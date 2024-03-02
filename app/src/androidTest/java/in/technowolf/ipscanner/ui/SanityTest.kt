package `in`.technowolf.ipscanner.ui

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.adevinta.android.barista.interaction.BaristaSleepInteractions.sleep
import `in`.technowolf.ipscanner.R
import `in`.technowolf.ipscanner.ui.home.HomeActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SanityTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun mainActivityTest() {
        val tilIpAddress =
            onView(
                allOf(
                    withId(R.id.etIpAddress),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.tilIpAddress),
                            0
                        ),
                        1
                    ),
                    isDisplayed()
                )
            )
        val fabFetch =
            onView(
                allOf(
                    withId(R.id.fabFetchDetails),
                    withContentDescription("Refresh"),
                    childAtPosition(
                        allOf(
                            withId(R.id.container),
                            childAtPosition(
                                withId(android.R.id.content),
                                0
                            )
                        ),
                        2
                    ),
                    isDisplayed()
                )
            )

        sleep(3000)
        tilIpAddress.perform(click())
        tilIpAddress.perform(replaceText("63.25.122.4"))
        tilIpAddress.perform(closeSoftKeyboard())
        tilIpAddress.perform(pressImeActionButton())
        fabFetch.perform(click())
        sleep(3000)

        tilIpAddress.perform(click())
        tilIpAddress.perform(replaceText("43.155.201.25"))
        tilIpAddress.perform(closeSoftKeyboard())
        tilIpAddress.perform(pressImeActionButton())
        fabFetch.perform(click())
        sleep(3000)

        tilIpAddress.perform(click())
        tilIpAddress.perform(replaceText("1.3.3.7"))
        tilIpAddress.perform(closeSoftKeyboard())
        tilIpAddress.perform(pressImeActionButton())
        fabFetch.perform(click())
        sleep(3000)
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>,
        position: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup &&
                    parentMatcher.matches(parent) &&
                    view == parent.getChildAt(position)
            }
        }
    }
}
