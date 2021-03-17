package com.britishbroadcast.frinder.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.britishbroadcast.frinder.databinding.PlacesItemLayoutBinding
import com.britishbroadcast.frinder.model.data.HangoutPlace
import com.britishbroadcast.frinder.view.adapter.PlacesMarkerAdapter.*
import com.bumptech.glide.Glide

class PlacesMarkerAdapter(private var hangoutResultList: List<HangoutPlace>, private val placesMarkerDelegate: PlacesMarkerDelegate): RecyclerView.Adapter<PlacesMarkerAdapter.PlacesViewHolder>() {
    inner class PlacesViewHolder(val binding: PlacesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    interface PlacesMarkerDelegate {
        fun selectPlacesMarker(hangoutPlace: HangoutPlace)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val binding = PlacesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlacesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        with(holder) {
            with(hangoutResultList[position]) {
                binding.businessNameTextView.text = this.name
                binding.businessAddressTextView.text = this.vicinity
                Glide.with(binding.businessIconImageView)
                            .load(this.icon)
                            .into(binding.businessIconImageView)
                holder.itemView.setOnClickListener {
                    placesMarkerDelegate.selectPlacesMarker(this)
                }

            }
        }
    }

    //override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
       // with(holder) {
         //   with(resultList[position]) {
           //     binding.businessNameTextView.text = this.name
          //      binding.businessAddressTextView.text = this.vicinity
          //      Glide.with(binding.businessIconImageView)
            //            .load(this.icon)
            //            .into(binding.businessIconImageView)

          //      if (this.opening_hours != null) {
          //          open = if (!this.opening_hours.open_now) "OPEN" else "CLOSED"
          //      }
           //     binding.businessOpenTextView.text = open
           //     binding.businessRatingTextView.text = "Rating: {this.rating.toString()}"
          //  }
      //  }
 //   }

    override fun getItemCount(): Int = hangoutResultList.size

    fun updateDate(newList: List<HangoutPlace>) {
        hangoutResultList = newList
        notifyDataSetChanged()
    }
}
