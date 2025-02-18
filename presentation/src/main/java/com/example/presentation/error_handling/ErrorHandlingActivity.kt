package com.example.presentation.error_handling

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.presentation.R
import com.example.presentation.databinding.ActivityErrorHandlingBinding

class ErrorHandlingActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_INTENT = "extra_intent"
        const val EXTRA_ERROR_TEXT = "extra_error_text"
    }

    private lateinit var binding: ActivityErrorHandlingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setBinding()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val originalIntent = intent.getParcelableExtra<Intent>(EXTRA_INTENT)    // 이전 activity 정보
        val errorText = intent.getStringExtra(EXTRA_ERROR_TEXT) // 에러 내용

        with(binding) {
            txtErrorDetail.text = errorText
            btnErrorHandling.setOnClickListener {
                originalIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)  // 이전 activity 새로 시작
                startActivity(originalIntent)
                finish()
            }
        }
    }

    private fun setBinding() {
        binding = ActivityErrorHandlingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}