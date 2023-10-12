package com.example.apigram

import androidx.room.*
import androidx.lifecycle.LiveData

@Dao
interface NoteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: NoteUserGithub.Item)

    @Query("SELECT * FROM User")
    fun loadAll(): LiveData<MutableList<NoteUserGithub.Item>>

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): NoteUserGithub.Item

    @Delete
    fun delete(user: NoteUserGithub.Item)
}