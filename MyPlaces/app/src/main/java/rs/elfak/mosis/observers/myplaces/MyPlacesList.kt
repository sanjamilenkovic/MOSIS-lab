package rs.elfak.mosis.observers.myplaces


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import rs.elfak.mosis.observers.myplaces.activity.MainActivity.Companion.NEW_PLACE
import rs.elfak.mosis.observers.myplaces.activity.EditMyPlaceActivity
import rs.elfak.mosis.observers.myplaces.activity.ViewMyPlaceActivity
import rs.elfak.mosis.observers.myplaces.data.MyPlace
import rs.elfak.mosis.observers.myplaces.data.MyPlacesData

class MyPlacesList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_places_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            var i = Intent(this, EditMyPlaceActivity::class.java)
            startActivityForResult(i, NEW_PLACE)
        }


        val myPlaceList = findViewById<ListView>(R.id.my_place_list)

        myPlaceList.adapter = ArrayAdapter<MyPlace>(
            this,
            android.R.layout.simple_list_item_1,
            MyPlacesData.getInstance().getMyPlaces()
        )
        myPlaceList.setOnItemClickListener { parent, view, position, id ->
            var place: MyPlace = parent.adapter.getItem(position) as MyPlace
            Toast.makeText(this, place.name.toString() + " selected ", Toast.LENGTH_SHORT).show()

            var positionBundle: Bundle = Bundle()
            positionBundle.putInt("position", position)
            var intent = Intent(this, ViewMyPlaceActivity::class.java)
            intent.putExtras(positionBundle)
            startActivity(intent)
        }
        myPlaceList.setOnCreateContextMenuListener { menu, v, menuInfo ->
            var info: AdapterView.AdapterContextMenuInfo =
                menuInfo as AdapterView.AdapterContextMenuInfo
            var place: MyPlace = MyPlacesData.getInstance().getPlace(info.position)
            menu.setHeaderTitle(place.name)
            menu.add(0, 1, 1, "View place")
            menu.add(0, 2, 2, "Edit place")

        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var info: AdapterView.AdapterContextMenuInfo =
            item.menuInfo as AdapterView.AdapterContextMenuInfo
        var positionBundle: Bundle = Bundle()
        positionBundle.putInt("position", info.position)
        var i: Intent? = null

        if (item.itemId == 1) {
            i = Intent(this, ViewMyPlaceActivity::class.java)
            i.putExtras(positionBundle)
            startActivity(i)
        } else if (item.itemId == 2) {
            i = Intent(this, EditMyPlaceActivity::class.java)
            i.putExtras(positionBundle)
            startActivityForResult(i, 1)
        }

        return super.onContextItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_my_places_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.show_map_item -> Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show()
            R.id.new_place_item -> {
                var intent = Intent(this, EditMyPlaceActivity::class.java)
                startActivityForResult(intent, NEW_PLACE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var myPlacesList: ListView = findViewById(R.id.my_place_list)
        myPlacesList.adapter = ArrayAdapter<MyPlace>(
            this,
            android.R.layout.simple_list_item_1,
            MyPlacesData.getInstance().getMyPlaces()
        )

    }
}