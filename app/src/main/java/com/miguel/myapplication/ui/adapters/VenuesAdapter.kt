package com.miguel.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.miguel.myapplication.R
import com.miguel.myapplication.datasource.remote.Venue
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_empty.view.*
import kotlinx.android.synthetic.main.view_title.view.*
import kotlinx.android.synthetic.main.view_venue.view.*

private const val TYPE_HEADER = 1
private const val TYPE_LIST = 2
private const val TYPE_EMPTY = -1

class VenuesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onVenueClickListener: (String) -> Unit

    var title: String = "Venue"

    var venuesList: List<Venue> = mutableListOf()
        set(teachers) {
            field = teachers
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> TitleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_title,
                    parent,
                    false
                )
            )
            TYPE_EMPTY -> EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_empty,
                    parent,
                    false
                ), R.string.data_empty_message
            )
            else -> VenuesViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_venue, parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VenuesViewHolder -> {
                holder.selectedVenueListener = onVenueClickListener
                holder.bind(venuesList[position - 1])
            }
            is TitleViewHolder -> {
                holder.bind(title)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when {
        venuesList.isEmpty() -> TYPE_EMPTY
        position == 0 -> TYPE_HEADER
        else -> TYPE_LIST
    }

    override fun getItemCount(): Int = venuesList.size + 1

    inner class VenuesViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView), LayoutContainer {
        lateinit var selectedVenueListener: (String) -> Unit

        override val containerView: View?
            get() = itemView

        fun bind(item: Venue) {
            itemView.venue_address_tv.text = item.location?.address
            itemView.venue_postcode_tv.text = item.location?.postalCode
            itemView.setOnClickListener {
                selectedVenueListener.invoke("item:$item")
            }
        }

    }

    class TitleViewHolder(parent: View) : RecyclerView.ViewHolder(parent), LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(title: String) {
            itemView.tvTitle.text = title
        }
    }

    class EmptyViewHolder(parent: View, text: Int) : RecyclerView.ViewHolder(parent) {
        init {
            itemView.tvEmpty.setText(text)
        }
    }
}
