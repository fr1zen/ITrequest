package com.example.itrequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedData : ViewModel()
{
	private val _user = MutableLiveData<User>()
	val user: LiveData<User> get() = _user
	
	fun setUser(newUser: User) {
		_user.value = newUser
	}
}