package com.example.presentation.base

import androidx.lifecycle.ViewModel
import com.example.domain.model.Failure
import com.example.domain.usecase.SaveErrorLogUseCase
import com.example.presentation.R
import com.example.presentation.util.ResourceProvider
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var saveErrorLogUseCase: SaveErrorLogUseCase

    open suspend fun handleFailure(failure: Failure): String? {
        // 에러 메세지 반환
        val errorMessage = when (failure) {
            is Failure.NetworkFailure -> resourceProvider.getString(R.string.error_message_network_failure)
            is Failure.ServerFailure -> resourceProvider.getString(R.string.error_message_server_failure)
            is Failure.DatabaseFailure -> resourceProvider.getString(R.string.error_message_database_failure)
            is Failure.ValidationFailure -> failure.message
                ?: resourceProvider.getString(R.string.error_message_validation_failure)

            is Failure.BusinessFailure -> failure.message
                ?: resourceProvider.getString(R.string.error_message_business_failure)

            is Failure.AppCrashFailure -> failure.message
                ?: resourceProvider.getString(R.string.error_message_app_crash_failure)

            is Failure.UiErrorFailure -> failure.message
            is Failure.UiWarningFailure -> failure.message
            is Failure.UnknownFailure -> resourceProvider.getString(R.string.error_message_unknown_failure)
        }

        // 에러 로그 저장
        if (!(failure is Failure.ValidationFailure || failure is Failure.UiWarningFailure || failure is Failure.BusinessFailure)) {
            saveErrorLogUseCase.invoke(failure)
        }

        return errorMessage
    }
}