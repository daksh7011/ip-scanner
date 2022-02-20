package `in`.technowolf.ipscanner.ui

import `in`.technowolf.ipscanner.R
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.interaction.BaristaSleepInteractions.sleep
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
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val tilIpAddress = onView(
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
        val fabFetch = onView(
            allOf(
                withId(R.id.fabFetchDetails), withContentDescription("Refresh"),
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
        position: Int,
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
