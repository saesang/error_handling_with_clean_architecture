package com.example.domain.model

sealed class ResultWrapper<out T>(
    open val data: T? = null
) {
    data class Success<T>(override val data: T) : ResultWrapper<T>(data)
    data class Error<T>(val error: Failure, override val data: T? = null) : ResultWrapper<T>(data)
}
