package rs.elfak.mosis.observers.myplaces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val ok = findViewById<Button>(R.id.about_ok)
        ok.setOnClickListener {
            finish()
        }

        //komentar
    }
}