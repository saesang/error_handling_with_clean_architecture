package com.example.data.repositoryImpl

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.data.exception.DataException
import com.example.data.exception.DataException.Companion.toFailure
import com.example.data.mapper.ErrorLogMapper
import com.example.data.room.TodayFortuneDb
import com.example.domain.model.Failure
import com.example.domain.repository.ErrorLogRepository
import javax.inject.Inject

class ErrorLogRepositoryImpl @Inject constructor(
    private val todayFortuneDb: TodayFortuneDb
) : ErrorLogRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun saveErrorLog(failure: Failure) {
        try {
            val errorLogEntity = ErrorLogMapper.mapperToErrorLogEntity(failure)
            todayFortuneDb.dao().insertErrorLog(errorLogEntity)
        } catch (e: Exception) {
            DataException.mapToDataException(e).toFailure()
        }
    }
}