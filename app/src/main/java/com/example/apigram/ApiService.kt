package com.example.apigram

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUserGithub(): MutableList<NoteUserGithub.Item>

    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getDetailUserGithub(@Path("username") username: String): ResponseDetailUser

    @JvmSuppressWildcards
    @GET("users/{username}/followers")
    suspend fun getFollowersUserGithub(@Path("username") username: String): MutableList<NoteUserGithub.Item>

    @JvmSuppressWildcards
    @GET("users/{username}/following")
    suspend fun getFollowingsUserGithub(@Path("username") username: String): MutableList<NoteUserGithub.Item>

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun SearchUserGithub(@QueryMap params: Map<String, Any>): NoteUserGithub

}

