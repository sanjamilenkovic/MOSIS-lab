package rs.elfak.mosis.observers.myplaces.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import rs.elfak.mosis.observers.myplaces.About
import rs.elfak.mosis.observers.myplaces.R

class MyPlacesMapsActivity : AppCompatActivity() {

    lateinit var map: MapView
    lateinit var mapController: IMapController

    var NEW_PLACE: Int = 1
    var PERMISSION_ACCESS_FINE_LOCATION: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_places_maps)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            var i = Intent(this, EditMyPlaceActivity::class.java)
            startActivityForResult(i, NEW_PLACE);
        }
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        map = findViewById(R.id.map)

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
        } else
        {
            setMyLocationOverlay()
            setOnMapClickOverlay()
        }

        mapController = map.controller
        mapController.setZoom(15.0)
        var startPoint = GeoPoint(43.3209, 21.8958)
        mapController.setCenter(startPoint)

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
        menuInflater.inflate(R.menu.menu_my_places_maps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.new_place_item -> {
                var intent = Intent(this, EditMyPlaceActivity::class.java)
                startActivityForResult(intent, 1)
            }
            R.id.about_item -> {
                var intent = Intent(this, About::class.java)
                startActivity(intent)
            }
            R.id.about_item -> {
                Toast.makeText(this, "About!", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, About::class.java)
                startActivity(intent)
            }
            android.R.id.home -> finish()


        }

        return super.onOptionsItemSelected(item)
    }

    fun setMyLocationOverlay() {
        var myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)
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
            }
        }
    }

    fun setOnMapClickOverlay()
    {
        var mReceive: MapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                var lon : String = p.longitude.toString()
                var lat : String = p.latitude.toString()
                var locationIntent = Intent()
                locationIntent.putExtra("lon", lon)
                locationIntent.putExtra("lat", lat)
                setResult(Activity.RESULT_OK, locationIntent)
                finish()
                return false
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }
        }

        var overlaysEvent =  MapEventsOverlay(mReceive)
        map.overlays.add(overlaysEvent)



    }


}