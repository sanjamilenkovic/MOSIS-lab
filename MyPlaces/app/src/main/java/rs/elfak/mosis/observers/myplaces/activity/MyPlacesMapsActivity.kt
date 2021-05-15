package rs.elfak.mosis.observers.myplaces.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import rs.elfak.mosis.observers.myplaces.About
import rs.elfak.mosis.observers.myplaces.MyPlacesList
import rs.elfak.mosis.observers.myplaces.R

class MyPlacesMapsActivity : AppCompatActivity() {

    lateinit var map : MapView
    lateinit var mapController : IMapController

    var NEW_PLACE : Int = 1

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
}