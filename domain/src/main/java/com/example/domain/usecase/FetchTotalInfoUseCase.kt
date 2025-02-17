package com.example.domain.usecase

import com.example.domain.model.TotalInfoData
import com.example.domain.repository.TotalInfoRepository

class FetchTotalInfoUseCase(
    private val totalInfoRepository: TotalInfoRepository
) {
    suspend operator fun invoke(username: String): TotalInfoData {
        // 서버 요청
        return totalInfoRepository.fetchTotalInfo(username)
    }
}