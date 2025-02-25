package com.example.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.room.entity.ErrorLogEntity
import com.example.data.room.entity.TotalInfoEntity

@Database(
    entities = [TotalInfoEntity::class, ErrorLogEntity::class],
    version = 2
)
abstract class TodayFortuneDb : RoomDatabase() {
    abstract fun dao(): TodayFortuneDao
}