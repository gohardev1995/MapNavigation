package com.invotech.runshuffle.Activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.invotech.runshuffle.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class SourceInflatorActivity : AppCompatActivity() {

    private var Sourceview: View? = null
    private var DestinationView: View? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var SharedEditor: SharedPreferences.Editor
    private var myPreference = "myPrefs"
    private var email = "email"
    private var password = "password"
    private var customDialogDest: View? = null
    private var DestDialog: AlertDialog? = null
    private lateinit var optionDialog: AlertDialog
    private var poly = ArrayList<LatLng>()
    private val PERMISSION_CODE = 101
    private var onIndex: Int = 0
    private lateinit var locateUser: LatLng
    private lateinit var sourceLatLng: LatLng
    private lateinit var destinationLatLng: String
    private lateinit var currentPolyline: Polyline
    private lateinit var getLocation: LatLng
    private lateinit var myplace: Place
    private lateinit var polyline: Polyline
    private lateinit var myplace2: Place
    private lateinit var mMap: GoogleMap
    private lateinit var destination: LatLng
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private var source: LatLng? = null
    private var REQUEST_CODE = 101
    private lateinit var arr: ArrayList<String>
    var placeFields = Arrays.asList(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS,
        Place.Field.LAT_LNG
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source_inflator)
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_place) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(placeFields)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                Places.initialize(this@SourceInflatorActivity, getString(R.string.places_api))
                myplace = p0

                val queriedLocation = myplace.latLng
                source = LatLng(queriedLocation!!.latitude, queriedLocation.longitude)


                /*source = LatLng(queriedLocation!!.latitude, queriedLocation.longitude)

                */

                val intent = Intent(this@SourceInflatorActivity, MainActivity::class.java)
                intent.putExtra("lat", queriedLocation.latitude)
                intent.putExtra("lng", queriedLocation.longitude)
                startActivity(intent)


            }

            override fun onError(p0: Status) {

            }


        })


    }
}