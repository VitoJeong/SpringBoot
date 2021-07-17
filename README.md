
# SpringBoot
> Spring Boot 공부를 위한 리포지토리

[데어 프로그래밍](https://www.youtube.com/channel/UCVrhnbfe78ODeQglXtT1Elw/featured)님의 강좌를 통해 Spring Boot를 비롯한 관련 기술들을 배우기 위한 프로젝트들입니다.

### 개발환경
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

# instagram
> Spring MVC, Spring Security, JPA(Hibernate), JSP, JSTL 활용해 인스타그램을 구현 

### 기존 프로젝트와 다르게 구현한 점

1. Entity Id의 타입을 `int`가 아닌 `Long`로 변경하여 구현
2. DI 사용시 필드주입 -> 생성자주입 방식으로 변경하여 구현

# security1
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
![F79C843E-48BA-4391-9848-46C2269C20A3](https://user-images.githubusercontent.com/63029576/125806434-6b34335d-5239-4d11-b43e-365c8e85b730.jpeg)

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
    
# jwt
> Spring security와 JWT를 활용해 로그인 기능을 구현

## 세션 저장소의 단점

* 세션저장소에 장애가 일어나면, 시스템 전체가 문제가 생긴다.
* 만약 메모리에 세션 정보가 들어있다면, 메모리가 많이 사용될 수 있다.
* 클라이언트의 요청이 있을때마다 권한을 검증하기 위해, 매번 세션저장소에 세션 데이터를 조회해야 한다.

## JWT
> JSON 객체를 통해 안전하게 정보를 전송할 수 있는 웹표준(RFC7519)

JWT(Json Web Token)은 토큰 기반 인증 방식으로, 클라이언트의 세션 상태를 저장하는 게 아니라 필요한 정보를 토큰 body에 저장해 클라이언트가 가지고 있고 그것을 증명서처럼 사용한다.

### JWT의 기본 구성
* Header — **토큰의 유형**이나 `HMAC SHA256` 또는 `RSA`와 같이 **사용되는 해시 알고리즘**이 무엇으로 사용했는지 등 정보가 담긴다. Base64Url로 인코딩되어있다.
* Payload — **클라이언트에 대한 정보**나, **meta Data**같은 내용이 들어있고, `Base64Url`로 인코딩되어있다.
* Signature — header에서 지정한 알고리즘과 **secret 키**, 서명으로 payload와 header를 담는다.

### JWT 보안 위험
JWT는 자체 내에 정보를 가지고 있기 때문에 클라이언트가 해독해 정보를 볼 수 있다. 하지만 받는 자가 secret 키를 알고 있어야만 수정이 가능하다.

### JWT의 저장
웹에서는 2가지의 저장 방식이 있다.

* Cookies
* local/session storage.

## 기본 인증 과정
![tokenflow](https://user-images.githubusercontent.com/63029576/126043162-90f9770d-b4d6-4d2e-a3dd-dae236363020.png)

1. 클라이언트가 로그인을 요청한다.
2. 서버는 요청받은 ID/PW를 검증하고 토큰을 생성한다.
3. 생성한 토큰을 클라이언트에게 전달하면서 저장시킨다.
4. 이후 클라이언트가 모든 요청을 할 때 토큰을 Header에 포함시킨다.
5. 서버는 토큰을 해독해 만료나 변조 여부를 확인하여 응답을 반환한다.
6. 기한이 만료되었으면 토큰을 지워주고 재로그인을 하게 한다.

### Spring Security의 토큰 인증 과정
 ![spring-boot-jwt-mysql-spring-security-architecture](https://user-images.githubusercontent.com/63029576/126043105-7d2b1f00-befb-4af5-963f-da4d3efd0288.png)

1. HTTP 리퀘스트 요청은 그대로 인증 필터로 들어온다.
2. 여기서 토큰을 체크하는 토큰 유효성 검사가 들어가고 토큰이 없다면 컨트롤러에게 토큰 발급을 요청한다.
3. 토큰 발급 요청을 받으면 `Token Provider`가 위치한 필터 체인의 `Authentication Manager`로 요청이 넘어간다.
4. `Authentication Manager`는 `UserDetailsService`를 통해 객체 정보(ID/PW)를 저장/검증하고 다시 컨트롤러에게 정보를 넘긴다.
5. 컨트롤러는 받은 정보를 통해 토큰을 발급받는다.
6. 발급된 토큰을 컨트롤러가 다시 유저에게 넘긴다.

## JWT의 문제점
* 클라이언트가 계속 시스템을 이용하다가 access 토큰 기한이 만료된다면 사용중에 갑자기 로그인을 하라고 할 것이다.
* 수명이 너무 짧다면 만료될때마다 로그인 해야하고, 수명이 너무 길면 해커에게 해독되어 사용될 가능성이 높아진다.
