package com.miguel.myapplication.search

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.miguel.myapplication.R
import com.miguel.myapplication.ui.main.ARG_CITY
import com.miguel.myapplication.ui.main.ARG_VENUE
import com.miguel.myapplication.ui.main.HomeFragment
import io.mockk.mockk
import io.mockk.verify
import navigateSafe
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
    fun search_clickd_success() {
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

}