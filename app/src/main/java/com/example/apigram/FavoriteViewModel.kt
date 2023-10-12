package com.example.apigram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FavoriteViewModel(private val roomDb: RoomDb) : ViewModel() {

    fun getUserFavorite() = roomDb.userDao.loadAll()

    class Factory(private val db: RoomDb) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoriteViewModel(db) as T
    }
}