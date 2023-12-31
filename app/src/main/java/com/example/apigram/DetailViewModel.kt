package com.example.apigram

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(private val db: RoomDb): ViewModel() {

    val hasilDetailUser = MutableLiveData<Hasil>()
    val hasilFollowersUser = MutableLiveData<Hasil>()
    val hasilFollowingUser = MutableLiveData<Hasil>()
    val hasilSuksesFavorite = MutableLiveData<Boolean>()
    val hasilDeleteFavorite = MutableLiveData<Boolean>()

    private var isFavorite = false

    fun setFavorite(item: NoteUserGithub.Item?) {
        viewModelScope.launch {
            item?.let {
                if (isFavorite) {
                    db.userDao.delete(item)
                    hasilDeleteFavorite.value = true
                } else {
                    db.userDao.insert(item)
                    hasilSuksesFavorite.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user != null) {
                listenFavorite()
                isFavorite = true
            }
        }
    }

    fun getDetailUser(username:String){
        viewModelScope.launch{
            flow{
                val response = ApiClient
                    .githubService
                    .getDetailUserGithub(username)

                emit(response)
            }.onStart{
                hasilDetailUser.value = Hasil.Loading(true)
            }.onCompletion{
                hasilDetailUser.value = Hasil.Loading(false)
            }.catch{
                hasilDetailUser.value = Hasil.Error(it)
            }.collect{
                hasilDetailUser.value = Hasil.Sukses(it)
            }
        }

    }
    fun getFollowers(username: String){
        viewModelScope.launch{
            flow{
                val response = ApiClient
                    .githubService
                    .getFollowersUserGithub(username)

                emit(response)
            }.onStart{
                hasilFollowersUser.value = Hasil.Loading(true)
            }.onCompletion{
                hasilFollowersUser.value = Hasil.Loading(false)
            }.catch{
                hasilFollowersUser.value = Hasil.Error(it)
            }.collect{
                hasilFollowersUser.value = Hasil.Sukses(it)
            }
        }
    }

    fun getFollowing(username: String){
        viewModelScope.launch{
            flow{
                val response = ApiClient
                    .githubService
                    .getFollowingsUserGithub(username)

                emit(response)
            }.onStart{
                hasilFollowingUser.value = Hasil.Loading(true)
            }.onCompletion{
                hasilFollowingUser.value = Hasil.Loading(false)
            }.catch{
                hasilFollowingUser.value = Hasil.Error(it)
            }.collect{
                hasilFollowingUser.value = Hasil.Sukses(it)
            }
        }
    }
    class Factory(private val db: RoomDb) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}
