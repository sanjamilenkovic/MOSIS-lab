package rs.elfak.mosis.observers.myplaces.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import rs.elfak.mosis.observers.myplaces.R
import rs.elfak.mosis.observers.myplaces.data.MyPlace
import rs.elfak.mosis.observers.myplaces.data.MyPlacesData
import java.util.*

class MyPlacesMapsActivity : AppCompatActivity() {

    lateinit var map: MapView
    lateinit var mapController: IMapController

    private var NEW_PLACE: Int = 1
    private var PERMISSION_ACCESS_FINE_LOCATION: Int = 1

    companion object {
        var SHOW_MAP: Int = 0
        var CENTER_PLACE_ON_MAP = 1
        var SELECT_COORDINATES = 2
    }

    private var state: Int = 0
    private var setCoorsEnabled: Boolean = false
    private lateinit var placeLoc: GeoPoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            var mapIntent = intent
            var mapBundle = mapIntent.extras
            if (mapBundle != null) {
                state = mapBundle.getInt("state")
                if (state == CENTER_PLACE_ON_MAP) {
                    var placeLat = mapBundle.getString("lat")
                    var placeLon = mapBundle.getString("lon")

                    placeLoc = GeoPoint(placeLat!!.toDouble(), placeLon!!.toDouble())
                }
            }
        } catch (e: Exception) {
            Log.d("Error", "Error reading state")
        }


        setContentView(R.layout.activity_my_places_maps)
        setSupportActionBar(findViewById(R.id.toolbar))

        var fab = findViewById<FloatingActionButton>(R.id.fab)

        if (state != SELECT_COORDINATES) {
            fab.setOnClickListener {
                val i = Intent(this, EditMyPlaceActivity::class.java)
                startActivityForResult(i, NEW_PLACE)
            }

        }
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        map = findViewById(R.id.map)
        map.setMultiTouchControls(true)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ACCESS_FINE_LOCATION
            )
        } else {
            setupMap()
        }


//        mapController = map.controller
//        mapController.setZoom(15.0)
//        val startPoint = GeoPoint(43.3209, 21.8958)
//        mapController.setCenter(startPoint)

    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if (state == SELECT_COORDINATES && setCoorsEnabled) {

            menu!!.add(0, 1, 1, "Select coordinates")
            menu!!.add(0, 2, 2, "Cancel")

            menu.getItem(0).setIcon(R.drawable.ic_action_set_coordinates)
            menu.getItem(0).icon.alpha = 255
            menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            return super.onCreateOptionsMenu(menu)

        } else {
            menuInflater.inflate(R.menu.menu_my_places_maps, menu)
            return true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (state == SELECT_COORDINATES && setCoorsEnabled) {
            if (item.itemId == 1) {
                setCoorsEnabled = true
                item.isEnabled = false
                item.icon.alpha = 64
                setOnMapClickOverlay()
                Toast.makeText(this, "Select coordinates!", Toast.LENGTH_SHORT).show()
            } else
                if (item.itemId == 2) {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
        } else {
            when (item.itemId) {
                R.id.new_place_item -> {
                    val intent = Intent(this, EditMyPlaceActivity::class.java)
                    startActivityForResult(intent, 1)
                }
                R.id.about_item -> {
                    val intent = Intent(this, About::class.java)
                    startActivity(intent)
                }

                android.R.id.home -> finish()
            }

        }


        return super.onOptionsItemSelected(item)
    }


    private fun setMyLocationOverlay() {
        val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)

        mapController = map.controller
        mapController.setZoom(15.0)
        myLocationOverlay.enableFollowLocation()

        var current = ResourcesCompat.getDrawable(resources, R.drawable.pin, null)
        var currentIcon: Bitmap? = null

        if (current != null)
            currentIcon = current.toBitmap()

        myLocationOverlay.setDirectionArrow(currentIcon, currentIcon)
        myLocationOverlay.setPersonIcon(currentIcon)

    }

    fun setCenterPlaceOnMap() {
        mapController = map.controller

        mapController.setZoom(15.0)
        mapController.animateTo(placeLoc)

    }

    fun setupMap() {
        when (state) {
            SHOW_MAP -> {
                setMyLocationOverlay()
            }
            SELECT_COORDINATES -> {

                mapController = map.controller
                if (mapController != null) {
                    mapController.setZoom(15.0)
                    val startPoint = GeoPoint(43.3209, 21.8958)
                    mapController.setCenter(startPoint)
                }
                setCoorsEnabled = true
                //setOnMapClickOverlay()
            }

            else -> setCenterPlaceOnMap()
        }
    }

    private fun setOnMapClickOverlay() {
        val mReceive: MapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                if (state == SELECT_COORDINATES && setCoorsEnabled) {
                    val lon: String = p.longitude.toString()
                    val lat: String = p.latitude.toString()
                    val locationIntent = Intent()
                    locationIntent.putExtra("lon", lon)
                    locationIntent.putExtra("lat", lat)
                    setResult(Activity.RESULT_OK, locationIntent)
                    finish()

                }
                return false
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }
        }

        val overlaysEvent = MapEventsOverlay(mReceive)
        map.overlays.add(overlaysEvent)


    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_ACCESS_FINE_LOCATION -> {
                if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    setMyLocationOverlay()
                setupMap()
            }
        }
    }


}