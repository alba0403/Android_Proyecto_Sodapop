package com.example.sodapop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Cridar abans de super.onCreate() i setContentView()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            false
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnInvitado).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<TextView>(R.id.linkIniciarSessio).setOnClickListener {
            startActivity(Intent(this, IniciarSessioActivity::class.java))
        }

        findViewById<TextView>(R.id.linkRegistrar).setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }
    }
}