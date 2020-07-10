package com.invotech.runshuffle.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.Gson
import com.invotech.runshuffle.Object.SaveSharedPreference
import com.invotech.runshuffle.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    //----------------------------------------Assigning Variables---------------------------------//

    private var poly = ArrayList<LatLng>()
    private val PERMISSION_CODE = 99

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
    private var source: LatLng? = null
    private var REQUEST_CODE = 101
    private var PREFS_NAME = "PrefsFile"

    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    var placeFields = Arrays.asList(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS,
        Place.Field.LAT_LNG
    )


    //-----------------------------------</Assigning Variables/>----------------------------------//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*arrLatlng.add(source)*/
        /*getCurrentLocation()*/

        /*checkPermission()*/
        /*checkPermission()*/


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)




        txt_logout.setOnClickListener(View.OnClickListener {
            SaveSharedPreference.setLoggedIn(
                getApplicationContext(),
                false,
                "",""


            )
            startActivity(Intent(this, LoginActivity::class.java))
        })
        /*new FetchURL(MapActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
        */
        //----------------------------Google Place API KEY----------------------------------------//
        Places.initialize(this, getString(R.string.places_api))
        //---------------------------</Google Place API KEY/>-------------------------------------//
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
         getCurrentLocation()



        mMap.setOnMapClickListener { dest ->


            destination = dest
            edt_destination.setText(dest.latitude.toString() + " , " + dest.longitude)
            /* edt_destination.setText(dest.latitude.toString(),dest.longitude)*/
            if (source == null && destination.toString().isNotEmpty()) {

                calculateRoute(getLocation, destination, "walking")
                mMap.addMarker(MarkerOptions().position(getLocation))
                mMap.addMarker(MarkerOptions().position(destination))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 15f))


            } else {
                calculateRoute(source!!, destination, "walking")
                mMap.addMarker(MarkerOptions().position(source!!))
                mMap.addMarker(MarkerOptions().position(destination))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 15f))

            }
            /* calculateRoute(source!!, destination, "driving")*/
            /*if ( source.toString().isEmpty())
            {
                calculateRoute(getLocation,destination,"walking")
                mMap.clear()

                *//*mMap.addMarker(MarkerOptions().position(source!!))*//*
                mMap.addMarker(MarkerOptions().position(getLocation))

                mMap.addMarker(MarkerOptions().position(destination))

                *//*edt_source_location.setText(queriedLocation.latitude.toString() + " , " + queriedLocation.longitude.toString())*//*
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 15f))
            }
            else
            {
                calculateRoute(source!!,destination,"walking")
                mMap.addMarker(MarkerOptions().position(source!!))
                mMap.addMarker(MarkerOptions().position(destination))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination,15f))
            }
        */


        }


    }

    //----------------------------------Function to get Current Location--------------------------//
    private fun getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_CODE
            )
        }
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return

        }


        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { locateUser ->
            getLocation = LatLng(locateUser?.latitude!!, locateUser.longitude)
            mMap.addMarker(MarkerOptions().position(getLocation))
            edt_source.setText("My Location")
            /*edt_source.setText(locateUser.latitude.toString() + " , " + locateUser.longitude)*/
            /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocation, 10f))*/
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLocation, 15f))


        }

        /*val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { locateUser ->
            mMap.clear()
            val location1 = LatLng(locateUser.longitude,locateUser.latitude)
            val location2 = LatLng(13.0253215,78.11)
            val getLocation = LatLng(location1.latitude,location1.longitude)
            *//*mMap.addCircle(CircleOptions().center(getLocation).radius(500.0).fillColor(Color.rgb(234,241,242)).strokeColor(Color.rgb(234,241,242)))*//*
        }
*/
    }

    //--------------------------------</Function to get Current Location/>------------------------//
    //----------------------------------- Function to Get Source----------------------------------//
    private fun getsourceLocation() {

        val optionDialog = AlertDialog.Builder(this@MainActivity).create()

        val customDialog = layoutInflater.inflate(R.layout.custom_location, null)
        autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_place) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(placeFields)
        /*autocompleteFragment.setText("Name")*/
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                myplace = p0


                val queriedLocation = myplace.latLng
                mMap.clear()
                source = LatLng(queriedLocation!!.latitude, queriedLocation.longitude)
                edt_source.setText(queriedLocation.latitude.toString() + " , " + queriedLocation.longitude)
                mMap.addMarker(MarkerOptions().position(source!!).title("Source"))
                /* edt_source_location.setText(queriedLocation.latitude.toString() + " , " + queriedLocation.longitude.toString())*/
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 15f))
                optionDialog.dismiss()


            }

            override fun onError(p0: Status) {

            }

        })
        optionDialog.setView(customDialog)
        optionDialog.show()

    }


    fun getSourceLocation(view: View) {
        mMap.clear()

        /*getCurrentLocation()*/


        /*getsourceLocation()*/
    }

    //-----------------------------------<Function to Get Source>---------------------------------//
    //----------------------------------- Function to Get Destination-----------------------------//
    fun getDestinationLocation(view: View) {
        edt_destination.text = null


    }

    private fun destinationLocation() {

        val DestDialog: AlertDialog = AlertDialog.Builder(this@MainActivity).create()

        val customDialog = layoutInflater.inflate(R.layout.custom_destination, null)
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_place1) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(placeFields)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                myplace2 = p0
                val queriedLocation = myplace2.latLng

                destinationLatLng = p0.latLng.toString()
                edt_destination.setText(queriedLocation?.latitude.toString() + " , " + queriedLocation?.longitude)

                destination = LatLng(queriedLocation!!.latitude, queriedLocation.longitude)

                mMap.addMarker(MarkerOptions().position(destination).title("Marker in Destination"))
                DestDialog.dismiss()
                /*edt_destination_location.setText(queriedLocation.latitude.toString() + " , " + queriedLocation.longitude.toString())*/


            }

            override fun onError(p0: Status) {

            }

        })
        DestDialog.setView(customDialog)
        DestDialog.show()

    }

    //-----------------------------------</Function to Get Destination/>--------------------------//
    //----------------------------------- Function to Hide Keyboard-------------------------------//
    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun getCloseKeyboard(view: View) {
        closeKeyboard()
    }
    //-----------------------------------</Function to Hide Keyboard/>----------------------------//

    //--------------------Function to Create Routes between Source and Destination----------------//
    fun getRoute(view: View) {
        if (TextUtils.isEmpty(edt_destination.text.toString())) {
            Toast.makeText(
                applicationContext,
                "Please Add Destination Place First",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            if (source == null && destination.toString().isNotEmpty()) {

                if(txt_get_route.text == "Search")
                {
                    calculateRoute(getLocation, destination, "driving")
                    mMap.addMarker(MarkerOptions().position(getLocation))
                    mMap.addMarker(MarkerOptions().position(destination))
                    txt_get_route.setText("search")
                }
                else {
                    calculateRoute(getLocation, destination, "walking")
                    mMap.addMarker(MarkerOptions().position(getLocation))
                    mMap.addMarker(MarkerOptions().position(destination))
                    txt_get_route.setText("Search")
                }

            } else {
                if (txt_get_route.text == "Search")
                {
                calculateRoute(source!!, destination, "driving")
                mMap.addMarker(MarkerOptions().position(source!!))
                mMap.addMarker(MarkerOptions().position(destination))
                    txt_get_route.setText("search")
                }
                else
                {
                    calculateRoute(source!!, destination, "walking")
                    mMap.addMarker(MarkerOptions().position(source!!))
                    mMap.addMarker(MarkerOptions().position(destination))
                    txt_get_route.setText("Search")


                }

                /*return calculateRoute(source!!,destination,"driving")*/
            }
        }


    }


    private fun calculateRoute(origin: LatLng, dest: LatLng, mode: String) {

        mMap.clear()
        val URL = getDirectionURL(origin, dest, mode)
        Log.d("GoogleMap", "URL : $URL")
        GetDirection(URL).execute()


    }


    //-------------------</Function to Create Routes between Source and Destination/>-------------//
    //----------------All Funtions used to Create Routes between Source and Destination-----------//
    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url: String) :
        AsyncTask<Void, Void, List<List<LatLng>>>() {


        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body()!!.string()
            Log.d("GoogleMap", " data : $data")
            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, GoogleMapDTO::class.java)

                val path = ArrayList<LatLng>()

                for (i in 0..(respObj.routes[0].legs[0].steps.size - 1)) {
                    /*val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble()
                            ,respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
                    path.add(startLatLng)
                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble()
                            ,respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
*/
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))


                }

                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result

        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices) {
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)

            }
            mMap.addPolyline(lineoption)
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {

        poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly

    }

    fun clearEverything(view: View) {
        mMap.clear()
        edt_source.text = null
        edt_destination.text = null


    }


    fun getDirectionURL(source: LatLng, destination: LatLng, mode: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${source.latitude},${source.longitude}&destination=${destination.latitude},${destination.longitude}&sensor=false&mode=${mode}&maptype=normal,+CA&key=AIzaSyCn_Rpi7ierJ-yxJS-xCGSMfAjSfoz7_u4"
        /*"https://maps.googleapis.com/maps/api/directions/json?origin=${source.latitude},${source.longitude}&destination=${destination.latitude},${destination.longitude}&sensor=false&mode=driving"*/
    }

    fun getSourceInflate(view: View) {
        getsourceLocation()

    }

    fun getDestinationInflator(view: View) {

        destinationLocation()
    }


}