package com.example.itrequest

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))
		{ v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
		
		val label = findViewById<TextView>(R.id.MainLabel)
		val userName: EditText = findViewById(R.id.UserName)
		val button: Button = findViewById(R.id.Auth)
		val regex = Regex("^[a-zA-Z]+$")
		val userCurrent = User()
		
		val users = arrayOf(		// задаем пользователей
			User().apply{
				login = "progeon"
				password = "12345"
			}, User().apply{
				login = "fr1zen"
				password = "12345"
			}
		)
		val usersQt = users.size		// количество пользователей
		
		button.setOnClickListener {
			userCurrent.login = userName.text.toString().trim()
			if (userCurrent.login.length >= R.integer.login_length_min && regex.matches(userCurrent.login))
				Toast.makeText(this, "Логин подходит", Toast.LENGTH_SHORT).show()
			else
			{
				Toast.makeText(this, "Логин не подходит (Английские символы, минимум 8 - символов)", Toast.LENGTH_LONG).show()
				userName.setText("")
			}
		}
	}
}