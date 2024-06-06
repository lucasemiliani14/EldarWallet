package com.example.eldarwallet.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.eldarwallet.data.CreditCard

@Dao
interface CreditCardDao {

    @Insert
    suspend fun insert(creditCard: CreditCard)

    @Update
    suspend fun update(creditCard: CreditCard)

    @Delete
    suspend fun delete(creditCard: CreditCard)

    @Query("SELECT * FROM creditcard")
    suspend fun getAllCreditCards(): List<CreditCard>

    @Query("SELECT * FROM CreditCard WHERE userId = :userId")
    suspend fun getCreditCardsByUserId(userId: Int): List<CreditCard>

    @Query("SELECT * FROM CreditCard WHERE id = :id")
    suspend fun getCreditCardById(id: Int): CreditCard?

    @Query("SELECT * FROM CreditCard WHERE numero = :numero")
    suspend fun getCreditCardByNumber(numero: String): CreditCard?


}