package com.jas.tecnologia.task

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jas.tecnologia.task.fragments.ClimaTempoFragment
import com.jas.tecnologia.task.fragments.ListTaskFragment
import okhttp3.*
import java.io.IOException

class TarefasActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var toolbar : Toolbar
    private lateinit var btnLogout: ImageButton
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefas)

        carregarItems()

        btnLogout.setOnClickListener {

            Firebase.auth.signOut()

            val login = Intent(this,LoginActivity::class.java)
            startActivity(login)
            finish()
        }

        val clima = ClimaTempoFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.clima_tempo, clima)
            .commit()

        val fragmento = ListTaskFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmento, fragmento)
            .commit()



    }


    fun carregarItems (){

        btnLogout = findViewById(R.id.btnLogout)

        auth = Firebase.auth
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


    }


}