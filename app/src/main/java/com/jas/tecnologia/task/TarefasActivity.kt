package com.jas.tecnologia.task

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TarefasActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var toolbar : Toolbar
    private lateinit var shared : SharedPreferences
    private lateinit var editTextTask: EditText
    private lateinit var listViewTasks: ListView
    private lateinit var buttonAddTask: Button
    private lateinit var btnRemoveTask: Button
    private lateinit var btnLogout: Button
    private lateinit var btnEditText: Button
    private var tarefaSelecionado : Int = 0
    private var taskList = mutableListOf<String>()
    private lateinit var  uid : String
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefas)

        carregarItems()
        recuperarShared()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList)
        listViewTasks.adapter = adapter

       listViewTasks.setOnItemClickListener { parent, view, position, id ->

           val item = parent.getItemAtPosition(position).toString()
           this.tarefaSelecionado = position
           editTextTask.setText(item)

       }

        buttonAddTask.setOnClickListener {
            val taskName = editTextTask.text.toString()
            if (taskName.isNotEmpty()) {

                taskList.add(taskName)

                // add ao sharedpreferences
                guardarTarefas()

                adapter.notifyDataSetChanged()
                editTextTask.text.clear()
            }
        }

        btnRemoveTask.setOnClickListener {
            taskList.removeAt(tarefaSelecionado)
            adapter.notifyDataSetChanged()

            // atualiza sharedpreferences
            guardarTarefas()

            editTextTask.text.clear()
        }

        btnEditText.setOnClickListener {
            var novaTarefa = editTextTask.text.toString()
            taskList.set(tarefaSelecionado, novaTarefa)

            // atualiza sharedpreferences
            guardarTarefas()

            adapter.notifyDataSetChanged()
            editTextTask.text.clear()

        }

        btnLogout.setOnClickListener {

            Firebase.auth.signOut()

            val login = Intent(this,LoginActivity::class.java)
            startActivity(login)
            finish()
        }

    }

    fun carregarItems (){

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Suas tarefas"

        auth = Firebase.auth
        this.shared = application.getSharedPreferences("tarefas", Context.MODE_PRIVATE)
        editTextTask = findViewById(R.id.editTextTask)
        listViewTasks = findViewById(R.id.listViewTasks)
        buttonAddTask = findViewById(R.id.buttonAddTask)
        btnRemoveTask = findViewById(R.id.btnRemoveTask)
        btnEditText = findViewById(R.id.btnEditTask)
        btnLogout = findViewById(R.id.btnLogout)
    }
    fun guardarTarefas (){
        val gson = Gson()
        val listaSerializada = gson.toJson(taskList)
        shared.edit().putString("tarefas", listaSerializada).apply()
    }

    fun recuperarShared(){
        // Ler a string do SharedPreferences
        val listaRecuperada = this.shared.getString("tarefas", null)

        val gson = Gson()
        val tipoLista = object : TypeToken<List<String>>() {}.type
        val lista = gson.fromJson<List<String>>(listaRecuperada, tipoLista)

        lista.forEach { item ->
            taskList.add(item)
        }
    }

}