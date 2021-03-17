package com.britishbroadcast.frinder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.britishbroadcast.frinder.R
import com.britishbroadcast.frinder.viewmodel.FrinderViewModel
import kotlinx.android.synthetic.main.map_places_marker_item_layout.business_name_textView
import kotlinx.android.synthetic.main.places_item_layout.*

class PlacesMarkerFragment: Fragment() {
    private val frinderViewModel by activityViewModels<FrinderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceStae: Bundle?
        ): View? {
        return inflater.inflate(R.layout.map_places_marker_item_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = arguments
        if(bundle != null) {
            var place_title: String = bundle.getString("KEY").toString()
            frinderViewModel.placeLiveData.value?.forEach {
                if(it.name == place_title) {
                    business_name_textView.text = it.name
                    business_address_textView.text = it.vicinity
                }
            }
        }
    }
}