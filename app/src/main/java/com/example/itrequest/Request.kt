package com.example.itrequest

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "requests")
data class Request
(
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0,
	var title: String,
	var description: String,
	var login: String
)