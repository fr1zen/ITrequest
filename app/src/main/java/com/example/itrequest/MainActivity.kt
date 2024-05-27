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
		val userPassword: EditText = findViewById(R.id.UserPassword)
		val authButton: Button = findViewById(R.id.Auth)
		val passwordSort = Regex("^[a-zA-Z0-9]+$")
		val userCurrent = User()
		
		val users = arrayOf(		// задаем пользователей
			User().apply{
				login = "progeon"
				password = "1234567amz"
				jobTitle = "manager"
			}, User().apply{
				login = "fr1zen"
				password = "1234567amz"
				jobTitle = "teacher"
			}
		)
		val usersQt = users.size		// количество пользователей
		
		authButton.setOnClickListener {
			userCurrent.login = userName.text.toString().trim()
			userCurrent.password = userPassword.text.toString().trim()
			val minPasswordLength = resources.getInteger(R.integer.password_length_min)
			val isPasswordValid = userCurrent.password.length >= minPasswordLength && passwordSort.matches(userCurrent.password)
			val userExists = users.any { it.login == userCurrent.login && it.password == userCurrent.password }
			
			if (isPasswordValid && userExists) {
				Toast.makeText(this, "Логин подходит", Toast.LENGTH_SHORT).show()
			} else {
				Toast.makeText(this, "Пароль или Логин не подходит", Toast.LENGTH_LONG).show()
				userPassword.setText("")
			}
		}
	}
}