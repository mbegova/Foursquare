package com.miguel.myapplication.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.miguel.myapplication.R
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
            val city = parent.main_fragment_city_it.text.toString()
            val venue = parent.main_fragment_venue_it.text.toString()
            val bundle = bundleOf(
                ARG_CITY to city,
                ARG_VENUE to venue
            )
            navigation.navigateSafe(R.id.action_home_to_search, bundle)
        }

        return parent
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
