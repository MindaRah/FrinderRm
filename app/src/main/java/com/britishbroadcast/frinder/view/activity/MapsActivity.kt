package com.britishbroadcast.frinder.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.britishbroadcast.frinder.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(map: GoogleMap) {
        this.map = map
        val sydney = LatLng(-34.0, 151.0)
        this.map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        this.map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}