package com.example.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.presentation.databinding.ActivityMainHomeBinding
import com.example.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainHomeBinding
    private val viewModel: MainHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setBinding()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // mainHomeUiState 관찰
        setupObservers()

        // UI 이벤트 리스너
        setupListeners()

        // 초기 UI 상태 설정
        lifecycleScope.launch {
            viewModel.setInitUiState()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainHomeUiState.collect { state ->
                    when (state) {
                        is UiState.Loading -> showLoadingState()
                        is UiState.Success -> showSuccessState(state.data)
                        is UiState.Error -> showErrorState(state.message, state.data ?: MainHomeUiState())
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showLoadingState() {
        binding.apply {
            progressBar.visibility = VISIBLE
            btnSave.isEnabled = false
            inputName.isEnabled = false
        }
    }

    private fun showSuccessState(uiState: MainHomeUiState) {
        binding.apply {
            progressBar.visibility = GONE
            btnSave.isEnabled = uiState.isBtnSaveEnabled
            inputName.isEnabled = !uiState.isBtnBackVisible
            if (uiState.isUsernameClear) inputName.setText("")
            btnSave.visibility = if (uiState.isBtnSaveVisible) VISIBLE else GONE
            textFortune.visibility = if (uiState.isTextFortuneVisible) VISIBLE else GONE
            btnBack.visibility = if (uiState.isBtnBackVisible) VISIBLE else GONE
            textFortune.text = uiState.totalInfoData?.content ?: "운세 정보 없음"
        }
    }

    private fun showErrorState(errorMessage: String, uiState: MainHomeUiState) {
        binding.apply {
            progressBar.visibility = GONE
            btnSave.isEnabled = uiState.isBtnSaveEnabled
            inputName.isEnabled = !uiState.isBtnBackVisible
            if (uiState.isUsernameClear) inputName.setText("")
            btnSave.visibility = if (uiState.isBtnSaveVisible) VISIBLE else GONE
            textFortune.visibility = if (uiState.isTextFortuneVisible) VISIBLE else GONE
            btnBack.visibility = if (uiState.isBtnBackVisible) VISIBLE else GONE
            textFortune.text = errorMessage
        }
    }

    private fun setupListeners() {
        with(binding) {
            // 유효한 이름 입력 시 '확인' 버튼 활성화
            inputName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val inputName = s.toString()
                    lifecycleScope.launch {
                        viewModel.onTextChanged(inputName)
                    }
                }
            })

            // '확인' 버튼 클릭 시 운세 정보 가져오기
            btnSave.setOnClickListener {
                val username = inputName.text.toString()
                lifecycleScope.launch {
                    viewModel.updateUiState(username)
                }
            }

            // '뒤로가기' 버튼 클릭 시 초기화
            btnBack.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.setInitUiState()
                }
            }
        }
    }

    private fun setBinding() {
        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}