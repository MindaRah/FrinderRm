package com.britishbroadcast.frinder.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.britishbroadcast.frinder.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log

class MainActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback {

    private val REQUEST_CODE = 100

    private lateinit var slideAnimation: Animation
    private lateinit var slideOutAnimation: Animation

    private lateinit var locationManager: LocationManager

    private lateinit var map: GoogleMap

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

//            map.addCircle(
//                    CircleOptions()
//                            .center(myLocation)
//                            .radius(1000.00)
//                            .strokeColor(R.color.black)
//                            .strokeWidth(2f)
//                            .fillColor(R.color.alpha_blue))
        }

    }

    fun onMenuClick(view: View){
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        view.startAnimation(animation)

        val menu = PopupMenu(this, view)
        menu.inflate(R.menu.main_menu)

//        val mInflater = menu.menuInflater
//        mInflater.inflate(R.menu.main_menu, menu.menu)

        menu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.drinks_item -> {
                    //Get drink locations
                }
                R.id.food_item -> {
                    //Get restaurant locations
                }
                R.id.soccer_item -> {
                    //Get soccer locations
                }
                else -> {
                    //Get movie theater locations
                }
            }
            true
        }

        menu.show()


    }

    override fun onMapReady(map: GoogleMap) {
        Log.d("TAG_X", "Map is ready....")
        this.map = map
    }
}