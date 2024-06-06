package com.example.eldarwallet.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.data.CreditCard
import com.example.eldarwallet.data.User
import com.example.eldarwallet.database.AppDatabase
import kotlinx.coroutines.launch

class CreditCardListViewmodel(private val appDatabase: AppDatabase, private val userId: Int): ViewModel()  {

    private val _creditCardList = MutableLiveData<List<CreditCard>>()
    val creditCardList: LiveData<List<CreditCard>> get() = _creditCardList


    init {
        getCreditCardList()
    }

    private fun getCreditCardList() {
        viewModelScope.launch {
            val response = appDatabase.creditCardDao().getCreditCardsByUserId(userId)
            _creditCardList.value = response
            val a = "a"
        }
    }

}