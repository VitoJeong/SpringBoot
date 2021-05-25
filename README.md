
# SpringBoot
> Spring Boot 공부를 위한 리포지토리

[데어 프로그래밍](https://www.youtube.com/channel/UCVrhnbfe78ODeQglXtT1Elw/featured)님의 강좌를 통해 Spring Boot를 비롯한 관련 기술들을 배우기 위한 프로젝트들입니다.

![](../header.png)

## 개발환경
* 개발도구
  * STS 4
  * MySQL WorkBench 
  <br>
* 사용기술
  * JAVA `JDK 11`
  * Spring Boot `2.*`
  * Spring Security
  * Maven `4.0`
  <br>
* 서버(WAS)
  * Apache Tomcat
  <br>
* 커뮤니티
  * Github
```

## 사용 예제

스크린 샷과 코드 예제를 통해 사용 방법을 자세히 설명합니다.

_더 많은 예제와 사용법은 [Wiki][wiki]를 참고하세요._

## 개발 환경 설정

모든 개발 의존성 설치 방법과 자동 테스트 슈트 실행 방법을 운영체제 별로 작성합니다.

```sh
make install
npm test
```

## 기존 프로젝트와 다르게 구현한 점

1. Entity Id의 타입을 `int`가 아닌 `Long`로 변경하여 구현
2. DI 사용시 필드주입 -> 생성자주입 방식으로 변경하여 구현
