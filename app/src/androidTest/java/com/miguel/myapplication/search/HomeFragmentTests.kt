package com.miguel.myapplication.search

import android.content.Context
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.textfield.TextInputLayout
import com.miguel.myapplication.R
import com.miguel.myapplication.ui.main.ARG_CITY
import com.miguel.myapplication.ui.main.ARG_VENUE
import com.miguel.myapplication.ui.main.HomeFragment
import io.mockk.mockk
import io.mockk.verify
import navigateSafe
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.jetbrains.anko.bundleOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4::class)
class HomeFragmentTests : KoinTest {

    private lateinit var context: Context

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun home_fragment_view() {

        launchFragmentInContainer<HomeFragment>(themeResId = R.style.ToolbarTheme)
        onView(withId(R.id.main_fragment_city_itl)).check(matches(hasTextInputLayoutHintText("City")))
        onView(withId(R.id.main_fragment_venue_itl)).check(matches(hasTextInputLayoutHintText("Venue")))
        onView(withText("Search")).check(matches(isDisplayed()))
        onView(withText("Last Query")).check(matches(isDisplayed()))

    }


    @Test
    fun home_fragment_city_empty() {

        launchFragmentInContainer<HomeFragment>(themeResId = R.style.ToolbarTheme)

        onView(withId(R.id.btn_search)).perform(click())
        onView(withId(R.id.main_fragment_city_itl)).check(matches(hasTextInputLayoutErrorText("Field is empty")))

    }


    @Test
    fun home_fragment_venue_empty() {

        launchFragmentInContainer<HomeFragment>(themeResId = R.style.ToolbarTheme)

        onView(withId(R.id.main_fragment_city_it))
            .perform(clearText(), typeText("something"), closeSoftKeyboard())
        onView(withId(R.id.btn_search)).perform(click())
        onView(withId(R.id.main_fragment_venue_itl)).check(matches(hasTextInputLayoutErrorText("Field is empty")))

    }


    @Test
    fun last_query_click_success() {

        val venue = "LAST QUERY"
        val mockNavController = mockk<NavController>(relaxed = true)
        val recognitionScenario = launchFragmentInContainer<HomeFragment>(themeResId = R.style.ToolbarTheme)
        recognitionScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        val bundle = bundleOf(
            ARG_VENUE to venue
        )

        onView(withId(R.id.btn_last_query)).perform(click())

        verify(exactly = 1) { mockNavController.navigateSafe(R.id.action_home_to_search, bundle) }
    }

    @Test
    fun search_click_success() {
        val city = "city"
        val venue = "venue"
        val mockNavController = mockk<NavController>(relaxed = true)
        val recognitionScenario = launchFragmentInContainer<HomeFragment>(themeResId = R.style.ToolbarTheme)
        recognitionScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        val bundle = bundleOf(
            ARG_CITY to city,
            ARG_VENUE to venue
        )
        onView(withId(R.id.main_fragment_city_it))
            .perform(clearText(), typeText(city))

        onView(withId(R.id.main_fragment_venue_it))
            .perform(clearText(), typeText(venue), closeSoftKeyboard())

        onView(withId(R.id.btn_search)).perform(click())

        verify(exactly = 1) { mockNavController.navigateSafe(R.id.action_home_to_search, bundle) }
    }



    fun hasTextInputLayoutHintText(expectedErrorText: String): Matcher<View> = object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description?) { }

        override fun matchesSafely(item: View?): Boolean {
            if (item !is TextInputLayout) return false
            val error = item.hint ?: return false
            val hint = error.toString()
            return expectedErrorText == hint
        }
    }

    fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> = object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description?) { }

        override fun matchesSafely(item: View?): Boolean {
            if (item !is TextInputLayout) return false
            val error = item.error ?: return false
            val hint = error.toString()
            return expectedErrorText == hint
        }
    }
}