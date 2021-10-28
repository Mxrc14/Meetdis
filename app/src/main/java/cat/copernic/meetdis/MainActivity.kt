package cat.copernic.meetdis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val boton: Button = findViewById(R.id.b_entra)
        var holamon: TextView = findViewById(R.id.hello)
        var nom:String = "Holaaa"

        boton.setOnClickListener{cambio()}

    }

    fun cambio() {
        val boton: Button = findViewById(R.id.b_entra)
        var holamon: TextView = findViewById(R.id.hello)
        var nom:String = "XD"

        holamon.text = nom


    }
}