
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

## 기존 프로젝트와 다르게 구현한 점

1. Entity Id의 타입을 `int`가 아닌 `Long`로 변경하여 구현
2. DI 사용시 필드주입 -> 생성자주입 방식으로 변경하여 구현

# Spring Security
> Spring security와 OAuth2를 활용해 로그인 기능을 구현


## Spring Security란?
* 커스터마이징이 가능한 인증 및 액세스 제어 프레임워크이다.
* Spring 기반 application의 보안 표준이라고 할 수 있다.
* Java application의 인증을 제공하는 프레임워크이다.
* 모든 Spring 프로젝트와 마찬가지로 Spring Security는 맞춤형 요구사항을 충족하기 위해 쉽게 확장 가능하다.

### Spring Security의 주요 기능
1. Basic Access Authentication
    Spring Security는 네트워크를 통해 요청하는 동안 사용자 이름과 비밀번호를 제공하는 데 사용되는 기본 액세스 인증을 지원함.
2. Authorization
    리소스에 접근하기 전에 사용자에게 권한을 부여하는 기능을 제공한다.
    개발자가 리소스에 대한 액세스 정책을 정의 할 수 있다.

### Spring Security의 처리 과정
1. HTTP 요청 수신 (Http Request) 및 AuthenticationFilter 통과
    * Spring Security 는 일련의(연결된) 필터 들을 가지고 있다.
    * 요청(request)은, 인증(Authentication)과 권한부여(Authorization)를 위해 이 필터들을 통과하게 된다.
    * 이 필터를 통과하는 과정은, 해당 요청과 관련된 인증 필터(인증 메커니즘/모델에 기반한 관련 인증 필터)를 찾을 때 까지 지속된다.
    <br>
    예)
    <br>
    HTTP Basic - `BasicAuthenticationFilter` 
    <br>
    로그인 form submit - `UsernamePasswordAuthenticationFilter`

2. 사용자 자격 증명을 기반으로 `AuthenticationToken` 생성
    * 인증 요청이 관련 `AuthenticationFilter`에 의해 수신되면 수신된 요청에서 사용자 이름과 비밀번호를 추출한다.
    * 추출된 사용자 자격 증명(credentials)을 기반으로 인증개체를 만든다
    * 추출된 자격 증명(credentials)을 통해 `UsernamePasswordAuthenticationToken`이 생성된다.

3. `AuthenticationManager`를 위해 생성된 `AuthenticationToken` 위임
    * 만들어진 `UsernamePasswordAuthenticationToken`는 `AuthenticaionManager`의 인증 메서드를 호출하는데 사용됨
    * `AuthenticaionManager`는 단순한 인터페이스이며 실제 구현은 `ProviderManager` 이다.

  ```
  public interface AuthenticationManager {
      Authenticaion authenticate(Authentication authentication) throw AuthenticaionException;
  }
  ```

    * `ProviderManager`에는 사용자 요청을 인증에 필요한 `AuthenticationProvdier` 목록이 있다.
    * `ProviderManager`는 제공된 각 `AuthenticationProvdier`를 살펴보고 전달된 인증 개체(`UsernamePasswordAuthenticationToken`)를 기반으로 사용자 인증을 시도한다.

4. `AuthenticationProvier` 목록으로 인증 시도
    * `AuthenticationProvider` 는 제공된 인증 개체로 사용자를 인증한다
    ```
    public interface AuthenticationProvider {
        Authentication authenticate(Authentication authentication) throws AuthenticationException;
        boolean supports(Class<?> authentication);
    }
    ```
    즉, `authenticate` 메소드는 통과하는 무언가가 지원되는 형식(`Class<? extends Authentication>`)인지 확인한다.

5. `UserDetailsService` (옵션 - customize)
    * 일부 `AuthenticationProvider`는 사용자 이름(username)을 기반으로 사용자 세부 정보를 검색하기 위해 `UserDetailSesrvice를` 사용할 수 있다.
    ```
    public interface UserDetailService {
        UserDetail loadUserByUsername(String username) throws UsernameNotFoundException;
    }
    ```
6. `UserDetails` (옵션 - customize)

7. `User` (옵션 - customize)
    * 5번의 예제 코드에 사용된 `UserDetail`은 사용자 정보(`User`)를 담고 있는 인터페이스이면 될 것이다.

8. `AuthenticationException`
    * `AuthenticationProvider` 인터페이스에 의해 사용자가 성공적으로 인증되면 완전히 채워진 인증개체가 반환된다.
    * 그렇지 않으면 `AuthenticationException가` 발생한다.
    * `AuthenticationException`가 발생하면 인증 메커니즘을 지원하는 `AuthenticationEntryPoint`에 의해 처리된다.

9. 인증 완료!
    * `AuthenticationManager`는 획득한 완전히 채워진 인증개체를 관련 인증 필터로 다시 반환한다.

10. `SecurityContext` 에서 인증 개체 설정
    * 관련 `AuthenticationFilter`는 향후 필터 사용을 위해 획득한 인증 개체를 `SecurityContext`에 저장한다.
    ```
    SecurityContextHolder.getContext().setAuthentication(authentication);
    ```

## OAuth 2.0
 OAuth(Open Authorization, Open Authentication)는 사용자 리소스를 관리하는 서비스(구글, 페이스북 등)에서 **제3의 애플리케이션에게 사용자의 패스워드 제공 없이 인증, 인가할 수 있는 인증 관련 표준 프로토콜**이다. OAuth 이전에 사용자의 권한을 위임받는 방식은 사용자가 이용하는 서비스의 계정/패스워드를 제공받는 방식이었다. 이는 패스워드 유출 뿐 아니라 권한을 위임받는 애플리케이션이 필요 이상으로 계정에 대한 모든 권한을 획득하게 되는 등 다양한 문제점이 존재한다. OAuth 인증은 API를 제공하는 서버에서 사용자 인증 및 권한 부여를 진행하고 이에 대한 'Access Token'을 발급하는 방식을 제공하며 이러한 문제들을 해결할 수 있다.  
 OAuth2는 OAuth1 프로토콜의 복잡한 인증방식을 단순화 한 것이 가장 큰 특징이다.

### OAuth2 기본 용어


* Resource-Owner : 리소스 소유권을 가진 사람(User)
* Resource-Server : 리소스가 위치한 서버 (ex. google, Kakao)
* Client : 리소스를 요청하는 애플리케이션
* Authorization-Server : 클라이언트 인증, 토큰 발행 등의 역할을 담당하는 서버
* Authorized Redirect Uri : Client 측에서 등록하는 Url. 만약 이 Uri로부터 인증을 요구하는 것이 아니라면, Resource Server는 해당 요청을 무시한다.
* Token : Authorization-Server에 의해 생성되고, 클라이언트의 요청 시점에 발행되는 랜덤 문자열을 의미하며 2가지 타입이 있다.
    1. Access-Token
    : 리소스에 대한 접근을 허용하는 토큰이며, 헤더나 파라미터 등에 담겨 리소스 서버로의 요청에 포함되어야 한다. 만료일이 있으며, Authorization-Server에 의해 정의된다
    2. Refresh-Token(optional)
    : Access-Token 이 만료되기 전에 Authorization-Server에 전송해 토근 기간을 연장하는 데 사용된다.
    OAuth2 Type 중에는 Refresh-Token을 허용하지 않는 경우도 있다.
