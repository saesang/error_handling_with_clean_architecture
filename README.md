# error-handling-with-clean-architecture
안드로이드 클린 아키텍처 에러 핸들링 방식을 적용한 '오늘의 운세' 프로젝트

<br/>

## 고려 사항
- Crashlytics 핸들러만을 사용해서 앱의 크래시를 잡을 수는 없다 -> 치명적 오류 발생 시 크래시 확인 및 로그를 찍고 앱을 종료시키는 핸들러
- 또한, 클린 아키텍처 구조에서 각 모듈의 에러를 하나의 ExceptionHandler에서 전부 처리하는 것은 좋지 않음

<br/>

## 결론
- 각 모듈에서 발생한 에러는 try-catch 문을 통해 에러 전달 객체로 변환해서 presentation 모듈까지 전달, 최종적으로 UiState.Error()로 변환하여 presentation에서 처리
- 치명적 오류 또는 uncaughtException에 대해서만 application()에서 ExceptionHandler로 처리!

<br/>

## Error Handling Flow

![ErrorHandlingFlow_ver2](https://github.com/user-attachments/assets/e777ad42-530b-4eda-879d-2d1598f688cd)
