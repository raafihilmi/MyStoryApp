package com.bumantra.mystoryapp.ui.auth

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bumantra.mystoryapp.R
import com.bumantra.mystoryapp.ui.auth.login.LoginActivity
import com.bumantra.mystoryapp.ui.auth.welcome.WelcomeActivity
import com.bumantra.mystoryapp.ui.main.MainActivity
import com.bumantra.mystoryapp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AuthUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLoginLogoutSuccess() {
        // Input dulu ygy

        onView(withId(R.id.ed_login_email)).perform(
            typeText("akundummy@testing.com"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"), closeSoftKeyboard())

        // Klik tombol login
        onView(withId(R.id.loginButton)).perform(click())
        Intents.init()
        onView(withText("Yeah")).inRoot(isDialog()).check(matches(isDisplayed()))
        onView(withText("Lanjut")).inRoot(isDialog()).perform(click())

        // Pindah ke Main Activity
        intended(hasComponent(MainActivity::class.java.name))
        Intents.release()
        onView(withId(R.id.mainActivity)).check(matches(isDisplayed()))

        // Logout
        onView(withId(R.id.action_logout)).perform(click())
        Intents.init()
        onView(withText("Logout")).inRoot(isDialog()).check(matches(isDisplayed()))
        onView(withText("Ya")).inRoot(isDialog()).perform(click())

        // Pindah ke WelcomeActivity guys
        intended(hasComponent(WelcomeActivity::class.java.name))
        Intents.release()
        onView(withId(R.id.welcomeActivity)).check(matches(isDisplayed()))

    }

}