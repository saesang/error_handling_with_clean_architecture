package com.example.domain.model

data class TotalInfoData(
    val date: String,
    val username: String,
    val age: Int,
    val personality: String,
    val content: String
)

fun TotalInfoData.isEmpty(): Boolean {
    return this.date.isEmpty() &&
            this.username.isEmpty() &&
            this.age == 0 &&
            this.personality.isEmpty() &&
            this.content.isEmpty()
}