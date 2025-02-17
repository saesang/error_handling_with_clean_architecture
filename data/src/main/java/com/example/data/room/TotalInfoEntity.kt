package com.example.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TotalInfoEntity (
    @PrimaryKey val username: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "age") val age: Int,
    @ColumnInfo(name = "personality") val personality: String,
    @ColumnInfo(name = "content") val content: String
)