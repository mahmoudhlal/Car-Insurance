package com.mahmoud.carsinsurance.fragment.maps


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mahmoud.carsinsurance.R
import com.mahmoud.carsinsurance.models.location.SharedViewModel
import kotlinx.android.synthetic.main.fragment_map.*
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment() , OnMapReadyCallback, LocationListener,
    View.OnClickListener {

    private var navController: NavController? = null
    private var mMap: GoogleMap? = null
    private var lat = 0.0
    private  var lng = 0.0
    private var mLocationManager: LocationManager? = null
    private var gpsCheck = false
    private val marker: MarkerOptions? = null
    private var locationViewModel: SharedViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationViewModel = ViewModelProvider(activity!!).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        var mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        if (mapFragment == null) {
            val fm = fragmentManager
            val ft = fm!!.beginTransaction()
            mapFragment = SupportMapFragment.newInstance()
            ft.replace(R.id.map, mapFragment).commit()
        }
        mapFragment!!.getMapAsync(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        btnLocation.setOnClickListener(this)
        //searchBar();
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mLocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        } else  /*if (lat != 0 && lng != 0)*/ {
            getCurrentLocation()
            val myLoc = LatLng(lat, lng)
            val cameraPosition = CameraPosition.Builder()
                .target(myLoc)
                .zoom(12f)
                .bearing(90f)
                .tilt(40f)
                .build()
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 10f))
            //mMap.addMarker(new MarkerOptions().position(myLoc));
        }
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap!!.isMyLocationEnabled = true
        mMap!!.setOnMyLocationButtonClickListener {
            if (ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@setOnMyLocationButtonClickListener true
            }
            getCurrentLocation()
            mMap!!.isMyLocationEnabled = true
            val myLoc = LatLng(lat, lng)
            val cameraPosition = CameraPosition.Builder()
                .target(myLoc)
                .zoom(12f)
                .bearing(90f)
                .tilt(40f)
                .build()
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 13f))
            true
        }
    }

    private fun buildAlertMessageNoGps(){
        val builder =
            AlertDialog.Builder(activity!!)
        builder.setMessage(getString(R.string.pleaseOpenGPS))
            .setCancelable(false).setPositiveButton(R.string.open){dialog, which ->
                run {
                    gpsCheck = true;
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }.setNegativeButton(R.string.close){dialog, which ->
                run {
                    dialog.cancel()
                }
            }
        val alert = builder.create()
        alert.show()
    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onResume() {
        super.onResume()
        if (gpsCheck) {
            mLocationManager =
                activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps()
            } else {
                getCurrentLocation()
                val myLoc = LatLng(lat, lng)
                val cameraPosition = CameraPosition.Builder()
                    .target(myLoc)
                    .zoom(12f)
                    .bearing(90f)
                    .tilt(40f)
                    .build()
                mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 10f))
                //mMap.addMarker(new MarkerOptions().position(myLoc));
            }
        }
    }


    private fun getLastKnownLocation(): Location? {
        val providers = mLocationManager!!.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return bestLocation
            }
            val l = mLocationManager!!.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }
        return bestLocation
    }

    private fun getAddress(lat: Double, lng: Double): String? {
        val geocoder: Geocoder
        val addresses: List<Address>
        val locale = Locale("en")
        geocoder = Geocoder(activity, locale)
        var address: String? = ""
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1)
            address = addresses[0].getAddressLine(0)
        } catch (e: IOException) {
            Toast.makeText(
                activity,
                getString(R.string.no_detected_address),
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IndexOutOfBoundsException) {
            Toast.makeText(
                activity,
                getString(R.string.no_detected_address),
                Toast.LENGTH_SHORT
            ).show()
        }
        return address
    }

    private fun mapClicked(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(12f)
            .bearing(90f)
            .build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
        /*marker = new MarkerOptions()
                .position(latLng).title((getAddress(latLng.latitude, latLng.longitude)));


        mMap.clear();
        mMap.addMarker(marker);
        */
        lat = latLng.latitude
        lng = latLng.longitude
    }


    private fun getCurrentLocation() {
        mLocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val location = getLastKnownLocation()
        if (location != null) {
            lat = location.latitude
            lng = location.longitude
        } else {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                100, 0f, this
            )
        }
    }

    override fun onLocationChanged(location: Location) {
        mLocationManager!!.removeUpdates(this)
        lat = location.latitude
        lng = location.longitude
    }

    override fun onStatusChanged(s: String?, i: Int, bundle: Bundle?) {}

    override fun onProviderEnabled(s: String?) {}

    override fun onProviderDisabled(s: String?) {}

    private fun searchBar() { /* if (!Places.isInitialized()) {
            Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        Objects.requireNonNull(autocompleteFragment).setPlaceFields(Arrays.asList(Place.Field.ADDRESS,
                Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mapClicked(place.getLatLng());
                Log.d("OPOP" , place.getName()+"\n"+place.getAddress()) ;
            }
            @Override
            public void onError(@NonNull Status status) {

            }
        });
*/
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btnLocation) { //if (lat == 0 && lng == 0)
            markerPicker()
            val l = com.mahmoud.carsinsurance.models.location.Location(getAddress(lat, lng), lat, lng)
            locationViewModel!!.locationLiveData.setValue(l)
            navController!!.navigateUp()
        }
    }

    private fun markerPicker() {
        val latLng = mMap!!.cameraPosition.target
        lat = latLng.latitude
        lng = latLng.longitude
    }

}
