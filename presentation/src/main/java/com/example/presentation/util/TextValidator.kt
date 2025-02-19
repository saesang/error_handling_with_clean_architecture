package com.example.presentation.util

object TextValidator {
    fun isValidText(input: String): Boolean {
        if (input.isEmpty() || input.isBlank()) return false

        val alphabetRegex = Regex("^[a-zA-Z]+( [a-zA-Z]+)?\$") // 영문
        val koreanRegex = Regex("^[가-힣]+$") // 한글

        return alphabetRegex.matches(input) || koreanRegex.matches(input)
    }
}
