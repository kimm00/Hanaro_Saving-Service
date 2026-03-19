# 💰 Hanaro Saving Service (예적금몰 서비스)

Spring Boot 기반으로 구현한 예금/적금 관리 백엔드 서비스입니다.  
사용자는 상품에 가입하고 관리할 수 있으며, 관리자는 상품 및 회원을 관리할 수 있습니다.

---

## 📖 프로젝트 소개

본 프로젝트는 하나은행 금융 서비스 개발 과정에서 진행한 실습 과제로,  
예금 및 적금 상품을 관리하고 가입/해지/만기 처리를 수행하는 시스템입니다. :contentReference[oaicite:0]{index=0}

### 🎯 주요 목적
- 금융 상품 관리 시스템 구조 이해
- Spring Boot 기반 REST API 설계
- 인증/인가(JWT + Role 기반) 구현
- 실제 서비스 수준의 Validation / Logging / Exception 처리 경험

---

## 🛠️ 기술 스택

- **Backend**: Spring Boot 4+, Java 21
- **DB**: MySQL8 (Docker)
- **ORM**: JPA (Hibernate)
- **Security**: Spring Security + JWT
- **API Docs**: Swagger (OpenAPI 3)
- **Build Tool**: Gradle
- **Test**: JUnit, Mockito, Jacoco
- **Logging**: Logback
- **Monitoring**: Spring Actuator

---

## 🔐 인증 / 인가

- JWT 기반 인증
- Role 기반 권한 제어

| 역할 | 설명 |
|------|------|
| USER | 일반 사용자 |
| ADMIN | 관리자 |

---

## 🚀 주요 기능

### 👤 사용자 기능
- 회원가입 / 로그인 (가입 시 기본 계좌 자동 생성)
- 상품 목록 조회 / 상세 조회
- 상품 가입
- 내 계좌 및 가입 내역 조회
- 중도 해지 및 이자 확인

### 🛠 관리자 기능
- 상품 등록 / 수정 / 삭제 / 조회 (이미지 포함)
- 회원 목록 조회
- 회원별 가입 내역 조회
- 상품 만기 처리

---

## 🧩 기능

### 1️⃣ Validation
- 회원가입 및 상품 등록 시 유효성 검사
- 계좌번호 형식 검증 (11자리 숫자)

### 2️⃣ Exception Handling
- 공통 에러 응답 구조 설계
- @ControllerAdvice 기반 예외 처리

### 3️⃣ Logging
- 사용자 로그: `logs/user.log`
- 상품 관리 로그: `logs/product.log`
- 서비스 로그: `logs/service.log`

### 4️⃣ 파일 업로드
- 날짜 기반 디렉토리 저장
/resources/static/upload/yyyyMMdd

- 파일명 중복 방지 및 용량 제한

### 5️⃣ Monitoring
- Actuator를 통한 상태 확인
/actuator/health
/actuator/metrics

---
## 🗂️ ERD (Entity Relationship Diagram)

<img width="951" height="691" alt="ERD" src="https://github.com/user-attachments/assets/bccf8c68-114a-42a1-8672-a6927fbccc60" />

본 프로젝트는 회원(Member), 계좌(Account), 상품(Product), 가입(Subscription)을 중심으로 설계되었습니다.

### 📌 주요 관계

- **Member ↔ Account (1:N)**  
  한 회원은 여러 개의 계좌를 가질 수 있습니다.

- **Member ↔ Subscription (1:N)**  
  한 회원은 여러 금융 상품에 가입할 수 있습니다.

- **Account ↔ Subscription (1:1)**  
  하나의 계좌는 하나의 상품 가입과 연결됩니다.

- **Product ↔ Subscription (1:N)**  
  하나의 상품은 여러 사용자가 가입할 수 있습니다.

- **Product ↔ ProductImage (1:N)**  
  하나의 상품은 여러 이미지를 가질 수 있으며, 썸네일 이미지가 존재합니다.

### 📌 특징

- 모든 핵심 엔티티는 `createdAt`, `updatedAt`을 통해 변경 이력을 관리합니다.
- ENUM 타입을 활용하여 상품 종류, 납입 주기, 상태 등을 명확하게 구분합니다.
- Subscription을 중심으로 회원, 계좌, 상품 간 관계를 연결하여 금융 서비스 흐름을 반영했습니다.

---

## 📌 API 명세

### 🔑 인증
- `POST /api/auth/signin`

---

### 📦 Product API

| Method | URL | 설명 |
|--------|-----|------|
| GET | /api/products | 상품 목록 조회 |
| GET | /api/products/{id} | 상품 상세 조회 |
| POST | /api/products | 상품 등록 (ADMIN) |
| PUT | /api/products/{id} | 상품 수정 (ADMIN) |
| DELETE | /api/products/{id} | 상품 삭제 (ADMIN) |
| POST | /api/products/{productId}/images | 상품 이미지 업로드 |
| DELETE | /api/products/{productId}/images/{imageId} | 이미지 삭제 |

---

### 💳 Subscription API

| Method | URL | 설명 |
|--------|-----|------|
| POST | /api/subscriptions | 상품 가입 |
| POST | /api/subscriptions/{subscriptionId}/cancel | 상품 해지 |
| POST | /api/admin/subscriptions/{subscriptionId}/complete | 만기 처리 |
| GET | /api/subscriptions/my | 내 가입 내역 |
| GET | /api/admin/subscriptions | 전체 가입 내역 (ADMIN) |
| GET | /api/admin/members/{memberId}/subscriptions | 회원별 조회 |

---

### 👤 Member API

| Method | URL | 설명 |
|--------|-----|------|
| POST | /api/members/register | 회원가입 |
| GET | /api/members | 회원 목록 (ADMIN) |

---

### 🏦 Account API

| Method | URL | 설명 |
|--------|-----|------|
| POST | /api/accounts/{accountId}/payment | 납입 |
| POST | /api/accounts/default-account | 기본 계좌 생성 |
| PATCH | /api/accounts/{accountId}/cancel | 계좌 해지 |
| PATCH | /api/accounts/admin/{accountId}/complete | 만기 처리 |
| GET | /api/accounts/{accountId} | 계좌 조회 |
| GET | /api/accounts/members/{memberId} | 회원 계좌 조회 |

---

## 📊 테스트

- Repository Test Coverage ≥ 60%
- Controller Test Coverage ≥ 60%
- Jacoco Report 사용
<img width="1067" height="383" alt="스크린샷 2026-03-19 오후 9 51 25" src="https://github.com/user-attachments/assets/813353d0-a90a-43a2-830d-6ac8878ad675" />

---

## 📄 Swagger
http://localhost:8080/swagger-ui/index.html


---

## 📁 프로젝트 구조
controller
service
repository
entity
dto
mapper
security
config


---

## ✨ 프로젝트 특징

- 실제 금융 서비스 흐름 기반 설계
- 관리자 / 사용자 기능 완전 분리
- 확장 가능한 계층형 구조 (Controller-Service-Repository)
- 테스트 코드 기반 안정성 확보

---

## 👩‍💻 개발자

- 김도이 (Doyi Kim)


