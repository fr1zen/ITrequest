package com.example.itrequest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat

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
		
		val userName: EditText = findViewById(R.id.UserName)
		val userPassword: EditText = findViewById(R.id.UserPassword)
		val authButton: Button = findViewById(R.id.AddList)
		val passwordSort = Regex("^[a-zA-Z0-9]+$")
		val userCurrent = User()
		val dbUser = DbUsers(this, null)
		val user1 = User().apply {
			login = "progeon"
			password = "1234567amz"
			jobTitle = "manager"
		}
		dbUser.addUser(user1)
		val user2 = User().apply {
			login = "fr1zen"
			password = "1234567amz"
			jobTitle = "teacher"
		}
		dbUser.addUser(user2)
		
		val TVPasswordLength: TextView = findViewById(R.id.TVPasswordLength)
		
		authButton.setOnClickListener {
			userCurrent.login = userName.text.toString().trim()
			userCurrent.password = userPassword.text.toString().trim()
			val minPasswordLength = resources.getInteger(R.integer.password_length_min)
			val isPasswordValid = userCurrent.password.length >= minPasswordLength && passwordSort.matches(userCurrent.password)
			val users = dbUser.getAllUsers()
			val userExists = users.find { it.login == userCurrent.login && it.password == userCurrent.password }
			
			CheckPassword(TVPasswordLength, userCurrent.password)
			
			if (isPasswordValid && userExists != null) {
				if (userExists.jobTitle == "manager"){val mngWin = Intent(this, ManageActivity::class.java); startActivity(mngWin)}
				if (userExists.jobTitle == "teacher"){Toast.makeText(this, "ТЫ дАУН?)", Toast.LENGTH_LONG).show()}
			} else {
				Toast.makeText(this, "Пароль или Логин не подходит", Toast.LENGTH_LONG).show()
				userPassword.setText("")
			}
		}
	}
	
	//-------------------------------------------------------------------------------------
	fun CheckPassword(TVPasswordLength: TextView, password: String)
	{
		if (password.length != 0)
		{
			TVPasswordLength.text = resources.getString(R.string.password_length)
			if (password.length < R.integer.password_length_min)
				TVPasswordLength.setTextColor(ContextCompat.getColor(this, R.color.red))
			else
				TVPasswordLength.setTextColor(ContextCompat.getColor(this, R.color.green))
		}
	}
}