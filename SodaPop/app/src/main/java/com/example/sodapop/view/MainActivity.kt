package com.example.sodapop.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.sodapop.view.HomeActivity
import com.example.sodapop.view.IniciarSessioActivity
import com.example.sodapop.R
import com.example.sodapop.view.RegistrarActivity

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