package com.jas.tecnologia.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnRegister : Button
    private lateinit var editEmail : EditText
    private lateinit var editPassword : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        loadComponents()




        btnRegister.setOnClickListener {
            var email = editEmail.text.toString()
            var password = editPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Atenção preencha os campos!", Toast.LENGTH_SHORT).show()
            } else {
                addNewUser(email, password)
            }
        }

    }


    fun addNewUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Novo usuario gravado com sucesso!!", Toast.LENGTH_SHORT).show()
                    var home = Intent(applicationContext, TarefasActivity::class.java)
                    startActivity(home)
                    finish()

                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun loadComponents (){
        // Initialize Firebase Auth
        auth = Firebase.auth
        btnRegister = findViewById(R.id.btn_confirm_register)
        editEmail = findViewById(R.id.text_email_register)
        editPassword = findViewById(R.id.text_password_register)
    }
}