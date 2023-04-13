package com.jas.tecnologia.task.fragments

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.jas.tecnologia.task.R
import com.jas.tecnologia.task.model.Task


/**
 * A simple [Fragment] subclass.
 * Use the [ListTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListTaskFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var editTextTask: EditText
    private lateinit var listViewTasks: ListView
    private lateinit var buttonAddTask: Button
    private lateinit var btnRemoveTask: Button
   // private lateinit var btnLogout: Button
    private lateinit var btnEditText: Button
    private var tarefaSelecionado : Int = 0
    private var taskList = mutableListOf<String>()
    private lateinit var  uid : String
    private lateinit var adapter: ArrayAdapter<String>

    val CHANNEL_ID = "pickerChannel"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_task, container, false)

        carregarItems(view)
        getUid()
        recuperarTarefas()

        criarNotificacao(view)

        adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, taskList)
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

                guardarTarefas()

                adapter.notifyDataSetChanged()
                editTextTask.text.clear()

                executarNotificacao(view)
            }
        }

        btnRemoveTask.setOnClickListener {
            taskList.removeAt(tarefaSelecionado)
            adapter.notifyDataSetChanged()

            // atualiza a lista de tarefas no database
            guardarTarefas()

            editTextTask.text.clear()
        }

        // editar tarefa
        btnEditText.setOnClickListener {
            var novaTarefa = editTextTask.text.toString()
            taskList.set(tarefaSelecionado, novaTarefa)

            // atualiza sharedpreferences
            atualizarBanco()

            adapter.notifyDataSetChanged()
            editTextTask.text.clear()

        }
        return view
    }


    @SuppressLint("SuspiciousIndentation")
    fun criarNotificacao (view:View){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Tasking"
            val descriptionText = "Tarefa Agendada"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
              view.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
              notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun executarNotificacao(view: View){
        val builder = NotificationCompat.Builder(view.context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Nova Tarefa")
            .setContentText("Uma nova Tarefa adicionada")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(view.context)){
            notify(0, builder.build())
        }
    }


    fun carregarItems (view:View){

        auth = Firebase.auth
        database = Firebase.database.reference
        adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, taskList)
        editTextTask = view.findViewById(R.id.editTextTask)
        listViewTasks = view.findViewById(R.id.listViewTasks)
        buttonAddTask = view.findViewById(R.id.buttonAddTask)
        btnRemoveTask = view.findViewById(R.id.btnRemoveTask)
        btnEditText = view.findViewById(R.id.btnEditTask)

        //btnLogout = view.findViewById(R.id.btnLogout)
    }
    fun guardarTarefas (){

        val task = Task(taskList )
        database.child("tasks").child(this.uid).child("mytasks").setValue(taskList)

    }

    fun recuperarTarefas(){

        database.child("tasks").child(this.uid).child("mytasks").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue<List<String>>()
                if (data != null) {
                    if(taskList.size != 0){
                        taskList.clear()
                    }
                    data.map { t -> taskList.add(t) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println("Erro ao ler dados: ${error.toException()}")
            }
        })

    }

    fun atualizarBanco(){
        database.child("tasks").child(this.uid).child("mytasks")
        val updates = mapOf<String, String>("${tarefaSelecionado}" to taskList[tarefaSelecionado].toString())
        database.child("tasks").child(this.uid).child("mytasks").updateChildren(updates)
    }

    fun getUid(){
        val data= auth.currentUser
        this.uid = data?.uid.toString()

    }

    fun novaTarefa():String{

        val tarefa = editTextTask.text.toString()
        return tarefa

    }
}