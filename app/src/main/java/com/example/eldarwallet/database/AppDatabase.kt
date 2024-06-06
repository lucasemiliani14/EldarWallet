package com.example.eldarwallet.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eldarwallet.data.CreditCard
import com.example.eldarwallet.dao.CreditCardDao
import com.example.eldarwallet.data.User
import com.example.eldarwallet.dao.UserDao

@Database(entities = [User::class, CreditCard::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun creditCardDao(): CreditCardDao

    companion object {
        private var instance: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .build()
                }
            }
            return instance!!
        }
    }
}