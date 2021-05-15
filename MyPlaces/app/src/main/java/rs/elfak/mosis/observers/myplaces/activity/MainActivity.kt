package rs.elfak.mosis.observers.myplaces.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import rs.elfak.mosis.observers.myplaces.About
import rs.elfak.mosis.observers.myplaces.MyPlacesList
import rs.elfak.mosis.observers.myplaces.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }/////sasdasa

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.show_map_item -> {
                Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show()
                var i = Intent(this, MyPlacesMapsActivity::class.java)
                startActivity(i)
            }
            R.id.my_places_list_item -> {
                Toast.makeText(this, "My Places!", Toast.LENGTH_SHORT)
                    .show()
                var intent = Intent(this, MyPlacesList::class.java)
                startActivity(intent)
            }
            R.id.new_place_item -> {
                var intent = Intent(this, EditMyPlaceActivity::class.java)
                startActivityForResult(
                    intent,
                    NEW_PLACE
                )
            }
            R.id.about_item -> {
                Toast.makeText(this, "About!", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, About::class.java)
                startActivity(intent)
            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            Toast.makeText(this, "New Place added!", Toast.LENGTH_SHORT).show()
    }

    companion object {
        var NEW_PLACE = 1
    }
}

