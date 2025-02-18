package com.example.domain.repository

import com.example.domain.model.TotalInfoData

interface TotalInfoRepository {
    // 무결성 보장 및 DB를 2번씩 조회하는 상황 피하기 위해 로직 수정(checkTotalInfoInDb 삭제)
    // DB 요청, DB에 데이터 없으면 null 반환
    suspend fun getTotalInfo(username: String): TotalInfoData?

    // 서버 요청
    suspend fun fetchTotalInfo(username: String): TotalInfoData
}