package com.example.domain.repository

import com.example.domain.model.ResultWrapper
import com.example.domain.model.TotalInfoData

interface TotalInfoRepository {
    // DB 요청, DB에 데이터 없으면 null 반환
    suspend fun getTotalInfo(username: String): ResultWrapper<TotalInfoData?>

    // 서버 요청
    suspend fun fetchTotalInfo(username: String): ResultWrapper<TotalInfoData>
}