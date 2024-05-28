package com.example.itrequest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbUsers(val context: Context, factory: SQLiteDatabase.CursorFactory?):
				SQLiteOpenHelper(context, "Users", factory, 1)
{
	override fun onCreate(db: SQLiteDatabase?)
	{
		val query = "CREATE TABLE users (login TEXT, password TEXT, jobTitle TEXT)"
		db!!.execSQL(query)
	}
	
	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
	{
		db!!.execSQL("DROP TABLE IF EXISTS users")
		onCreate(db)
	}
	
	fun addUser(user: User)
	{
		val values = ContentValues()
		values.put("login", user.login)
		values.put("password", user.password)
		values.put("jobTitle", user.jobTitle)
		
		val db = this.writableDatabase
		db.insert("users", null, values)
		db.close()
	}
	
	fun getAllUsers(): List<User> {
		val users = mutableListOf<User>()
		val db = this.readableDatabase
		val cursor = db.rawQuery("SELECT * FROM users", null)
		
		if (cursor.moveToFirst()) {
			do {
				val user = User().apply {
					login = cursor.getString(cursor.getColumnIndexOrThrow("login"))
					password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
					jobTitle = cursor.getString(cursor.getColumnIndexOrThrow("jobTitle"))
				}
				users.add(user)
			} while (cursor.moveToNext())
		}
		
		cursor.close()
		db.close()
		return users
	}
}