---
name: crud-api-create
description: 'Spring Boot REST Controller, DTO 검증, Service와 H2 스키마를 포함한 CRUD API를 구현한다.'
---

# CRUD API 생성 절차

## 스키마와 엔티티 작성 절차

1. 애플리케이션과 통합 테스트의 데이터베이스는 H2를 사용한다.
2. 테이블 스키마는 `src/main/resources/db/schema.sql`에 작성한다.
3. 테이블의 컬럼 제약은 `NOT NULL`, `UNIQUE`을 적용한다.  
4. Entity 클래스 이름은 `User`, `Order`처럼 단수형 도메인 명사로 작성한다.
5. Entity 클래스는 `{기본 패키지}.entity`에 생성하고 `@Data`를 적용한다. 

## 컨트롤러 작성 절차

1. 컨트롤러의 이름은 `{기능명}Controller`로 작성한다.
2. 컨트롤러는 `{기본 패키지}.controller.api`에 생성한다.
3. 컨트롤러는 `@RestController`를 적용한다.
4. 컨트롤러는 CRUD 엔드포인트를 구현한다.
  - 등록: `POST /api/{단수형}`         | `201`, 생성 응답 DTO, `Location`
  - 목록: `GET /api/{단수형}`          | `200`, 응답 DTO 배열, 없으면 빈 배열
  - 읽기: `GET /api/{단수형}/{id}`     | `200`, 응답 DTO
  - 수정: `PUT /api/{단수형}/{id}`     | `200`, 수정 응답 DTO
  - 삭제: `DELETE /api/{단수형}/{id}`  | `204`, 본문 없음
5. 공통 `@RestControllerAdvice`와 오류 DTO를 사용해 잘못된 입력은 `400`, 대상 부재는 `404`, 고유값 충돌은 `409`로 반환한다.
6. 내부 예외 메시지와 스택 트레이스를 응답에 노출하지 않는다.
7. DTO 이름은 용도와 방향을 드러내도록 `{기능명}{용도}Request` 또는 `{기능명}{용도}Response` 형식을 사용한다.
8. DTO는 `{기본 패키지}.dto`에 생성하고 `@Data`를 적용한다. 
9. Validation 스타터를 추가하고 DTO에 필수값·길이·형식·범위 제약을 선언한다.
10. 컨트롤러는 `@Valid @RequestBody`로 DTO를 검증하고, `@PathVariable("id")`처럼 경로 변수 이름을 명시한다.
11. 컨트롤러는 서비스만 호출하고, 요청 DTO를 Entity로 변환해 서비스에 전달한다. 서비스의 반환값은 응답 DTO로 변환해 반환한다.

## 서비스 작성 절차

1. 서비스는 `{기본 패키지}.service`에 `{기능명}Service`로 생성하고 `@Service`를 적용한다.
2. DAO를 생성자로 주입하고 CRUD 비즈니스 메서드의 매개변수와 반환값은 Entity를 사용한다.
3. 클래스에 `@Transactional(readOnly = true)`, 등록·수정·삭제 메서드에 `@Transactional`을 적용한다.
4. 등록·수정 시 고유값 중복을 확인하고 데이터베이스의 `UNIQUE` 제약을 최종 방어로 유지한다.
5. 단건 조회 결과가 없거나 수정·삭제의 영향 행 수가 `0`이면 대상 부재 예외를 발생시킨다.

## DAO 작성 절차

1. DAO와 Mapper를 생성하거나 변경할 때는 `mybatis` 스킬을 사용한다.