package com.miguel.myapplication.ui.main

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.miguel.myapplication.R
import com.miguel.myapplication.datasource.NO_ERROR
import com.miguel.myapplication.datasource.ui.Venue
import com.miguel.myapplication.ui.adapters.VenuesAdapter
import com.miguel.myapplication.viewmodel.VenuesViewModel
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import otherwise

//TODO: THIS IS A COPY OF SEARCH FRAGMENT - IT SHOULD BE HANDLE BOTH STATES IN SAME FRAGMENT
//FRAGMENT TO PROVE NAVIGATION WITH 2 ACTIONS
class LastQueryFragment : Fragment() {

    private val venuesViewModel: VenuesViewModel by viewModel()
    private lateinit var venuesAdapter: VenuesAdapter
    private lateinit var venue: String

    var loadingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        getBundleData()

        if (::venue.isInitialized) {
            initAdapter(venue, view)
            setVenuesLiveData()
            showLoadingState()
            //Uncomment to test with coroutines
            venuesViewModel.lastQueryCoroutines()
            //Uncomment to test with rxjava
            //venuesViewModel.lastQuery()
        } else {
            showErrorPopup()
        }

        return view
    }

    private fun getBundleData() {
        arguments?.let {
            venue = it.getString(ARG_VENUE, "")
        }.otherwise {
            showErrorPopup()
        }
    }

    fun setVenuesLiveData() {
        venuesViewModel.venueListLiveData.observe(this, Observer { venueList ->
            refreshVenues(venueList)
        })

        venuesViewModel.errorLiveData.observe(this, Observer {
            if (it != NO_ERROR) {
                Log.i("TEST-I", "Error code:$it")
                hideLoadingState()
                showErrorPopup()
            }
        })

    }

    fun refreshVenues(venueList: List<Venue>) {
        hideLoadingState()
        venuesAdapter.venuesList = venueList
    }


    private fun initAdapter(title: String, view: View) {
        view.venue_list.layoutManager = LinearLayoutManager(this.context)
        venuesAdapter = VenuesAdapter()
        venuesAdapter.title = title
        view.venue_list.adapter = venuesAdapter
        //TODO:GO to details screen
        venuesAdapter.onVenueClickListener = { data ->
            Log.i("TEST-I", "data:$data")
        }
    }

    private fun hideLoadingState() {
        loadingDialog?.dismiss()
    }

    private fun showLoadingState() {
        hideLoadingState()
        val llPadding = 30
        val ll = LinearLayout(this.context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(this.context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(this.context)
        tvText.text = getString(R.string.common_loading)
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(this.context)
        builder.setCancelable(true)
        builder.setView(ll)

        loadingDialog = builder.create()
        loadingDialog?.show()
        loadingDialog?.window?.let {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(it.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }
    }

    fun showErrorPopup() {
        this.context?.let { innerContext ->
            androidx.appcompat.app.AlertDialog.Builder(innerContext)
                .setTitle(R.string.state_error_close_title)
                .setMessage(R.string.state_error_close_text)
                .setPositiveButton(getString(R.string.button_close)) { di, _ ->
                    di.dismiss()
                    activity?.finishAffinity()
                }.show()
        }

    }


    companion object {
        @JvmStatic
        fun newInstance(city: String, venue: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CITY, city)
                    putString(ARG_VENUE, venue)
                }
            }
    }
}
