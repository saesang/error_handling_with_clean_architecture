package com.example.domain.usecase

import com.example.domain.model.Failure
import com.example.domain.repository.ErrorLogRepository

class SaveErrorLogUseCase(
    private val errorLogRepository: ErrorLogRepository
) {
    suspend operator fun invoke(failure: Failure) {
        return errorLogRepository.saveErrorLog(failure)
    }
}