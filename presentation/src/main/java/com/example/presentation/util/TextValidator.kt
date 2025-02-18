package com.example.presentation.util

object TextValidator {
    fun isValidText(input: String): Boolean {
        if (input.isEmpty()) return false
        if (input.isBlank()) return false

        val alphabetRegex = Regex("^[a-zA-Z]+( [a-zA-Z]+)?\$")
        if (alphabetRegex.matches(input)) return true

        val koreanRegex = Regex("^[가-힣]+$")
        if (koreanRegex.matches(input)) return true

        return false
    }
}