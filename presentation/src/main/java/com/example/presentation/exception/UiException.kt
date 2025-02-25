package com.example.presentation.exception

import android.hardware.camera2.CameraAccessException
import com.example.domain.model.Failure
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.BufferOverflowException


sealed class UiException(alert: String?, cause: Throwable? = null) : Exception(alert, cause) {
    class UiStateError(alert: String, cause: Throwable? = null) : UiException(alert, cause)
    class UiIndexError(alert: String, cause: Throwable? = null) : UiException(alert, cause)
    class DeviceSecurityError(alert: String, cause: Throwable? = null) : UiException(alert, cause)
    class CameraAccessError(alert: String, cause: Throwable? = null) : UiException(alert, cause)
    class DeviceAccessError(alert: String, cause: Throwable? = null) : UiException(alert, cause)
    class MemoryError(alert: String, cause: Throwable? = null) : UiException(alert, cause)
    class BufferOverflowError(alert: String, cause: Throwable? = null) : UiException(alert, cause)
    class FileNotFoundError(alert: String?, cause: Throwable? = null) : UiException(alert, cause)
    class UnknownError(alert: String, cause: Throwable? = null) : UiException(alert, cause)

    companion object {
        fun mapToUiException(e: Throwable): UiException {
            return when (e) {
                is IllegalStateException, is NullPointerException, is IllegalArgumentException ->
                    UiStateError("예상치 못한 오류가 발생했습니다. 새로고침을 시도해 주세요", e)

                is IndexOutOfBoundsException ->
                    UiIndexError("데이터를 불러오는 중 문제가 발생했습니다. 고객센터로 문의해 주세요", e)

                is SecurityException ->
                    DeviceSecurityError("기기 접근 권한이 필요합니다. '설정'에서 기기 접근을 허용해 주세요", e)

                is CameraAccessException ->
                    CameraAccessError("카메라를 사용할 수 없습니다. 실행 중인 다른 앱을 종료한 후, 다시 시도해 주세요", e)

                is BufferOverflowException ->
                    BufferOverflowError("출력 데이터가 너무 많아 프린터가 응답하지 않습니다. 잠시 후 다시 시도해 주세요", e)

                is FileNotFoundException ->
                    FileNotFoundError(null, e)

                is IOException ->
                    DeviceAccessError("연결할 기기를 찾을 수 없습니다. 고객센터로 문의해 주세요", e)

                is OutOfMemoryError ->
                    MemoryError("시스템 오류가 발생했습니다. 새로고침을 시도해 주세요", e)

                else -> UnknownError("예상치 못한 오류가 발생했습니다. 새로고침을 시도해 주세요", e)
            }
        }

        fun UiException.toFailure(): Failure {
            return when (this) {
                is UiStateError, is MemoryError -> Failure.AppCrashFailure(this)
                is BufferOverflowError, is DeviceAccessError, is FileNotFoundError, is UiIndexError -> Failure.UiErrorFailure(this)
                is DeviceSecurityError, is CameraAccessError -> Failure.UiWarningFailure(this)
                is UnknownError -> Failure.UnknownFailure(this)
            }
        }
    }
}