package com.miguel.myapplication.ui.main

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.miguel.myapplication.R
import com.miguel.myapplication.datasource.remote.Venue
import com.miguel.myapplication.ui.adapters.VenuesAdapter
import com.miguel.myapplication.viewmodel.VenuesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val venuesViewModel: VenuesViewModel by viewModel()
    private lateinit var venuesAdapter: VenuesAdapter

    var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val title = "Costa Coffee"
        initAdapter(title)
        setVenuesLiveData()
        showLoadingState()
        venuesViewModel.searchVenue()
    }


    fun setVenuesLiveData() {
        venuesViewModel.venueListLiveData.observe(this, Observer {venueList->
            refreshVenues(venueList)
        })

        venuesViewModel.errorLiveData.observe(this, Observer {
            hideLoadingState()
            showErrorPopup()
        })

    }

    fun refreshVenues(venueList: List<Venue>) {
        hideLoadingState()
        venuesAdapter.venuesList = venueList
    }


    private fun initAdapter(title:String) {
        venue_list.layoutManager = LinearLayoutManager(this)
        venuesAdapter = VenuesAdapter()
        venuesAdapter.title = title
        venue_list.adapter = venuesAdapter
        //TODO:GO to details screen
        venuesAdapter.onVenueClickListener = {data->
            Log.i("TEST-I", "data:$data")
        }
    }

    private fun hideLoadingState() {
        loadingDialog?.dismiss()
    }

    private fun showLoadingState() {
        hideLoadingState()
        val llPadding = 30
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(this)
        tvText.text = getString(R.string.common_loading)
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(this)
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
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.state_error_close_title)
                .setMessage(R.string.state_error_close_text)
                .setPositiveButton(getString(R.string.button_close)) { di, _ ->
                    di.dismiss()
                    finishAffinity()
                }.show()

    }

}
