package com.morcinek.omise

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.morcinek.omise.ui.NavActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NavActivityInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(NavActivity::class.java)

    @Test
    fun hasToolbarWithCharities() {
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Charities"))))
    }

    @Test
    fun tapOnBackArrowInDonationScreen() {
        Thread.sleep(1000)
        onView(withText("Habitat for Humanity"))
            .perform(click())

        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Donation"))))
        onView(withContentDescription("Navigate up"))
            .perform(click())

        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Charities"))))
    }

    @Test
    fun tapOnItemShowsDonationsScreen() {
        Thread.sleep(1000)
        onView(withText("Habitat for Humanity"))
            .perform(click())

        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Donation"))))

        onView(withId(R.id.nameTextInputLayout)).check(matches(isDisplayed()))

        onView(withId(R.id.amount)).check(matches(isDisplayed()))
        onView(withText("Choose amount")).check(matches(isDisplayed()))
        onView(withText("0 THB")).check(matches(isDisplayed()))

        onView(withId(R.id.creditCard)).check(matches(isDisplayed()))
        onView(withText("Select credit card")).check(matches(isDisplayed()))
        onView(withText("NOT SET")).check(matches(isDisplayed()))

        onView(withId(R.id.confirmButton))
            .check(matches(withText("MAKE DONATION")))
            .check(matches(isDisplayed()))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun tapOnSelectCreditCardInDonationStartsCreditCardActivity() {
        Thread.sleep(1000)
        onView(withText("Habitat for Humanity"))
            .perform(click())

        onView(withText("Select credit card"))
            .perform(click())

        onView(withText("Credit Card")).check(matches(isDisplayed()))

        onView(withText("Card number")).check(matches(isDisplayed()))
        onView(withText("Name on card")).check(matches(isDisplayed()))
        onView(withText("Expiration date")).check(matches(isDisplayed()))
        onView(withText("Security code")).check(matches(isDisplayed()))

        onView(withText("PAY"))
            .check(matches(isDisplayed()))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun makingDonation() {
        Thread.sleep(1000)
        onView(withText("Habitat for Humanity"))
            .perform(click())

        onView(isAssignableFrom(EditText::class.java))
            .perform(replaceText("Tomasz M"))
        onView(withText("MAKE DONATION")).check(matches(not(isEnabled())))

        onView(withId(R.id.amount))
            .perform(click())
        onView(withText("Choose amount")).check(matches(isDisplayed()))
        onView(withText("50 THB"))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.amount)).check(matches(hasDescendant(withText("50 THB"))))

        onView(withId(R.id.amount))
            .perform(click())
        onView(withText("1000 THB"))
            .perform(click())
        onView(withId(R.id.amount)).check(matches(hasDescendant(withText("1000 THB"))))

        onView(withText("MAKE DONATION")).check(matches(not(isEnabled())))

        onView(withText("Select credit card"))
            .perform(click())
        onView(withId(R.id.edit_card_number))
            .perform(typeText("4658904090346222"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.edit_expiry_date))
            .perform(scrollTo())
            .perform(typeText("324"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.edit_security_code))
            .perform(scrollTo())
            .perform(typeText("123"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.edit_card_name))
            .perform(scrollTo())
            .perform(replaceText("Someone J"))
            .perform(closeSoftKeyboard())

        onView(withText("PAY"))
            .check(matches(isEnabled()))
            .perform(click())
        Thread.sleep(4000)

        onView(withText("MAKE DONATION"))
            .check(matches(isEnabled()))
            .perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Success"))))
        onView(withText("Your donation for\n\'Habitat for Humanity\'\nhas finished with success!")).check(matches(isDisplayed()))
        onView(withText("OK"))
            .perform(click())
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Charities"))))
    }
}
