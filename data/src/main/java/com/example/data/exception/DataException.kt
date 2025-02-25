package com.example.data.exception

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.example.domain.model.Failure
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


sealed class DataException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NetworkConnectionError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class ServerError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class ClientError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class ConnectionError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class ParseError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class DatabaseError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class TimeoutError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class EmptyDataError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class UnknownUserError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class InvalidMeasurementError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class InvalidInputError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class MappingError(message: String, cause: Throwable? = null) : DataException(message, cause)
    class UnknownError(message: String, cause: Throwable? = null) : DataException(message, cause)

    companion object {
        fun mapToDataException(e: Throwable): DataException {
            return when (e) {
                is ConnectException, is SocketTimeoutException ->
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

                is JsonSyntaxException, is JsonParseException ->
                    ParseError("데이터 파싱 오류", e)

                is SQLiteConstraintException, is SQLiteException ->
                    DatabaseError("데이터베이스 오류", e)

                is TimeoutException ->
                    TimeoutError("응답 시간 초과", e)

                else -> UnknownError("기타 오류", e)
            }
        }

        fun DataException.toFailure(): Failure {
            return when(this) {
                is NetworkConnectionError, is ServerError, is ClientError -> Failure.ServerFailure(this)
                is ConnectionError, is TimeoutError -> Failure.NetworkFailure(this)
                is DatabaseError, is ParseError -> Failure.DatabaseFailure(this)
                is InvalidInputError -> Failure.ValidationFailure("입력값 오류", this)
                is InvalidMeasurementError -> Failure.ValidationFailure("측정값 오류", this)
                is UnknownUserError, is EmptyDataError, is MappingError -> Failure.BusinessFailure(this)
                is UnknownError -> Failure.UnknownFailure(this)
            }
        }
    }
}