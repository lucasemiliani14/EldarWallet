package com.example.eldarwallet.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    val nombre: String,
    val apellido: String,
    val saldo: Int = 0,
    val key: String
)
