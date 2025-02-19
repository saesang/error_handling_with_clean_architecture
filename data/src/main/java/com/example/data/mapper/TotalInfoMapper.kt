package com.example.data.mapper

import com.example.data.dto.FortuneInfoDto
import com.example.data.dto.UserInfoDto
import com.example.data.room.entity.TotalInfoEntity
import com.example.domain.model.TotalInfoData

// dto -> entity -> model
object TotalInfoMapper {
    fun mapperToTotalInfoEntity(fortuneDto: FortuneInfoDto, userInfoDto: UserInfoDto): TotalInfoEntity {
        return TotalInfoEntity(
            date = fortuneDto.date,
            username = userInfoDto.username,
            age = userInfoDto.age,
            personality = userInfoDto.personality,
            content = fortuneDto.content
        )
    }

    fun mapperToTotalInfoData(totalInfoEntity: TotalInfoEntity?): TotalInfoData? {
        return if (totalInfoEntity == null) {
            null
        } else {
            TotalInfoData(
                date = totalInfoEntity.date,
                username = totalInfoEntity.username,
                age = totalInfoEntity.age,
                personality = totalInfoEntity.personality,
                content = totalInfoEntity.content
            )
        }
    }
}