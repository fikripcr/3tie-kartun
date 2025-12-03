package com.example.a3tie_kartun.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a3tie_kartun.Data.Dao.NoteDao
import com.example.a3tie_kartun.Data.Entity.NoteEntity

@Database(
    entities = [NoteEntity::class], // tambahkan entitas baru di sini
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    /*Tambahkan Dao baru disini */
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}