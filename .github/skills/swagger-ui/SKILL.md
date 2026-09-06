---
name: swagger-ui
description: 'OpenAPI YAML과 Swagger UI를 구현한다.'
---

# Swagger UI 구현 절차

## OpenAPI 문서 작성 절차

1. OpenAPI 문서는 `src/main/resources/static/docs/openapi.yaml`에 작성한다.
2. API 구현의 경로, 요청·응답 DTO, 상태 코드와 예시를 YAML에 작성한다.
3. API 변경 시 컨트롤러와 YAML을 함께 갱신한다.
4. Java 코드에는 문서 전용 애노테이션을 추가하지 않는다.

## 정적 리소스 설치 절차

1. 고정된 Swagger UI 버전의 CSS와 JavaScript를 로컬에 설치한다.
2. CSS는 `src/main/resources/static/docs/css/`에 배치한다.
3. JavaScript는 `src/main/resources/static/docs/js/`에 배치한다.

## 화면 연결 절차

1. Swagger UI 화면은 `src/main/resources/templates/docs/index.html`에 작성한다.
2. OpenAPI 문서를 `/docs/openapi.yaml`에서 불러오도록 설정한다.
3. `MvcController`에서 `/docs/index.html` 요청을 Swagger UI 화면에 매핑한다.
4. CSS는 `/docs/css/**`, JavaScript는 `/docs/js/**` URL로 연결한다.

## 검증 절차

1. OpenAPI YAML의 경로와 API 구현이 일치하는지 확인한다.
2. `/docs/index.html`에서 Swagger UI와 API 목록이 표시되는지 확인한다.
3. CSS·JavaScript·YAML 요청의 `404`와 브라우저 JavaScript 오류가 없는지 확인한다.