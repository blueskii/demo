---
name: mybatis
description: 'MyBatis DAO, Mapper XML, 공통 설정, Entity 매핑과 H2 통합 테스트를 구현한다.'
---

# MyBatis 구현 절차

## 의존성과 설정 작성 절차

1. 빌드 파일의 Spring Boot·Java 버전을 확인하고 MyBatis와 H2 의존성을 추가한다.
2. MyBatis 공통 설정은 `src/main/resources/db/mybatis-config.xml`에 작성한다.
3. Mapper XML은 `src/main/resources/db/mapper`에 생성한다.
4. `application.properties`에 데이터 소스, MyBatis 설정과 Mapper XML 경로를 지정한다.

## DAO 작성 절차

1. DAO는 `{기본 패키지}.dao`에 `{기능명}Dao`로 생성하고 `@Mapper`를 적용한다.
2. CRUD 메서드의 매개변수와 조회 반환값은 Entity를 사용한다.
3. 등록·수정·삭제 메서드는 영향받은 행 수를 반환하고, 서비스가 수정·삭제 결과 `0`을 대상 부재로 처리하게 한다.
4. 두 개 이상의 매개변수는 `@Param`으로 이름을 명시한다.

## Mapper XML 작성 절차

1. 파일 이름은 DAO 이름과 맞추고 `namespace`는 DAO의 전체 클래스명으로 작성한다.
2. SQL의 `id`는 DAO 메서드명과 일치시킨다.
3. 등록·목록·단건 조회·수정·삭제 SQL을 작성한다.
4. 입력값은 `#{...}`로 바인딩하고 `${...}`를 사용하지 않는다.
5. 생성 키는 `useGeneratedKeys`와 `keyProperty` 또는 `selectKey`로 Entity에 저장한다.

## Entity 매핑 절차

1. 테이블과 컬럼은 `snake_case`, Entity 필드는 `camelCase`로 작성한다.
2. `mapUnderscoreToCamelCase`를 적용하고 복잡한 조회는 `resultMap`으로 명시한다.
3. SQL 매개변수와 조회 결과 타입을 Entity 필드 타입에 맞춘다.

## 통합 테스트 절차

1. H2와 `schema.sql`을 사용해 MyBatis 통합 테스트 환경을 구성한다.
2. 등록 후 생성 키와 저장값을 확인하고 목록·단건 조회를 검증한다.
3. 수정 후 변경값을 재조회하고 삭제 후 대상이 없음을 확인한다.
4. 고유값 충돌과 대상이 없는 수정·삭제를 검증한다.
5. 관련 통합 테스트를 실행한 뒤 전체 테스트를 실행한다.
