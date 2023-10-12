package com.example.apigram

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: PreferenceManager) : ViewModel() {

    val hasilUser = MutableLiveData<Hasil>()

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    fun getUser(){
        viewModelScope.launch{
            flow{
                    val response = ApiClient
                        .githubService
                        .getUserGithub()

                    emit(response)
            }.onStart{
                hasilUser.value = Hasil.Loading(true)
            }.onCompletion{
                hasilUser.value = Hasil.Loading(false)
            }.catch{
                hasilUser.value = Hasil.Error(it)
            }.collect{
                hasilUser.value = Hasil.Sukses(it)
            }
        }

    }
    fun getUser(username:String){
        viewModelScope.launch{
            flow{
                val response = ApiClient
                    .githubService
                    .SearchUserGithub(mapOf(
                        "q" to username,
                        "per_page" to 10
                    ))

                emit(response)
            }.onStart{
                hasilUser.value = Hasil.Loading(true)
            }.onCompletion{
                hasilUser.value = Hasil.Loading(false)
            }.catch{
                hasilUser.value = Hasil.Error(it)
            }.collect{
                hasilUser.value = Hasil.Sukses(it.items)
            }
        }

    }

    class MainFactory (private val pref: PreferenceManager) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
