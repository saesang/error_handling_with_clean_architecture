package com.example.data.dto

object DummyData {
    val fortuneInfoList = listOf(
        FortuneInfoDto(
            date = "2025-02-17",
            content = "오늘은 당신에게 긍정적인 일이 많이 생길 것입니다. 새로운 사람과의 만남에서 좋은 기회를 얻을 가능성이 높습니다.",
            garbageData = "random1",
            garbageData2 = "randomA"
        ),
        FortuneInfoDto(
            date = "2025-02-17",
            content = "조심스럽게 행동하고 현재 상황을 잘 관찰하면, 예상하지 못한 행운이 당신에게 찾아올 가능성이 큽니다.",
            garbageData = "random2",
            garbageData2 = "randomB"
        ),
        FortuneInfoDto(
            date = "2025-02-17",
            content = "새로운 도전을 시작하기에 아주 좋은 날입니다. 용기를 내어 첫걸음을 내딛으면 큰 성과를 거둘 수 있습니다.",
            garbageData = "random3",
            garbageData2 = "randomC"
        ),
        FortuneInfoDto(
            date = "2025-02-17",
            content = "긍정적인 마음으로 하루를 시작하면, 어려운 상황에서도 해결책을 찾을 수 있는 기회가 열릴 것입니다.",
            garbageData = "random4",
            garbageData2 = "randomD"
        )
    )

    val userInfoList = listOf(
        UserInfoDto(
            username = "김용혁",
            age = 30,
            personality = "긍정적",
            job = "개발자"
        ),
        UserInfoDto(
            username = "이재욱",
            age = 27,
            personality = "신중함",
            job = "디자이너"
        ),
        UserInfoDto(
            username = "안수민",
            age = 25,
            personality = "열정적",
            job = "마케터"
        ),
        UserInfoDto(
            username = "박세하",
            age = 28,
            personality = "창의적",
            job = "작가"
        )
    )
}