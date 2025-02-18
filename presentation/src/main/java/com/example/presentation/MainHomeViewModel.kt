package com.example.presentation

import androidx.lifecycle.ViewModel
import com.example.domain.model.TotalInfoData
import com.example.domain.model.isEmpty
import com.example.domain.usecase.FetchTotalInfoUseCase
import com.example.domain.usecase.GetTotalInfoUseCase
import com.example.presentation.state.BaseState
import com.example.presentation.state.MainHomeUiState
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
    private val _mainHomeUiState = MutableStateFlow(BaseState.Success(MainHomeUiState()))
    val mainHomeUiState = _mainHomeUiState.asStateFlow()

    private val initialState = MainHomeUiState()

    suspend fun setInitUiState() {
        _mainHomeUiState.value = BaseState.Success(initialState)
    }

    suspend fun onTextChanged(inputName: String) {
        val state = initialState.copy(isUsernameClear = false, isBtnSaveEnabled = TextValidator.isValidText(inputName))
        _mainHomeUiState.value = BaseState.Success(state)
    }

    suspend fun updateUiState(username: String) {
        lateinit var state: MainHomeUiState

        // Db에서 조회한 후, 데이터 값이 없으면(null) 서버에 요청
        val totalInfoData: TotalInfoData = getTotalInfoUseCase.invoke(username) ?: fetchTotalInfoUseCase.invoke(username)

        if (totalInfoData.isEmpty()) {
            val newTotalInfoDate = TotalInfoData("", username, 0, "", "현재 운세를 불러올 수 없습니다. 다시 시도해 주세요")
            state = initialState.copy(isUsernameClear = false, isBtnSaveVisible = false, isTextFortuneVisible = true, isBtnBackVisible = true, totalInfoData = newTotalInfoDate)
        } else {
            state = initialState.copy(isUsernameClear = false, isBtnSaveVisible = false, isTextFortuneVisible = true, isBtnBackVisible = true, totalInfoData = totalInfoData)
        }

        _mainHomeUiState.value = BaseState.Success(state)
    }
}