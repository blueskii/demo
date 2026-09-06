---
name: api-test-html
description: 'Thymeleaf, Bootstrap 5와 fetch를 사용한 CRUD API 테스트 HTML과 MVC 엔드포인트를 구현한다.'
---

## API 테스트 HTML 구현 절차

1. 등록·목록·단건 조회·수정·삭제 API의 경로, 요청 DTO와 응답 DTO를 확인한다.
2. Thymeleaf 템플릿은 `src/main/resources/templates/{기능명}/api-test.html`에 작성한다.
3. Bootstrap 5를 사용하고 화면 전용 CSS와 JavaScript는 HTML 내부에 작성한다.
4. `MvcController`에 `/{기능명}/api-test` 화면 엔드포인트를 정의한다.
5. 각 API 요청은 `fetch`를 사용하고 HTTP 상태 코드와 응답 본문을 화면에 표시한다.
6. 등록·목록·단건 조회·수정·삭제의 성공 응답 `200`, `201`, `204`를 검증한다.
7. 잘못된 입력 `400`, 대상 부재 `404`, 고유값 충돌 `409` 응답을 검증한다.
8. 브라우저에서 화면 표시, API 요청과 응답, JavaScript 오류 유무를 확인한다.

