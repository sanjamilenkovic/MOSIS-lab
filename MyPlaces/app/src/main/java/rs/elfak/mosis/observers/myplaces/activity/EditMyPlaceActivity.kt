package rs.elfak.mosis.observers.myplaces.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import rs.elfak.mosis.observers.myplaces.R
import rs.elfak.mosis.observers.myplaces.data.MyPlace
import rs.elfak.mosis.observers.myplaces.data.MyPlacesData
import java.lang.Exception

class EditMyPlaceActivity : AppCompatActivity() {
    var editMode: Boolean = true
    var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_place)

        try {
            var listIntent: Intent = intent
            var positionBundle: Bundle? = listIntent.extras
            if (positionBundle != null) {
                position = positionBundle.getInt("position")
            } else {
                editMode = false
            }


        } catch (e: Exception) {
            editMode = false
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



        var finishedButton = findViewById<Button>(R.id.editMyPlaceFinishedButton)
        finishedButton.text = "ADD"
        finishedButton.isEnabled = false

        var nameEdit = findViewById<EditText>(R.id.editName)

        if (!editMode) {
            finishedButton.isEnabled = false
            finishedButton.setText("ADD")
        } else if (position >= 0) {
            finishedButton.setText("SAVE")
            var place: MyPlace = MyPlacesData.getInstance().getPlace(position)
            nameEdit.setText(place.name)
            var descEditText = findViewById<EditText>(R.id.descriptionEditText)
            descEditText.setText(place.description)

        }

        var getLocationButton = findViewById<Button>(R.id.getLocationButton)

        getLocationButton.setOnClickListener {

            var i = Intent(this, MyPlacesMapsActivity::class.java)
            i.putExtra("state", MyPlacesMapsActivity.SELECT_COORDINATES)

            startActivityForResult(i, 2)
        }


        nameEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                finishedButton.isEnabled = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        finishedButton.setOnClickListener {


            var nameEditText = findViewById<EditText>(R.id.editName).text.toString()
            var descriptionEditText =
                findViewById<EditText>(R.id.descriptionEditText).text.toString()
            var latitudeEditText = findViewById<EditText>(R.id.editLatitude).text.toString()
            var longitudeEditText = findViewById<EditText>(R.id.editLongitude).text.toString()

            if (!editMode) {
                var newPlace = MyPlace()
                newPlace.description = descriptionEditText
                newPlace.name = nameEditText
                newPlace.latitude = latitudeEditText
                newPlace.longitude = longitudeEditText
                MyPlacesData.getInstance().addNewPlace(newPlace)

            } else {
                MyPlacesData.getInstance().updatePlace(position,nameEditText,descriptionEditText,longitudeEditText,latitudeEditText)
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        var cancelButton = findViewById<Button>(R.id.editMyPlaceCancelButton)
        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_my_place, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.show_map_item -> {
                var i = Intent(this, MyPlacesMapsActivity::class.java)
                i.putExtra("state", MyPlacesMapsActivity.SELECT_COORDINATES)
                Toast.makeText(this, "KOORDINATE", Toast.LENGTH_SHORT).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK) {
                var lon = data?.extras?.getString("lon")
                var lonText = findViewById<EditText>(R.id.editLongitude)
                lonText.setText(lon)

                var lat = data?.extras?.getString("lat")
                var latText = findViewById<EditText>(R.id.editLatitude)
                latText.setText(lat)
            }
        }
        catch (e : Exception)
        {

        }
    }
}