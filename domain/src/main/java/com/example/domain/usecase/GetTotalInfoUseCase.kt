package com.example.domain.usecase

import com.example.domain.model.TotalInfoData
import com.example.domain.repository.TotalInfoRepository

class GetTotalInfoUseCase(
    private val totalInfoRepository: TotalInfoRepository
) {
    suspend operator fun invoke(username: String): TotalInfoData? {
        // DB 요청, DB에 데이터 없으면 null 반환
        return totalInfoRepository.getTotalInfo(username)
    }
}