package com.miguel.myapplication.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.miguel.myapplication.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import navigateSafe
import org.jetbrains.anko.bundleOf


class HomeFragment : Fragment() {

    private lateinit var parent: View

    private val navigation by lazy { Navigation.findNavController(parent) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_main, container, false)

        parent.btn_search.setOnClickListener {
            if(validateCity() && validateVenue()) {
                val city = parent.main_fragment_city_it.text.toString()
                val venue = parent.main_fragment_venue_it.text.toString()
                val bundle = bundleOf(
                    ARG_CITY to city,
                    ARG_VENUE to venue
                )
                navigation.navigateSafe(R.id.action_home_to_search, bundle)
            }
        }

        return parent
    }

    private fun validateVenue(): Boolean =
        if (main_fragment_venue_it.text.toString().trim().isEmpty()) {
            main_fragment_venue_itl.setError("Field is empty")
            requestFocus(main_fragment_venue_it)
            false
        } else {
            main_fragment_venue_itl.setErrorEnabled(false)
            true
        }


    private fun validateCity(): Boolean =
        if (main_fragment_city_it.text.toString().trim().isEmpty()) {
            main_fragment_city_itl.setError("Field is empty")
            requestFocus(main_fragment_city_it)
            false
        } else {
            main_fragment_city_itl.setErrorEnabled(false)
            true
        }


    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            activity?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
