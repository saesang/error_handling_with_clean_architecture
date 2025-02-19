package com.example.data.repositoryImpl

import com.example.data.exception.DataException
import com.example.data.exception.DataException.Companion.toFailure
import com.example.data.mapper.TotalInfoMapper
import com.example.data.retrofit.ServerApi
import com.example.data.room.TodayFortuneDb
import com.example.domain.model.ResultWrapper
import com.example.domain.model.TotalInfoData
import com.example.domain.repository.TotalInfoRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TotalInfoRepositoryImpl @Inject constructor(
    private val serverApi: ServerApi,
    private val todayFortuneDb: TodayFortuneDb,
) : TotalInfoRepository {
    override suspend fun getTotalInfo(username: String): ResultWrapper<TotalInfoData?> {
        try {
            val totalInfoEntity =
                todayFortuneDb.dao().getTotalInfoByUsername(username).firstOrNull()

            return ResultWrapper.Success(TotalInfoMapper.mapperToTotalInfoData(totalInfoEntity))
        } catch (e: Exception) {
            return ResultWrapper.Error(DataException.mapToDataException(e).toFailure())
        }
    }

    override suspend fun fetchTotalInfo(username: String): ResultWrapper<TotalInfoData> {
        return try {
            val userInfoResponse = serverApi.fetchUserInfo(username)
            val fortuneInfoResponse = serverApi.fetchFortuneInfo(username)

            if (!userInfoResponse.isSuccessful || !fortuneInfoResponse.isSuccessful) {
                return ResultWrapper.Error(DataException.NetworkConnectionError("서버 응답 실패").toFailure())
            }

            val userInfo =
                userInfoResponse.body() ?: return ResultWrapper.Error(DataException.EmptyDataError("UserInfo 데이터가 비어 있음").toFailure())
            val fortuneInfo = fortuneInfoResponse.body()
                ?: return ResultWrapper.Error(DataException.EmptyDataError("FortuneInfo 데이터가 비어 있음").toFailure())

            val totalInfoEntity = TotalInfoMapper.mapperToTotalInfoEntity(fortuneInfo, userInfo)
            val returnData = TotalInfoMapper.mapperToTotalInfoData(totalInfoEntity)
                ?: return ResultWrapper.Error(DataException.MappingError("TotalInfoData 변환 실패").toFailure())

            try {
                todayFortuneDb.dao().insertTotalInfo(totalInfoEntity)
            } catch (e: Exception) {
                DataException.mapToDataException(e).toFailure()
            }

            ResultWrapper.Success(returnData)

        } catch (e: Exception) {
            return ResultWrapper.Error(DataException.mapToDataException(e).toFailure())
        }
    }
}