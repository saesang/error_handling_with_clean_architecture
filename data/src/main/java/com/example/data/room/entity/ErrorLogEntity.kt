package com.example.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ErrorLogEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "code") val code: Int,
    @ColumnInfo(name = "message") val message: String
)