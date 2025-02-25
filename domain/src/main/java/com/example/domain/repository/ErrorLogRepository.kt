package com.example.domain.repository

import com.example.domain.model.Failure


interface ErrorLogRepository {
    // DB에 ErrorLog 저장
    suspend fun saveErrorLog(failure: Failure)
}