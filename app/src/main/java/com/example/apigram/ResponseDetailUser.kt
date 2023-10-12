package com.example.apigram

data class ResponseDetailUser(
    val avatar_url: String,
    val followers: Int,
    val followers_url: String,
    val following: Int,
    val following_url: String,
    val id: Int,
    val login: String,
    val name: String
)