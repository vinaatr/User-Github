package com.example.apigram

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteUserGithub.Item::class], version = 1, exportSchema = false)
abstract class NoteRoomDbApp : RoomDatabase() {
    abstract fun userDao(): NoteUserDao
}