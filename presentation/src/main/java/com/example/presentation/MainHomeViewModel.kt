package com.example.presentation

import android.content.Context
import com.example.domain.model.Failure
import com.example.domain.model.ResultWrapper
import com.example.domain.model.TotalInfoData
import com.example.domain.usecase.FetchTotalInfoUseCase
import com.example.domain.usecase.GetTotalInfoUseCase
import com.example.presentation.base.BaseViewModel
import com.example.presentation.state.UiState
import com.example.presentation.util.TextValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainHomeViewModel @Inject constructor(
    private val getTotalInfoUseCase: GetTotalInfoUseCase,
    private val fetchTotalInfoUseCase: FetchTotalInfoUseCase
) : BaseViewModel() {
    private val _mainHomeUiState =
        MutableStateFlow<UiState<MainHomeUiState>>(UiState.Success(MainHomeUiState()))
    val mainHomeUiState = _mainHomeUiState.asStateFlow()

    private val initialState = MainHomeUiState()

    fun setInitUiState() {
        _mainHomeUiState.value = UiState.Success(initialState)
    }

    fun onTextChanged(inputName: String) {
        val state = initialState.copy(
            isUsernameClear = false,
            isBtnSaveEnabled = TextValidator.isValidText(inputName)
        )
        _mainHomeUiState.value = UiState.Success(state)
    }

    suspend fun updateUiState(username: String) {
        _mainHomeUiState.value = UiState.Loading

        val result = getTotalInfoUseCase.invoke(username)

        val finalResult = when (result) {
            is ResultWrapper.Success -> result.data?.let { result } ?: fetchTotalInfoUseCase.invoke(
                username
            )

            is ResultWrapper.Error -> result
        }

        _mainHomeUiState.value = when (finalResult) {
            is ResultWrapper.Success -> UiState.Success(
                initialState.copy(
                    isUsernameClear = false,
                    isBtnSaveVisible = false,
                    isTextFortuneVisible = true,
                    isBtnBackVisible = true,
                    totalInfoData = result.data
                )
            )

            is ResultWrapper.Error -> mapFailureToUiState(finalResult.error)
        }
    }

    suspend fun mapFailureToUiState(failure: Failure): UiState.Error<MainHomeUiState> {
        val errorMessage = handleFailure(failure)

        val errorState = initialState.copy(
            isUsernameClear = false,
            isBtnSaveVisible = false,
            isTextFortuneVisible = true,
            isBtnBackVisible = true,
            totalInfoData = null
        )
        return UiState.Error(errorMessage, failure, errorState)
    }
}

data class MainHomeUiState(
    val isUsernameClear: Boolean = true,
    val isBtnSaveVisible: Boolean = true,
    val isBtnSaveEnabled: Boolean = false,
    val isBtnBackVisible: Boolean = false,
    val isTextFortuneVisible: Boolean = false,
    val totalInfoData: TotalInfoData? = null
)