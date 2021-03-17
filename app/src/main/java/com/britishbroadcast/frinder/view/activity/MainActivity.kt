package com.britishbroadcast.frinder.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.britishbroadcast.frinder.R
import com.britishbroadcast.frinder.model.data.HangoutPlace
import com.britishbroadcast.frinder.util.Type
import com.britishbroadcast.frinder.view.adapter.PlacesMarkerAdapter
import com.britishbroadcast.frinder.viewmodel.FrinderViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback, PlacesMarkerAdapter.PlacesMarkerDelegate {

    private val REQUEST_CODE = 100

    private lateinit var slideAnimation: Animation
    private lateinit var slideOutAnimation: Animation

    private lateinit var locationManager: LocationManager

    private lateinit var map: GoogleMap

    private var placesMarkerAdapter: PlacesMarkerAdapter = PlacesMarkerAdapter(mutableListOf(), this)

    private val viewModel by viewModels<FrinderViewModel>()

    private var locationString = "0,0"
    private var radius = 2000
    private var type: Type = Type.restaurant

    


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slideAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        slideOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        open_settings_button.setOnClickListener {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                val uri = Uri.fromParts("package", packageName, null)
                //package://com.britishbroadcast.frinder/permissions
                //package:com.britishbroadcast.frinder#1
                Log.d("TAG_X", uri.toString())
                it.data = uri
            })
        }

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.placeLiveData.observe(this, Observer{
            Log.d("TAG_X", "Results retrieved -> ${it.size}")

            updateMap(it)
        })

    }

    private fun updateMap(hangoutPlaces: List<HangoutPlace>) {
        hangoutPlaces.forEach {
            val loc = LatLng(it.geometry.location.lat, it.geometry.location.lng)
            map.addMarker(MarkerOptions()
                .position(loc).title(it.name))
            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10f))
        }
    }

    override fun onStart() {
        super.onStart()
        //1. Check if permission is granted>
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            hideOverlay()
            //this is already granted
            //Get location....
            registerLocationListener()
        } else {
            //not granted request permission
            requestLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun registerLocationListener() {

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000L,
                100f,
                this
        )

    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //this is already granted
                //Get location....
                registerLocationListener()
            } else { //Permission not granted - request again
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        )
                )
                    requestLocationPermission()
                else//user set the settings to 'Do not ask again'
                    showOverLay()
            }
        }
    }

    private fun hideOverlay() {
        overlay_view.animation = slideOutAnimation
        overlay_view.visibility = View.GONE
    }

    private fun showOverLay() {
        overlay_view.visibility = View.VISIBLE
        overlay_view.animation = slideAnimation
    }

    override fun onLocationChanged(location: Location) {
        Log.d("TAG_X", "Location is ready....")

        if (this::map.isInitialized) {
            val myLocation = LatLng(location.latitude, location.longitude)
            map.addMarker(MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon))
                    .position(myLocation).title("I'm Waldo"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10f))

            locationString = "${myLocation.latitude},${myLocation.longitude}"
            viewModel.findHangoutPlace(locationString, radius, type.name)
        }

    }

    @SuppressLint("RestrictedApi")
    fun onMenuClick(view: View){
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        view.startAnimation(animation)

        val menu = PopupMenu(this, view)
        menu.inflate(R.menu.main_menu)

        menu.setOnMenuItemClickListener {
            type = when(it.itemId){
                R.id.drinks_item -> {
                    //Get drink locations
                    Type.bar
                }
                R.id.food_item -> {
                    //Get restaurant locations
                    Type.restaurant
                }
                R.id.soccer_item -> {
                    //Get soccer locations
                    //stadium -> ground
                    Type.stadium
                }
                else -> {
                    //Get movie theater locations
                    Type.movie_theater
                }
            }
            //map.clear()//->adding items to map
            viewModel.findHangoutPlace(locationString, radius, type.name)
            true
        }

        //menu.show()
        val m = MenuPopupHelper(this, menu.menu as MenuBuilder, view )
        m.setForceShowIcon(true)
        m.show()

    }

    override fun selectPlacesMarker(hangoutPlace: HangoutPlace) {
        val latLng = LatLng(hangoutPlace.geometry.location.lat, hangoutPlace.geometry.location.lng)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
    }

    override fun onMapReady(map: GoogleMap) {
        Log.d("TAG_X", "Map is ready....")
        this.map = map
    }
}