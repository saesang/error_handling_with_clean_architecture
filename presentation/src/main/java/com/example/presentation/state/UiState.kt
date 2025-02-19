package com.example.presentation.state

import com.example.domain.model.Failure

sealed class UiState<out T>(val data : T?)  {
    companion object {
        val initial = None
    }
    data object None: UiState<Nothing>(data = null)
    data object Loading : UiState<Nothing>(data = null)
    data class Error<out T>(val error: Failure?, val result : T) : UiState<T>(data = result)
    data class Success<out T>(val result : T) : UiState<T>(data = result)
}