package com.example.domain.model

sealed class Failure(
    val message: String?,
    val e: Throwable? = null
) {
    // ValidationFailure, UiWarningFailure는 ErrorLog 저장 X
    class NetworkFailure(e: Throwable? = null) : Failure(e?.message ?: "네트워크 오류", e)
    class ServerFailure(e: Throwable? = null) : Failure(e?.message ?: "서버 오류", e)
    class DatabaseFailure(e: Throwable? = null) : Failure(e?.message ?: "DB 오류", e)
    class BusinessFailure(e: Throwable? = null) : Failure(e?.message ?: "비즈니스 로직 오류", e)
    class ValidationFailure(message: String? = "잘못된 입력값입니다. 올바른 값을 입력해 주세요", e: Throwable? = null) : Failure(e?.message ?: message, e)
    class UiErrorFailure(e: Throwable? = null) : Failure(e?.message, e)
    class UiWarningFailure(e: Throwable? = null) : Failure(e?.message, e)
    class AppCrashFailure(e: Throwable? = null) : Failure(e?.message ?: "치명적 오류", e)
    class UnknownFailure(e: Throwable? = null) : Failure(e?.message ?: "알 수 없는 오류", e)
}