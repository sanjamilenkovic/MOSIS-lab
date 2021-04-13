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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import rs.elfak.mosis.observers.myplaces.About
import rs.elfak.mosis.observers.myplaces.MyPlacesList
import rs.elfak.mosis.observers.myplaces.R
import rs.elfak.mosis.observers.myplaces.data.MyPlace
import rs.elfak.mosis.observers.myplaces.data.MyPlacesData

class EditMyPlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_place)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var finishedButton = findViewById<Button>(R.id.editMyPlaceFinishedButton)
        finishedButton.text = "ADD"
        finishedButton.isEnabled = false

        var nameEdit = findViewById<EditText>(R.id.editName)

        nameEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                finishedButton.isEnabled=true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        finishedButton.setOnClickListener {

            var nameEditText = findViewById<EditText>(R.id.editName).text.toString()
            var descriptionEditText = findViewById<EditText>(R.id.descriptionEditText).text.toString()

            var newPlace = MyPlace(nameEditText, descriptionEditText)
            MyPlacesData.getInstance().addNewPlace(newPlace)
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
        return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_edit_my_place, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.show_map_item -> Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show()
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