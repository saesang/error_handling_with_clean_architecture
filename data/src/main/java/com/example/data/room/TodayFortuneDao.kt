package com.example.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.room.entity.ErrorLogEntity
import com.example.data.room.entity.TotalInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodayFortuneDao {
    // username이 중복되고 날짜가 오늘이면 DB에서 불러오기
    @Query("SELECT * FROM TotalInfoEntity WHERE username = :username AND date = date('now')")
    fun getTotalInfoByUsername(username: String): Flow<TotalInfoEntity>

    // totalInfo DB에 저장
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTotalInfo(totalInfo: TotalInfoEntity)

    // errorLog DB에 저장
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertErrorLog(errorLog: ErrorLogEntity)
}