package com.example.domain.usecase

import com.example.domain.model.Failure
import com.example.domain.model.ResultWrapper
import com.example.domain.model.TotalInfoData
import com.example.domain.repository.TotalInfoRepository
import com.example.domain.usecase.validator.UsernameValidator

class GetTotalInfoUseCase(
    private val totalInfoRepository: TotalInfoRepository
) {
    suspend operator fun invoke(username: String): ResultWrapper<TotalInfoData?> {
        // DB 요청, DB에 데이터 없으면 null 반환
        // validation 체크
        val validationResult = UsernameValidator.validate<TotalInfoData?>(username)
        if (validationResult is ResultWrapper.Error) return validationResult

        return totalInfoRepository.getTotalInfo(username)
    }
}