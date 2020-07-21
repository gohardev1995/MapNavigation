package com.invotech.runshuffle.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.invotech.runshuffle.Interfaces.Routes
import com.invotech.runshuffle.Models.RegisterUser
import com.invotech.runshuffle.Models.RoundMaps
import com.invotech.runshuffle.Object.APIRound
import com.invotech.runshuffle.Object.SaveSharedPreference
import com.invotech.runshuffle.R
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Response

import retrofit2.Callback
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnMapReadyCallback,View.OnClickListener {

    //----------------------------------------Assigning Variables---------------------------------//
    private lateinit var customDialogDest: View
    private lateinit var optionDialog: AlertDialog
    private var poly = ArrayList<LatLng>()
    private val PERMISSION_CODE = 101
    private var onIndex: Int = 0
    private lateinit var destinationLatLng: String
    private lateinit var getLocation: LatLng
    private lateinit var myplace: Place
    private lateinit var myplace2: Place
    private lateinit var mMap: GoogleMap
    private lateinit var destination: LatLng
    private var SOURCE_PLACE = 1000
    private var DEST_PLACE = 10001
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
    private var destMarker : Marker? = null
    private var sourceMarker : Marker? = null
    private lateinit var currentMarker : Marker

    //----------------------------------------------------------//
    var mGoogleMap: GoogleMap? = null
    var mapFrag: SupportMapFragment? = null
    var mLocationRequest: LocationRequest? = null
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var mapFragment : SupportMapFragment
    //----------------------------------------------------------//

    //-----------------------------------</Assigning Variables/>----------------------------------//
    @SuppressLint("ResourceType", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
       getCurrentLocation()



        arr = ArrayList<String>()
        arr.add("walking")
        arr.add("driving")

        btn_round_routes.setOnClickListener(this)
        btn_clear_all.setOnClickListener(this)
        btn_normal.setOnClickListener(this)

        mapFrag = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFrag?.getMapAsync(this)
//edt_destination
        edt_source.setOnClickListener {
            val intent: Intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, placeFields
            )
                .build(this)
            startActivityForResult(intent, SOURCE_PLACE)



        }





        txt_get_route.setOnClickListener {
            val dialog =
                ProgressDialog.show(
                    this@MainActivity, "",
                    "Loading. Please wait...", true
                )
             val buttonTimer = Timer()
            val progressDialog =
                ProgressDialog(this@MainActivity)
            if (TextUtils.isEmpty(edt_destination.text.toString())) {
                dialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Please Add Destination Place First",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                if (source == null && destination.toString().isNotEmpty()) {

                    if (onIndex == 0) {
                        /*calculateRoute(getLocation, destination, arr[0])*/
                        mMap.addMarker(MarkerOptions().position(getLocation))
                        mMap.addMarker(MarkerOptions().position(destination))
                        onIndex = 1
                        /*txt_get_route.setText("search")*/
                    } else {

                        /*calculateRoute(getLocation, destination, arr[1])*/
                        mMap.addMarker(MarkerOptions().position(getLocation))
                        mMap.addMarker(MarkerOptions().position(destination))

                        onIndex = 0

                    }


                    /*mMap.addMarker(MarkerOptions().position(getLocation))*/
                    buttonTimer.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {



                                calculateRoute(getLocation, destination, arr[onIndex])

                                mMap.addMarker(MarkerOptions().position(getLocation))
                                mMap.addMarker(MarkerOptions().position(destination))


                            }
                            dialog.dismiss();
                        }
                    }, 4000)
                    /*
                 progressDialog.dismiss()*/

                } else {
                    if (onIndex == 0) {

                        mMap.addMarker(MarkerOptions().position(source!!))
                        mMap.addMarker(MarkerOptions().position(destination))

                        onIndex = 1

                    } else {

                        mMap.addMarker(MarkerOptions().position(source!!))
                        mMap.addMarker(MarkerOptions().position(destination))

                        onIndex = 0

                    }
                    buttonTimer.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {




                                calculateRoute(source!!, destination, arr[onIndex])
                                mMap.addMarker(MarkerOptions().position(source!!))
                                mMap.addMarker(MarkerOptions().position(destination))


                            }
                            dialog.dismiss();
                        }
                    }, 4000)

                    /*return calculateRoute(source!!,destination,"driving")*/
                }

            }

        }

        edt_destination.setOnClickListener {

            val intent: Intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, placeFields
            )
                .build(this)
            startActivityForResult(intent, DEST_PLACE)
        }





        txt_logout.setOnClickListener {


            SaveSharedPreference.setLoggedIn(
                applicationContext,
                false


            )
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))


        }

        //----------------------------Google Place API KEY----------------------------------------//
        Places.initialize(this, getString(R.string.places_api))
        //---------------------------</Google Place API KEY/>-------------------------------------//
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 120000 // two minute interval
        mLocationRequest!!.fastestInterval = 120000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback,
                    Looper.myLooper())
                mMap.isMyLocationEnabled = true
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback,
                Looper.myLooper())
            mMap.isMyLocationEnabled = true
        }
       /* getCurrentLocation()*/

        mMap.setOnMapClickListener { dest ->
           /* val markerOptions = MarkerOptions()

            mMap.clear()*/
            destination = dest
            edt_destination.setText(dest.latitude.toString() + " , " + dest.longitude)
            edt_destination.textSize = 10f

            /* edt_destination.setText(dest.latitude.toString(),dest.longitude)*/
            if (source == null && destination.toString().isNotEmpty()) {

                calculateRoute(getLocation, destination, "walking")
                mMap.addMarker(MarkerOptions().position(getLocation))
                val markerOptions = MarkerOptions()
                markerOptions.position(destination)
                destMarker =  mMap.addMarker(markerOptions)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 15f))


            } else {
                calculateRoute(source!!, destination, "walking")
                mMap.addMarker(MarkerOptions().position(source!!))
                mMap.addMarker(MarkerOptions().position(destination))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 15f))

            }

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
            AlertDialog.Builder(this)
                .setTitle("Location Permission Needed")
                .setMessage("This app needs the Location permission, please accept to use location functionality")
                .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface?, i: Int) {
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_CODE
                        )
                    }
                })
                .create()
                .show()



        }



        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { locateUser ->
            getLocation = LatLng(locateUser?.latitude!!, locateUser.longitude)
            currentMarker =  mMap.addMarker(MarkerOptions().position(getLocation))

            /*
            /*edt_source.setText("My Location")*/
             */
            /*edt_source.setText(locateUser.latitude.toString() + " , " + locateUser.longitude)*/
            /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocation, 10f))*/
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getLocation, 15f))


        }


    }

    //--------------------------------</Function to get Current Location/>------------------------//
    //------------------------------------Function to Get Source----------------------------------//

    private fun getsourceLocation() {

        autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_place) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(placeFields)

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {

                myplace = p0

                val queriedLocation = myplace.latLng
                mMap.clear()
                source = LatLng(queriedLocation!!.latitude, queriedLocation.longitude)
                edt_source.visibility = View.VISIBLE
                edt_source.setText(queriedLocation.latitude.toString() + " , " + queriedLocation.longitude)
                mMap.addMarker(MarkerOptions().position(source!!).title("Source"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 15f))




                mMap.addMarker(MarkerOptions().position(source!!).title("Marker in Source"))
                edt_source.visibility = View.VISIBLE

                /*Sourceview!!.visibility = GONE
                sourceLinear.visibility = VISIBLE*/

            }

            override fun onError(p0: Status) {

            }


        })


    }

    //-----------------------------------<Function to Get Source>---------------------------------//
    //----------------------------------- Function to Get Destination-----------------------------//
    fun getDestinationLocation(view: View) {
        edt_destination.text = null
        mMap.clear()
        if (source == null && destination.toString().isNotEmpty())
        {
            mMap.addMarker(MarkerOptions().position(getLocation))
        }
        else
        mMap.addMarker(MarkerOptions().position(source!!))

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

       createRoute()


    }

    private fun createRoute() {
        val progress = ProgressDialog(this)
        progress.setMessage("Creating Alternate Route");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        if (TextUtils.isEmpty(edt_destination.text.toString())) {
            Toast.makeText(
                applicationContext,
                "Please Add Destination Place First",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            if (source == null && destination.toString().isNotEmpty()) {

                if (onIndex == 0) {
                    /*calculateRoute(getLocation, destination, arr[0])*/
                    mMap.addMarker(MarkerOptions().position(getLocation))
                    mMap.addMarker(MarkerOptions().position(destination))
                    onIndex = 1
                    /*txt_get_route.setText("search")*/
                } else {

                    /*calculateRoute(getLocation, destination, arr[1])*/
                    mMap.addMarker(MarkerOptions().position(getLocation))
                    mMap.addMarker(MarkerOptions().position(destination))

                    onIndex = 0

                }


                calculateRoute(getLocation, destination, arr[onIndex])

                mMap.addMarker(MarkerOptions().position(getLocation))
                mMap.addMarker(MarkerOptions().position(destination))


            } else {
                if (onIndex == 0) {

                    mMap.addMarker(MarkerOptions().position(source!!))
                    mMap.addMarker(MarkerOptions().position(destination))

                    onIndex = 1

                } else {

                    mMap.addMarker(MarkerOptions().position(source!!))
                    mMap.addMarker(MarkerOptions().position(destination))

                    onIndex = 0

                }
                calculateRoute(source!!, destination, arr[onIndex])
                mMap.addMarker(MarkerOptions().position(source!!))
                mMap.addMarker(MarkerOptions().position(destination))
                /*return calculateRoute(source!!,destination,"driving")*/
            }

        }

    }


    private fun calculateRoute(origin: LatLng, dest: LatLng, mode: String) {

        mMap.clear()
        val URL = getDirectionURL(origin, dest, mode)
        Log.d("GoogleMap", "URL : $URL")
        GetDirection(URL).execute()
        mMap.clear()


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


    fun getDirectionURL(source: LatLng, destination: LatLng, mode: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${source.latitude},${source.longitude}&destination=${destination.latitude},${destination.longitude}&sensor=false&mode=${mode}&maptype=normal,+CA&key=AIzaSyCn_Rpi7ierJ-yxJS-xCGSMfAjSfoz7_u4"
        /*"https://maps.googleapis.com/maps/api/directions/json?origin=${source.latitude},${source.longitude}&destination=${destination.latitude},${destination.longitude}&sensor=false&mode=driving"*/
    }
    fun getRoundURL(source: LatLng, length : Int): String {
        return "https://maps.openrouteservice.org/directions?n1={${source.latitude}}&n2={${source.longitude}}&n3=17&a={${source.latitude},${source.longitude}}&b=0&r1={$length}&c=0&k1=en-US&k2=5b3ce3597851110001cf6248bda92fbd5b334daca44487ed1f86c865"

    }




    fun getSourceLocation(view: View) {
        mMap.clear()
        getCurrentLocation()
        edt_source.setText("My Location")


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SOURCE_PLACE && resultCode == Activity.RESULT_OK ) {
            mMap.clear()
            edt_destination.setText("")
            val sourceplace = Autocomplete.getPlaceFromIntent(data!!)
            val queriedLocation = sourceplace.latLng
            source = LatLng(queriedLocation!!.latitude, queriedLocation.longitude)
            edt_source.setText(queriedLocation.latitude.toString() + " , " + queriedLocation.longitude.toString())
            edt_source.textSize = 10f
            currentMarker.remove()
            val markerOptions = MarkerOptions()
            if(sourceMarker != null)
            {
                sourceMarker!!.remove()
            }
            if (source == null && destination.toString().isNotEmpty())
            {
                markerOptions.position(getLocation)
            }
            else
            {
                markerOptions.position(source!!)

            }


            sourceMarker =  mMap.addMarker(markerOptions)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source!!,15f))




        }
        else if (requestCode == DEST_PLACE && resultCode == Activity.RESULT_OK)
        {

            mMap.clear()

            val destplace = Autocomplete.getPlaceFromIntent(data!!)
            val queriedLocation = destplace.latLng
            destination = LatLng(queriedLocation!!.latitude, queriedLocation.longitude)
            edt_destination.setText(queriedLocation.latitude.toString() + " , " + queriedLocation.longitude)
            edt_destination.textSize = 10f
            btn_destination_location.isEnabled = true

            val markerOptions = MarkerOptions()
            if(destMarker != null)
            {

                destMarker!!.remove()
            }
            markerOptions.position(destination)
            mMap.addMarker(markerOptions)
            if (source == null && destination.toString().isNotEmpty())
            {
                mMap.addMarker(MarkerOptions().position(getLocation))

            }
            else
                mMap.addMarker(MarkerOptions().position(source!!))






            // do query with address
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            val status =
                Autocomplete.getStatusFromIntent(data!!)

            Toast.makeText(
                this@MainActivity,
                "Error: " + status.statusMessage,
                Toast.LENGTH_LONG
            ).show()
            Log.i("onActivityResult", status.statusMessage)
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }

    fun clearAll(view: View) {
        edt_source.setText(null)
        edt_destination.setText(null)
        mMap.clear()
    }
    var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                //The last location in the list is the newest
                val location = locationList[locationList.size - 1]
                mLastLocation = location
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker!!.remove()
                }


            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK") { dialogInterface, i -> //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_CODE)
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient!!.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper())
                        mMap.isMyLocationEnabled = true
                    }
                } else {

                    // if not allow a permission, the application will exit

                }
            }
        }
    }


    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.btn_clear_all -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_clear_all.background = getDrawable(R.drawable.pressed_button)
                    btn_round_routes.background = getDrawable(R.drawable.custom_btn_2)
                    btn_normal.background = getDrawable(R.drawable.custom_btn_2)

                }

                edt_round_route.visibility = GONE
                edt_destination.visibility = VISIBLE
                img_round.visibility = GONE
                img2.visibility = VISIBLE
                edt_destination.text = ""
                edt_destination.setHint("")
                edt_source.text = null
                mMap.clear()

            }
            R.id.btn_round_routes -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_round_routes.background = getDrawable(R.drawable.pressed_button)
                    btn_clear_all.background = getDrawable(R.drawable.custom_btn_2)
                    btn_normal.background = getDrawable(R.drawable.custom_btn_2)

                }

                edt_destination.setText("")
                edt_source.setText("My Location")
                edt_round_route.visibility = VISIBLE
                edt_destination.visibility = GONE
                img2.visibility = GONE
                img_round.visibility = VISIBLE
                mMap.clear()
                getCurrentLocation()
                mMap.setOnMapClickListener(null)
                edt_round_route.setHint("Please Enter Length")
                txt_get_route.setOnClickListener{
                   val OpenRoute = APIRound.client.create(Routes::class.java)
                    


                }



            }
            R.id.btn_normal -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_normal.background = getDrawable(R.drawable.pressed_button)
                    btn_clear_all.background = getDrawable(R.drawable.custom_btn_2)
                    btn_round_routes.background = getDrawable(R.drawable.custom_btn_2)

                }

                edt_round_route.visibility = GONE
                edt_destination.visibility = VISIBLE
                edt_destination.setHint("Please Enter Destionation")
                edt_destination.setText("")
                edt_source.setText("My Location")
                img_round.visibility = GONE
                img2.visibility = VISIBLE
                mMap.clear()

                getCurrentLocation()




                }
            }
        }
    }



