package com.example.eldarwallet.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.data.User
import com.example.eldarwallet.database.AppDatabase
import kotlinx.coroutines.launch

class UserViewmodel(private val appDatabase: AppDatabase, private val userId: Int): ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user


    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            val response = appDatabase.userDao().getUserById(userId)
            _user.value = response!!
        }
    }



}