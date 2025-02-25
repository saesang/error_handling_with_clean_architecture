package com.example.presentation.state

import com.example.domain.model.Failure

sealed class UiState<out T>(open val data: T? = null) {
    companion object {
        val initial = None
    }

    data object None : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Error<out T>(
        val message: String?,
        val error: Failure?,
        override val data: T? = null
    ) : UiState<T>(data)
    data class Success<out T>(override val data: T) : UiState<T>(data)
}