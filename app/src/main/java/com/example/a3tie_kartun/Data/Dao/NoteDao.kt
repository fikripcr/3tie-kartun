package com.example.a3tie_kartun.Data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.a3tie_kartun.Data.Entity.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    suspend fun getAll(): List<NoteEntity>

    @Insert
    suspend fun insert(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)
}