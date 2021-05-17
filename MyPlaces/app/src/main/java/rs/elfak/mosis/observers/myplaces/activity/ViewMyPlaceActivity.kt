package rs.elfak.mosis.observers.myplaces.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import rs.elfak.mosis.observers.myplaces.R
import rs.elfak.mosis.observers.myplaces.data.MyPlacesData
import java.lang.Exception

class ViewMyPlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_my_place)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var position = -1
        try {
            var listIntent = intent
            var positionBundle = listIntent.extras
            if (positionBundle != null) {
                position = positionBundle.getInt("position")
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            finish()
        }

        if (position >= 0)
        {
            var place = MyPlacesData.getInstance().getPlace(position)
            var twName = findViewById<TextView>(R.id.nameText)
            twName.setText(place.name)

            var twDesc = findViewById<TextView>(R.id.descText)
            twDesc.setText(place.description)

            var twLat = findViewById<TextView>(R.id.latText)
            twLat.setText(place.latitude)

            var twLon = findViewById<TextView>(R.id.lonText)
            twLon.setText(place.longitude)
        }

        var okButton = findViewById<Button>(R.id.button_ok)
        okButton.setOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_view_my_place, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.show_map_item -> {
                var i = Intent(this, MyPlacesMapsActivity::class.java)
                i.putExtra("state", MyPlacesMapsActivity.SHOW_MAP)

                startActivity(i)
            }
            R.id.my_places_list_item -> {
                var intent = Intent(this, MyPlacesList::class.java)
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