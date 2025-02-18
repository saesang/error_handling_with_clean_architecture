package com.example.data.repositoryImpl

import com.example.data.mapper.TotalInfoMapper
import com.example.data.retrofit.ServerApi
import com.example.data.room.TodayFortuneDb
import com.example.domain.model.TotalInfoData
import com.example.domain.repository.TotalInfoRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TotalInfoRepositoryImpl @Inject constructor(
    private val serverApi: ServerApi,
    private val todayFortuneDb: TodayFortuneDb,
) : TotalInfoRepository {
    override suspend fun getTotalInfo(username: String): TotalInfoData? {
        try {
            val totalInfoEntity =
                todayFortuneDb.dao().getTotalInfoByUsername(username).firstOrNull()

            return TotalInfoMapper.mapperToTotalInfoData(totalInfoEntity)
        } catch (e: RuntimeException) {
            return null
        }
    }

    override suspend fun fetchTotalInfo(username: String): TotalInfoData {
        lateinit var returnData: TotalInfoData
        val userInfoResponse = serverApi.fetchUserInfo(username)
        val fortuneInfoResponse = serverApi.fetchFortuneInfo(username)

        return if (userInfoResponse.isSuccessful && fortuneInfoResponse.isSuccessful) {
            val userInfo = userInfoResponse.body()!!
            val fortuneInfo = fortuneInfoResponse.body()!!
            val totalInfoEntity = TotalInfoMapper.mapperToTotalInfoEntity(fortuneInfo, userInfo)
            returnData = TotalInfoMapper.mapperToTotalInfoData(totalInfoEntity)!!
            try {
                todayFortuneDb.dao().insertTotalInfo(totalInfoEntity)
            } catch (e: RuntimeException) {
                return when {
                    e.message?.contains("DB 에러(저장 실패)") == true -> {
                        returnData
                    }

                    else -> {
                        TotalInfoData("", "", 0, "", "")
                    }
                }
            }
            returnData
        } else {
            TotalInfoData("", "", 0, "", "")
        }
    }
}