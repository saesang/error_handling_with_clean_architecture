package com.example.data.mapper

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.data.room.entity.ErrorLogEntity
import com.example.domain.model.ErrorLogData
import com.example.domain.model.Failure
import retrofit2.HttpException

// failure <-> entity
object ErrorLogMapper {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun mapperToErrorLogEntity(failure: Failure): ErrorLogEntity {
        return if (failure.e?.cause is HttpException) {
            val error = failure.e?.cause as HttpException
            ErrorLogEntity(
                id = 0,
                code = error.code(),
                message = failure.message ?: "알 수 없는 오류"
            )
        } else {
            ErrorLogEntity(
                id = 0,
                code = -1,
                message = failure.message ?: "알 수 없는 오류"
            )
        }
    }

    fun mapperToErrorLogData(errorLogEntity: ErrorLogEntity): ErrorLogData {
        return ErrorLogData(
            code = errorLogEntity.code,
            message = errorLogEntity.message
        )
    }
}