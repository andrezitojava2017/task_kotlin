package com.jas.tecnologia.task.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.jas.tecnologia.task.R


class PasswordFragment : Fragment() {


    private lateinit var textLoginSenha : EditText
    private lateinit var statusPassword : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_password, container, false)
        carregarComponentes(view)
        return view
    }

    fun carregarComponentes (view : View){
        textLoginSenha = view.findViewById(R.id.textLoginSenha)
        statusPassword = view.findViewById(R.id.status_password)
    }

    fun validPassword(password:String){

        var result:String =""
        val passwordStrong = Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9 ]).*$")
        val chars = Regex("(?=.*[A-Za-z])(?=.*[0-9]).*")
        val especial = Regex("[^A-Za-z0-9 ]") // busca caracters especial
        val numbers = Regex("^[0-9]+$") // busca numeros

        if(passwordStrong.matches(password)){
           statusPassword.setText("Senha forte")
        }

        if(especial.matches(password) || numbers.matches(password)){
            statusPassword.setText("Senha Fraca")
        }
    }

    fun getPassword ():String{
        return textLoginSenha.text.toString()
    }

}