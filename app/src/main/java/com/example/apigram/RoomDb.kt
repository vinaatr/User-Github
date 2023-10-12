package com.example.apigram

import android.content.Context
import androidx.room.Room

class RoomDb(private val context: Context) {
    private val db = Room.databaseBuilder(context, NoteRoomDbApp::class.java, "usergithub.db")
        .allowMainThreadQueries()
        .build()

    val userDao = db.userDao()
}