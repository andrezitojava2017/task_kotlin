package com.jas.tecnologia.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var textLoginEmail : EditText
    private lateinit var textLoginSenha : EditText
    private lateinit var btnLoginEntrar : Button
    private lateinit var btnLoginRegistrar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        carregarComponentes()

        btnLoginEntrar.setOnClickListener {
            var email = textLoginEmail.text.toString()
            var password = textLoginSenha.text.toString()

            login(email, password)

        }

        btnLoginRegistrar.setOnClickListener {
            val register = Intent(getApplication(),RegisterActivity::class.java)
            startActivity(register)
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
        textLoginEmail = findViewById(R.id.textLoginEmail)
        textLoginSenha = findViewById(R.id.textLoginSenha)
        btnLoginEntrar = findViewById(R.id.btnLoginEntrar)
        btnLoginRegistrar = findViewById(R.id.btnLoginRegistrar)
    }
}