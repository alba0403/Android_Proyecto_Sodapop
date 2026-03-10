package com.example.sodapop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BotonsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_botons)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Conectar botones a actividades existentes
        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, ViewReceptaActivity::class.java))
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            startActivity(Intent(this, BuscadorActivity::class.java))
        }
        findViewById<Button>(R.id.button4).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        findViewById<Button>(R.id.button6).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<Button>(R.id.button7).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}
