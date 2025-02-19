package com.example.domain.model

sealed class Failure(val e: Throwable? = null) {
    data class DataError(val error: Throwable? = null) : Failure(error)
    data class BusinessError(val message: String) : Failure()
    data class ValidationError(val message: String) : Failure()
}