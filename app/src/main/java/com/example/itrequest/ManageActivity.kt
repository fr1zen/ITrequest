package com.example.itrequest

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ManageActivity : AppCompatActivity() {
	
	private val todos: MutableList<String> = mutableListOf()
	private val details: HashMap<Int, String> = hashMapOf()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_manage)
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
		
		val listData: EditText = findViewById(R.id.ListData)
		val askList: ListView = findViewById(R.id.AskList)
		val addButton: Button = findViewById(R.id.AddList)
		
		val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todos)
		askList.adapter = adapter
		
		askList.setOnItemClickListener { _, _, position, _ ->
			val text = details[position] ?: todos[position]
			showDetailDialog(text, position)
		}
		
		addButton.setOnClickListener {
			val text = listData.text.toString().trim()
			if (text.isNotEmpty()) {
				adapter.insert(text, 0)
				listData.setText("")
			}
		}
	}
	
	private fun showDetailDialog(text: String, position: Int) {
		val builder = AlertDialog.Builder(this)
		builder.setTitle("Подробности")
		
		val input = EditText(this)
		input.setText(text)
		builder.setView(input)
		
		builder.setPositiveButton("Сохранить") { dialog, _ ->
			val newText = input.text.toString()
			details[position] = newText
			dialog.dismiss()
		}
		
		builder.setNegativeButton("Отмена") { dialog, _ ->
			dialog.cancel()
		}
		
		val dialog = builder.create()
		dialog.show()
	}
}
