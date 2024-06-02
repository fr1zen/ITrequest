package com.example.itrequest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbITrequest(val context: Context, factory: SQLiteDatabase.CursorFactory?, val name: String):
				SQLiteOpenHelper(context, name, factory, 1)
{
	override fun onCreate(db: SQLiteDatabase?)
	{
		val userQuery = "CREATE TABLE " + name + " (login TEXT, password TEXT, jobTitle TEXT)"
		val requestQuery = "CREATE TABLE requests (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT)"
		db!!.execSQL(userQuery)
		db.execSQL(requestQuery)
	}
	
	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
	{
		db!!.execSQL("DROP TABLE IF EXISTS " + name)
		db.execSQL("DROP TABLE IF EXISTS requests")
		onCreate(db)
	}
	
	fun addUser(user: User)
	{
		val values = ContentValues()
		values.put("login", user.login)
		values.put("password", user.password)
		values.put("jobTitle", user.jobTitle)
		
		val db = this.writableDatabase
		db.insert(name, null, values)
		db.close()
	}
	
	fun getAllUsers(): List<User> {
		val users = mutableListOf<User>()
		val db = this.readableDatabase
		val cursor = db.rawQuery("SELECT * FROM name", null)
		
		if (cursor.moveToFirst())
		{
			do
			{
				val login: String = cursor.getString(cursor.getColumnIndexOrThrow("login"))
				val password: String = cursor.getString(cursor.getColumnIndexOrThrow("password"))
				val jobTitle: String = cursor.getString(cursor.getColumnIndexOrThrow("jobTitle"))
				
				users.add(User(login, password, jobTitle))
			} while (cursor.moveToNext())
		}
		
		cursor.close()
		db.close()
		return users
	}
	
	fun addRequest(request: Request) {
		val values = ContentValues()
		values.put("title", request.title)
		values.put("description", request.description)
		values.put("login", request.login)
		
		val db = this.writableDatabase
		db.insert("requests", null, values)
		db.close()
	}
	
	fun getAllRequests(): List<Request> {
		val requests = mutableListOf<Request>()
		val db = this.readableDatabase
		val cursor = db.rawQuery("SELECT * FROM requests", null)
		
		if (cursor.moveToFirst()) {
			do {
				val request = Request(
					id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
					title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
					description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
					login = cursor.getString(cursor.getColumnIndexOrThrow("login"))
				)
				requests.add(request)
			} while (cursor.moveToNext())
		}
		
		cursor.close()
		db.close()
		return requests
	}
	
	fun updateRequest(request: Request) {
		val values = ContentValues()
		values.put("title", request.title)
		values.put("description", request.description)
		values.put("login", request.login)
		
		val db = this.writableDatabase
		db.update("requests", values, "id = ?", arrayOf(request.id.toString()))
		db.close()
	}
}