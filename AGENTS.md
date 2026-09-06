# 개발 지침

## 프로젝트 구조 및 모듈 구성

1. 이 지침은 Java, Gradle, Spring Boot, Spring MVC, Thymeleaf, MyBatis, H2 기반 프로젝트를 대상으로 합니다.
2. 테스트는 JUnit 5와 AssertJ를 사용합니다. 
3. 실제 기술 버전은 대상 프로젝트의 빌드 파일(`build.gradle` 또는 `build.gradle.kts`)과 Gradle 래퍼 설정을 기준으로 확인합니다.

## 파일 저장 규칙
1. 운영 코드는 `src/main/java`에서 `{기본 패키지}` 아래에 배치합니다. 
2. 설정, 템플릿 및 기타 런타임 리소스는 해당 모듈의 `src/main/resources`에 둡니다.
3. 테스트는 `src/test/java` 아래에서 운영 코드의 패키지 구조를 그대로 따릅니다.
4. `build/`, `bin/`, `.gradle/`, 폴더는 생성 결과물이 저장됩니다. 

### 패키지 및 클래스 이름 규칙

`src/main/java`는 다음 구조를 기준으로 구성합니다. 
아직 사용하지 않는 디렉터리는 미리 만들지 않고, 관련 기능을 추가할 때 생성합니다.

```text
{기본 패키지}
├── controller/   # Thymeleaf 화면 요청 처리를 위한 컨트롤러 클래스가 위치
│   └── api/      # REST API 요청 처리를 위한 컨트롤러 클래스가 위치
├── service/      # 비즈니스 로직을 가지고 있는 서비스 클래스가 위치
├── dao/          # 데이터 접근 인터페이스 클래스가 위치
├── dto/          # 계층 간 데이터를 전달하기 위한 클래스가 위치 
└── entity/       # 데이터베이스 테이블 매핑 클래스가 위치
```

### 리소스 폴더 구성 규칙

`src/main/resources`는 다음 구조를 기준으로 구성합니다. 
아직 사용하지 않는 디렉터리는 미리 만들지 않고, 관련 기능을 추가할 때 생성합니다.

```text
src/main/resources/
├── application.properties      # Spring Boot 및 데이터 소스 설정
├── db/                         # MyBatis 및 DB 관련 리소스
├── templates/                  # Thymeleaf HTML 템플릿
│   ├── home.html               # 홈 화면 HTML
│   ├── {기능명}/                # 기능별 HTML
│   └── fragments/              # 공통 HTML 조각
└── static/                     # 브라우저에 제공되는 정적 파일
    ├── css/                    # CSS 파일
    ├── js/                     # JavaScript 파일
    ├── images/                 # 이미지 파일
    └── fonts/                  # 폰트 파일
```

### REST API 컨트롤러 작성 규칙

1. API 컨트롤러 클래스는 `{기본 패키지}.controller.api` 패키지에 작성하고 `@RestController`를 적용합니다.
2. API 컨트롤러 클래스 이름은 `{기능명}Controller`로 생성합니다.
3. DTO는 `{기본 패키지}.dto`에 생성합니다.
4. DTO 이름은 `{기능명}{용도}Request` 또는 `{기능명}{용도}Response` 형식으로 작성합니다.
5. 서비스의 비즈니스 메소드를 호출할 경우에는 `*Request` 객체를 Entity 객체로 변환해서 비즈니스 메소드의 매개값으로 전달합니다.
6. 서비스의 비즈니스 메소드의 반환값은 `*Response` 객체로 변환해서 엔드포인트에서 반환합니다.

### 서비스 클래스 작성 규칙
1. 서비스 클래스는 `{기본 패키지}.service` 패키지에 작성하고 `@Service`를 적용합니다.
2. 서비스 클래스 이름은 `{기능명}Service.java`로 생성합니다.
3. 서비스 클래스의 비즈니스 메소드의 매개변수 타입과 반환 타입은 `*Request`, `*Response` DTO를 사용해서는 안됩니다.
4. 서비스 클래스에 `@Transactional(readOnly = true)`, 등록·수정·삭제 메서드에 `@Transactional`을 적용합니다.


### 화면 요청 처리 규칙
1. 모든 화면 요청을 처리하는 엔드포인트는 `{기본 패키지}/controller/MvcController.java`에 정의합니다.
2. `MvcController`는 `@Controller` 어노테이션을 적용합니다.
3. 화면 요청 처리 시 비즈니스 서비스가 필요하면 `{기본 패키지}.service`의 기존 서비스를 사용합니다.
4. 홈 화면 요청 엔드포인트는 `/` 이며 Thymeleaf 템플릿 파일은  `src/main/resources/templates/home.html`로 생성합니다. 
5. 기능별 화면 요청 엔드포인트는 `/{기능명}/*`이며 Thymeleaf 템플릿 파일은  `src/main/resources/templates/{기능명}/*.html`로 생성합니다.


### CSS/JS 작성 규칙
1. CSS 프레임워크는 Bootstrap 5를 사용합니다. 
2. UI는 Bootstrap이 제공하는 컴포넌트와 유틸리티 클래스를 우선 활용합니다.
3. Bootstrap으로 표현하기 어려운 화면별 스타일만 별도의 CSS 파일에 추가합니다.
4. 공통 CSS는 `src/main/resources/static/css/*.css`로 생성합니다.
5. 공통 JS는 `src/main/resources/static/js/*.js`로 생성합니다.
6. 화면 전용 CSS/JS는 해당 HTML 안에 생성합니다.
7. 파일 이름은 역할을 드러내는 소문자 kebab-case를 사용합니다. 

### 스킬 사용 규칙

1. 작업을 수행할 때는 대응 스킬 파일의 존재를 확인한 뒤 읽고, 해당 스킬의 지침을 따릅니다. 
2. 스킬 파일이 없으면 누락 사실을 알리고 이 문서의 공통 규칙과 기존 구현을 기준으로 진행합니다. 
3. CRUD API 생성 또는 변경: [crud-api-create](.github/skills/crud-api-create/SKILL.md)
4. MyBatis DAO·Mapper XML·공통 설정 생성 또는 변경 및 연결 장애 수정: [mybatis](.github/skills/mybatis/SKILL.md)
5. OpenAPI·Swagger UI 구성, 정적 CSS·JS 설치 및 문서 장애 수정: [swagger-ui](.github/skills/swagger-ui/SKILL.md)
6. API 테스트 HTML 생성 또는 변경: [api-test-html](.github/skills/api-test-html/SKILL.md)

