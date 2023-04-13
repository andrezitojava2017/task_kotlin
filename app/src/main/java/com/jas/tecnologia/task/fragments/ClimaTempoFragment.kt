package com.jas.tecnologia.task.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.jas.tecnologia.task.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ClimaTempoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clima_tempo, container, false)

        carregarClimaTempo(view)
        return view
    }


    fun carregarClimaTempo (view: View){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://api.weatherapi.com/v1/current.json?key=9c31828d1bf748b5b8a232349231004&q=48.8567,2.3508a")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context, "Erro ao fazer consulta", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val current = JSONObject(body).getJSONObject("current").getDouble("temp_c")

                    exibirClima(current, view)

                } else {
                    println(">>>>>>>>>>>>>>>>>>>>>>>>>Erro: ${response.code} - ${response.message}")
                }
            }
        })
    }

    fun exibirClima(temp:Double, view: View){

        val climaTempo = view.findViewById<TextView>(R.id.text_clima)
        climaTempo.text = "${temp} ºC"
    }

}