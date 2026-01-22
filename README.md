# 👕 Clothing Shop RESTful API
> **Spring Boot 기반의 의류 쇼핑몰 백엔드 API 서버**
>
> **GitHub:** [https://github.com/pd8459/restful-api-board](https://github.com/pd8459/restful-api-board)

<br/>

## 📝 프로젝트 소개 (Overview)
**Spring Boot**를 활용하여 의류 쇼핑몰의 핵심 기능을 RESTful API로 구축한 **백엔드 개인 프로젝트**입니다.

단순한 기능 구현을 넘어, **확장성 있는 데이터베이스 설계(JPA)**와 **안전한 인증 시스템(JWT)**, 그리고 **실제 결제 로직(Toss Payments)**을 구현하며 이커머스 백엔드의 전반적인 흐름을 경험했습니다.

<br/>

## 🕰️ 프로젝트 정보
- **개발 기간:** 2025.02.05 ~ 2025.06.26 (약 5개월)
- **개발 인원:** 1명 (개인 프로젝트)
- **주요 역할:**
  - RESTful API 설계 및 구현
  - DB 모델링 및 JPA 연관관계 매핑
  - Spring Security + JWT 기반 보안/인증 로직 구현
  - 외부 결제 API (아임포트) 연동

<br/>

## 🛠 기술 스택 (Tech Stack)

### Backend Framework
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

### Language & Tools
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-BC0230?style=for-the-badge&logo=lombok&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)

