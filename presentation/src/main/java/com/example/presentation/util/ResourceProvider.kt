package com.example.presentation.util

interface ResourceProvider {
    fun getString(resId: Int): String
}