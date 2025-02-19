package com.example.domain.usecase.validator

import com.example.domain.model.Failure
import com.example.domain.model.ResultWrapper

object UsernameValidator {
    fun <T> validate(username: String): ResultWrapper<T> {
        if (username.isBlank()) {
            return ResultWrapper.Error(Failure.ValidationFailure("이름을 입력해 주세요."))
        }
        if (!username.matches(Regex("^[a-zA-Z가-힣]+$"))) {
            return ResultWrapper.Error(Failure.ValidationFailure("한글 또는 영문 이름을 입력해 주세요!"))
        }
        return ResultWrapper.Success(Unit as T) // ✅ 성공 시 Unit 반환
    }
}