package com.example.data.retrofit

import com.example.data.dto.FortuneInfoDto
import com.example.data.dto.UserInfoDto
import retrofit2.Response
import retrofit2.http.GET

interface ServerApi {
    @GET("/api/fortune")
    suspend fun fetchFortuneInfo(username: String): Response<FortuneInfoDto>

    @GET("/api/userInfo")
    suspend fun fetchUserInfo(username: String): Response<UserInfoDto>
}