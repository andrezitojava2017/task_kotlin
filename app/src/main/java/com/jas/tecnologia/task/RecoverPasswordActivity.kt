package com.jas.tecnologia.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jas.tecnologia.task.R
import com.jas.tecnologia.task.fragments.EmailFragment
import com.jas.tecnologia.task.fragments.PasswordFragment

class RecoverPasswordActivity : AppCompatActivity() {

    private lateinit var btn_recuperar : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        val recover =EmailFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.recuperar_senha_fragment, recover)
            .commit()

        carregarComponentes()

        btn_recuperar.setOnClickListener {

            if(recover.validEmail(recover.getEmail())){
                var email = recover.getEmail().toString()
                resetarSenhaUsuario(email)
            } else{
               Toast.makeText(this, "Verifique o e-mail", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun resetarSenhaUsuario(email:String){

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> envaido email")
                    }
                }

    }

    fun carregarComponentes (){
        btn_recuperar = findViewById(R.id.btn_recuperar_senha)
        auth = Firebase.auth
    }
}