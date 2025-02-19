package com.example.domain.model

sealed class Failure(
    val message: String,
    val e: Throwable? = null
) {
    class NetworkFailure (e: Throwable? = null) : Failure(e?.message ?: "네트워크 오류", e)
    class ServerFailure (e: Throwable? = null) : Failure(e?.message ?: "서버 오류", e)
    class DatabaseFailure  (e: Throwable? = null) : Failure(e?.message ?: "DB 오류", e)
    class FileIOFailure  (e: Throwable? = null) : Failure(e?.message ?: "파일 입출력 오류", e)
    class BusinessFailure(e: Throwable? = null) : Failure(e?.message ?: "비즈니스 로직 오류", e)
    class ValidationFailure(message: String = "잘못된 입력값", e: Throwable? = null) : Failure(message, e)
    class UnknownFailure(e: Throwable? = null) : Failure(e?.message ?: "알 수 없는 오류", e)
}