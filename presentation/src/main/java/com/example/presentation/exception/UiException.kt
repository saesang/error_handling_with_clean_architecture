package com.example.presentation.exception

import android.content.Context
import android.hardware.camera2.CameraAccessException
import com.example.domain.model.Failure
import com.example.presentation.R
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
        fun mapToUiException(e: Throwable, context: Context): UiException {
            return when (e) {
                is IllegalStateException, is NullPointerException, is IllegalArgumentException ->
                    UiStateError(context.getString(R.string.error_message_ui_state_error), e)

                is IndexOutOfBoundsException ->
                    UiIndexError(context.getString(R.string.error_message_ui_index_error), e)

                is SecurityException ->
                    DeviceSecurityError(context.getString(R.string.error_message_device_security_error), e)

                is CameraAccessException ->
                    CameraAccessError(context.getString(R.string.error_message_camera_access_error), e)

                is BufferOverflowException ->
                    BufferOverflowError(context.getString(R.string.error_message_buffer_overflow_error), e)

                is FileNotFoundException ->
                    FileNotFoundError(null, e)  // 사용자 알림 필요 없음

                is IOException ->
                    DeviceAccessError(context.getString(R.string.error_message_device_access_error), e)

                is OutOfMemoryError ->
                    MemoryError(context.getString(R.string.error_message_memory_error), e)

                else -> UnknownError(context.getString(R.string.error_message_unknown_error), e)
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