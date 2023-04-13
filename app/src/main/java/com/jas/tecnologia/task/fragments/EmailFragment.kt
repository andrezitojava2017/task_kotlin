package com.jas.tecnologia.task.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jas.tecnologia.task.R


class EmailFragment : Fragment() {

    private lateinit var textLoginEmail : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_email, container, false)
        carregarComponentes(view)

        return view
    }


    fun carregarComponentes (view : View){
        textLoginEmail = view.findViewById(R.id.textLoginEmail)
    }

    fun validEmail(email:String):Boolean{
        val regexEmail = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return regexEmail.matches(email)
    }

    fun getEmail ():String{
        return textLoginEmail.text.toString()
    }
}