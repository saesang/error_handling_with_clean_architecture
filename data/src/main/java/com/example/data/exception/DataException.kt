package com.example.data.exception

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.example.domain.model.Failure
import retrofit2.HttpException
import java.io.FileNotFoundException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


sealed class DataException(message: String, e: Throwable? = null) : Exception() {
    class NetworkError(message: String, e: Throwable? = null) : DataException(message, e)
    class ConnectionError(message: String, e: Throwable? = null) : DataException(message, e)
    class TimeoutError(message: String, e: Throwable? = null) : DataException(message, e)
    class DatabaseError(message: String, e: Throwable? = null) : DataException(message, e)
    class FileIOError(message: String, e: Throwable? = null) : DataException(message, e)
    class MemoryError(message: String, e: Throwable? = null) : DataException(message, e)
    class UnknownError(message: String, e: Throwable? = null) : DataException(message, e)
    class EmptyDataError(message: String, e: Throwable? = null) : DataException(message, e)
    class MappingError(message: String, e: Throwable? = null) : DataException(message, e)

    companion object {
        fun mapToDataException(e: Throwable): DataException {
            return when (e) {
                is ConnectException, is HttpException ->
                    NetworkError("네트워크 관련 오류", e)

                is UnknownHostException ->
                    ConnectionError("인터넷 연결 없음", e)

                is SocketTimeoutException ->
                    TimeoutError("서버 연결 시간 초과", e)

                is SQLiteConstraintException, is SQLiteException ->
                    DatabaseError("데이터베이스 오류", e)

                is FileNotFoundException, is IOException ->
                    FileIOError("파일 I/O 오류", e)

                is OutOfMemoryError ->
                    MemoryError("메모리 부족", e)

                else -> UnknownError("기타 오류", e)
            }
        }

        fun DataException.toFailure(): Failure {
            return Failure.DataError(this)
        }
    }
}