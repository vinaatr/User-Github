package com.example.apigram

sealed class Hasil{
    data class Sukses<out T>(val data: T): Hasil()
    data class Error(val exception: Throwable): Hasil()
    data class Loading(val isLoading: Boolean): Hasil()
}
