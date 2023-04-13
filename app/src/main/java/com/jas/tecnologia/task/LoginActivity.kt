package com.jas.tecnologia.task

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jas.tecnologia.task.fragments.EmailFragment
import com.jas.tecnologia.task.fragments.FormLoginFragment
import com.jas.tecnologia.task.fragments.ListTaskFragment
import com.jas.tecnologia.task.fragments.PasswordFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnLoginEntrar : Button
    private lateinit var text_esqueci_minha_senha :TextView
    private lateinit var text_registrar :TextView
    private lateinit var analytics: FirebaseAnalytics

    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        analytics = Firebase.analytics

        carregarComponentes()
        carregarAdMob()


        val email = EmailFragment()
        val password = PasswordFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_form_login, email)
            .add(R.id.fragment_form_login, password)
            .commit()

        /**
         * botao de login, verificação de senha e email
         */
        btnLoginEntrar.setOnClickListener {

            val emailUser = email.getEmail()
            val passUser = password.getPassword()

            val res = email.validEmail(emailUser);
            val validPass = password.validPassword(passUser)

            if(res){
                login(emailUser, passUser)
            } else {
                Toast.makeText(this, "Formato de e-mail invalido!", Toast.LENGTH_LONG).show()
            }
        }

        text_registrar.setOnClickListener {
            val register = Intent(this,RegisterActivity::class.java)
            startActivity(register)
        }

        text_esqueci_minha_senha.setOnClickListener {
            val recover = Intent(this, RecoverPasswordActivity::class.java)
            startActivity(recover)
        }

    }

    fun login(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var intent = Intent(this, TarefasActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {

                    Toast.makeText(applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun carregarComponentes (){
        // Initialize Firebase Auth
        auth = Firebase.auth
        btnLoginEntrar = findViewById(R.id.btnLoginEntrar)
        text_esqueci_minha_senha = findViewById(R.id.text_esqueci_minha_senha)
        text_registrar = findViewById(R.id.text_registrar)



    }

    fun carregarAdMob(){
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

}