### Database
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### Payment
![PortOne](https://img.shields.io/badge/PortOne-FC5230?style=for-the-badge)

### API Docs
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

<br/>

## 💾 ERD 설계
<img width="1964" height="1576" alt="Image" src="https://github.com/user-attachments/assets/bc14056d-55b6-4202-bd55-606ae3fed143" />

### 📐 데이터베이스 구조 및 설계 의도

**1. 회원 중심의 데이터 설계 (`User`)**
- 회원(`User`)은 자신의 **장바구니(`Cart`)**를 하나씩 가집니다 (1:1 관계).
- 주문(`Orders`)과 결제(`Payments`) 내역을 회원을 기준으로 조회할 수 있도록 연관관계를 설정했습니다.

**2. 다대다(N:M) 관계 해소**
- **장바구니:** `Cart`와 `Item` 사이의 다대다 관계를 **`Cart_Item`** 중간 테이블로 풀어내어, 상품별 수량(`quantity`)을 관리합니다.
- **주문:** `Orders`와 `Item` 사이를 **`Order_Item`**으로 연결하여, 주문 당시의 상품 가격(`price`)과 수량(`count`)을 기록하여 데이터 무결성을 보장했습니다.

**3. 결제 및 배송 정보의 분리**
- **`Payments` 테이블:** 결제 고유 번호(`imp_uid`), PG사 정보, 결제 상태 등을 별도로 관리하여 결제 이력을 투명하게 관리합니다.
- **주소 정보 세분화:** 도로명 주소, 지번 주소, 상세 주소 등을 분리 저장하여 배송 정확도를 높였습니다.

**4. 동시성 제어**
- `Item` 테이블에 `version` 필드를 두어, 재고 차감 시 발생할 수 있는 동시성 이슈를 **낙관적 락(Optimistic Lock)**으로 제어할 수 있는 구조를 마련했습니다.
<br/>

🚀 주요 기능 (Key Features)
1️⃣ 안전한 인증/인가 시스템 (Security & Auth)
JWT (Json Web Token) 기반의 Stateless 인증을 구현하여 서버 확장성을 고려한 로그인 시스템 구축

Spring Security Filter Chain을 커스텀하여 URL별 접근 권한(User/Admin)을 세밀하게 제어

BCryptPasswordEncoder를 적용한 비밀번호 단방향 암호화 저장

2️⃣ 상품 및 장바구니 (Cart & Performance)
**JPA Dirty Checking(변경 감지)**을 활용하여 장바구니 수량 변경 로직을 효율적으로 구현

조회 성능 최적화: JOIN FETCH를 적극 활용하여 연관된 엔티티 조회 시 발생하는 N+1 문제 해결

N:M 관계 해소: 장바구니(Cart)와 상품(Item) 사이를 중간 엔티티(CartItem)로 매핑하여 데이터 확장성 확보

3️⃣ 주문 및 결제 (Order & Payment)
PortOne (Toss Payments) API를 연동한 실시간 결제 및 결제 검증(Verification) 로직 구현

동시성 제어 (Concurrency Control):

상품 재고 차감 시 발생하는 Race Condition 방지를 위해 **JPA Optimistic Lock(@Version)**을 적용하여 데이터 정합성 보장

데이터 무결성: @Transactional을 통해 주문 생성-결제 승인-재고 차감의 과정을 원자성(Atomicity) 있게 처리
<br/>

## 🧪 API 명세 (API Specification)

**Base URL:** `http://localhost:8080/api`  
**API Docs:** Swagger UI를 통해 API 명세 확인 및 테스트가 가능합니다.

<img width="1450" height="913" alt="Image" src="https://github.com/user-attachments/assets/5c939f7d-4ecd-4f16-9a96-ccf672158f02" />

### 1️⃣ 인증 및 회원 (Auth & User)
| Method | URI | Description | Note |
| :---: | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | 로그인 (JWT 발급) | `{email, password}` |
| `POST` | `/api/users` | 회원 가입 | `{email, password, name...}` |
| `GET` | `/api/users/{email}` | 회원 정보 조회 | PathVariable |
| `PUT` | `/api/users/{id}` | 회원 정보 수정 | `{name, address...}` |
| `DELETE` | `/api/users/{id}` | 회원 탈퇴 | - |

### 2️⃣ 상품 (Item)
| Method | URI | Description | Note |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/items` | 상품 목록 조회 | - |
| `GET` | `/api/items/{id}` | 상품 상세 조회 | - |
| `POST` | `/api/items/add` | 상품 등록 | `{name, price, stock...}` |
| `PUT` | `/api/items/{id}` | 상품 수정 | - |
| `DELETE` | `/api/items/{id}` | 상품 삭제 | - |

### 3️⃣ 장바구니 (Cart)
| Method | URI | Description | Note |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/cart` | 장바구니 조회 | - |
| `POST` | `/api/cart/add` | 장바구니 상품 추가 | `{itemId, count}` |
| `POST` | `/api/cart/update` | 장바구니 수량 변경 | `{cartItemId, count}` |
| `DELETE` | `/api/cart/remove` | 장바구니 상품 삭제 | - |

### 4️⃣ 주문 (Order)
| Method | URI | Description | Note |
| :---: | :--- | :--- | :--- |
| `POST` | `/api/orders/create` | 주문 생성 | `{cartItems, address}` |
| `GET` | `/api/orders/get/{orderId}` | 주문 상세 조회 | - |
| `DELETE` | `/api/orders/{orderId}` | 주문 취소 | - |

### 5️⃣ 결제 (Payment)
| Method | URI | Description | Note |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/payment/validate/{impUid}` | 결제 검증 (PortOne) | `imp_uid` 검증 |
| `POST` | `/api/payment/getToken` | 결제 토큰 발급 | - |
| `GET` | `/api/payment/test-keys` | 결제 키 테스트 | 개발용 |

### 6️⃣ 기타 (System)
| Method | URI | Description | Note |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/hello` | 서버 헬스 체크 | 연결 확인용 |

<br>

### 📉 에러 코드 (Error Response)
| Status Code | Description | 상황 (Case) |
| :---: | :--- | :--- |
| `200` | **OK** | 요청 성공 |
| `400` | **Bad Request** | 파라미터 누락, 유효성 검사 실패 |
| `401` | **Unauthorized** | 인증 실패 (토큰 만료, 로그인 필요) |
| `403` | **Forbidden** | 접근 권한 없음 (일반 회원이 관리자 페이지 접근) |
| `404` | **Not Found** | 리소스 없음 (존재하지 않는 회원/상품 ID) |
| `500` | **Internal Server Error** | 서버 내부 오류 |

<br>
<br/>

## 🔥 트러블 슈팅 & 회고 (Retrospective)

### ⚠️ N+1 문제 해결
- **문제:** 장바구니 조회 시 연관된 상품 엔티티를 가져오기 위해 쿼리가 반복 발생하는 N+1 문제 발생
- **해결:** JPQL의 `fetch join`을 사용하여 연관된 데이터를 한 번의 쿼리로 조회하도록 최적화

### 💳 결제 데이터 무결성
- **고민:** 결제 도중 네트워크 오류 등으로 DB에는 주문이 안 들어갔는데 돈만 빠져나가는 상황 방지 필요
- **해결:** 결제 승인 요청 전후로 트랜잭션을 설정하고, 예외 발생 시 전체 로직이 롤백되도록 처리하여 데이터 무결성 확보

<br/>

## 🏃 How to Run (실행 방법)

# 1. 프로젝트 다운로드 (Clone)
git clone https://github.com/pd8459/restful-api-board.git

# 2. 프로젝트 폴더로 이동
cd restful-api-board

# 3. 빌드 (Build) - 윈도우(Windows)
./gradlew.bat clean build

# 3. 빌드 (Build) - 맥/리눅스(Mac/Linux)
./gradlew clean build

# 4. 서버 실행 (Run)
java -jar build/libs/*.jar
