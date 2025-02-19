package com.example.presentation

import androidx.lifecycle.ViewModel
import com.example.domain.model.Failure
import com.example.domain.model.ResultWrapper
import com.example.domain.model.TotalInfoData
import com.example.domain.usecase.FetchTotalInfoUseCase
import com.example.domain.usecase.GetTotalInfoUseCase
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
) : ViewModel() {
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

    private fun mapFailureToUiState(failure: Failure): UiState.Error<MainHomeUiState> {
        val errorMessage = when (failure) {
            is Failure.NetworkFailure -> "네트워크 연결이 불안정합니다. 연결을 확인해 주세요."
            is Failure.ServerFailure -> "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
            is Failure.DatabaseFailure, is Failure.FileIOFailure -> "데이터를 불러오지 못했습니다. 다시 시도해 주세요."
            is Failure.ValidationFailure, is Failure.BusinessFailure -> "입력값이 잘못되었습니다. 다시 입력해 주세요."
            is Failure.UnknownFailure -> failure.message
        }

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