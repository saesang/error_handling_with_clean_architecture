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


sealed class DataException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NetworkConnectionError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class ServerError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class ClientError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class ConnectionError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class TimeoutError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class DatabaseError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class FileIOError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class MemoryError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class UnknownError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class EmptyDataError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class MappingError(message: String, cause: Throwable? = null) : DataException(message, cause)

    companion object {
        fun mapToDataException(e: Throwable): DataException {
            return when (e) {
                is ConnectException ->
                    NetworkConnectionError("서버 연결 실패", e)

                is HttpException -> {
                    val statusCode = e.code()
                    val errorMessage = "서버 통신 오류(HTTP $statusCode)"

                    when (statusCode) {
                        in 400..499 -> ClientError(errorMessage, e)
                        in 500..599 -> ServerError(errorMessage, e)
                        else -> UnknownError("알 수 없는 서버 오류", e)
                    }
                }

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
            return when(this) {
                is NetworkConnectionError, is ServerError, is ClientError -> Failure.ServerFailure(this)
                is ConnectionError, is TimeoutError -> Failure.NetworkFailure(this)
                is DatabaseError -> Failure.DatabaseFailure(this)
                is FileIOError -> Failure.FileIOFailure(this)
                is EmptyDataError, is MappingError -> Failure.BusinessFailure(this)
                is MemoryError, is UnknownError -> Failure.UnknownFailure(this)
            }
        }
    }
}