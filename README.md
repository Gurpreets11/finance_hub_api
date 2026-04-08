# My Finances — Spring Boot REST API

## Tech Stack
- Java 17 · Spring Boot 3.2
- Spring Security + JWT (JJWT 0.12)
- JPA / Hibernate · H2 (dev) · MySQL (prod)
- Lombok · Bean Validation

---

## Quick Start

```bash
# Clone / unzip the project
cd finance-api

# Run with H2 in-memory (no DB setup needed)
./mvnw spring-boot:run

# App starts at http://localhost:8080
# H2 console: http://localhost:8080/h2-console
```

### Demo credentials (auto-seeded)
| Field    | Value              |
|----------|--------------------|
| Email    | demo@finance.com   |
| Password | password123        |

---

## API Reference

All protected endpoints require:
```
Authorization: Bearer <access_token>
```

### Auth

| Method | Endpoint              | Auth | Description               |
|--------|-----------------------|------|---------------------------|
| POST   | /api/v1/auth/signup   | ❌   | Register new user         |
| POST   | /api/v1/auth/login    | ❌   | Login, get tokens         |
| POST   | /api/v1/auth/refresh  | ❌   | Refresh access token      |

**Signup body:**
```json
{
  "fullName": "Raj Kumar",
  "email": "raj@example.com",
  "password": "password123"
}
```

**Login response:**
```json
{
  "success": true,
  "data": {
    "accessToken":  "<jwt>",
    "refreshToken": "<jwt>",
    "tokenType":    "Bearer",
    "expiresIn":    900,
    "user": { "id": 1, "fullName": "Raj Kumar", "email": "...", "role": "USER" }
  }
}
```

---

### Dashboard

| Method | Endpoint                   | Description                          |
|--------|----------------------------|--------------------------------------|
| GET    | /api/v1/dashboard/summary  | Full home screen data in one call    |

**Response includes:**
- `netWorth`, `totalInvestments`, `totalLoans`
- `income`, `expenses` — this month amount + % change vs last month
- `recentIncomes` — last 5 income transactions
- `recentExpenses` — last 5 expense transactions
- `topInvestments` — top 3 by current value
- `upcomingPayments` — loan EMIs due in next 30 days

---

### Income

| Method | Endpoint              | Description           |
|--------|-----------------------|-----------------------|
| GET    | /api/v1/incomes       | List (paginated)      |
| GET    | /api/v1/incomes/{id}  | Get by ID             |
| POST   | /api/v1/incomes       | Add income            |
| PUT    | /api/v1/incomes/{id}  | Update income         |
| DELETE | /api/v1/incomes/{id}  | Delete income         |

**categoryEnum values:** `SALARY, RENTAL, DIVIDEND, FREELANCE, BUSINESS, OTHER`

---

### Expense

| Method | Endpoint               | Description           |
|--------|------------------------|-----------------------|
| GET    | /api/v1/expenses       | List (paginated)      |
| GET    | /api/v1/expenses/{id}  | Get by ID             |
| POST   | /api/v1/expenses       | Add expense           |
| PUT    | /api/v1/expenses/{id}  | Update expense        |
| DELETE | /api/v1/expenses/{id}  | Delete expense        |

**categoryEnum values:** `RENT, FOOD_DINING, TRANSPORT, UTILITIES, ENTERTAINMENT, HEALTH, EDUCATION, SHOPPING, OTHER`

---

### Investment

| Method | Endpoint                  | Description           |
|--------|---------------------------|-----------------------|
| GET    | /api/v1/investments       | List (paginated)      |
| GET    | /api/v1/investments/{id}  | Get by ID             |
| POST   | /api/v1/investments       | Add investment        |
| PUT    | /api/v1/investments/{id}  | Update investment     |
| DELETE | /api/v1/investments/{id}  | Delete investment     |

**type values:** `MUTUAL_FUND, STOCK, FIXED_DEPOSIT, GOLD, REAL_ESTATE, CRYPTO, OTHER`

---

### Loan

| Method | Endpoint            | Description           |
|--------|---------------------|-----------------------|
| GET    | /api/v1/loans       | List (paginated)      |
| GET    | /api/v1/loans/{id}  | Get by ID             |
| POST   | /api/v1/loans       | Add loan              |
| PUT    | /api/v1/loans/{id}  | Update loan           |
| DELETE | /api/v1/loans/{id}  | Delete loan           |

**type values:** `HOME, PERSONAL, CAR, EDUCATION, GOLD, BUSINESS, OTHER`

---

## Pagination

All list endpoints support:
```
GET /api/v1/incomes?page=0&size=20
```

---

## Switch to MySQL (Production)

In `application.properties`, comment out the H2 block and uncomment MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/finance_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

Create the DB first:
```sql
CREATE DATABASE finance_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## JWT Configuration

In `application.properties`:
```properties
# Generate a secure 256-bit base64 key for production:
# openssl rand -base64 32
app.jwt.secret=<your-base64-encoded-secret>
app.jwt.access-token-expiration=900000    # 15 minutes
app.jwt.refresh-token-expiration=604800000 # 7 days
```

---

## Project Structure

```
src/main/java/com/finance/api/
├── config/
│   ├── SecurityConfig.java       # Spring Security + JWT filter chain
│   └── DataSeeder.java           # Dev demo data seeder
├── controller/
│   ├── AuthController.java
│   ├── DashboardController.java
│   ├── IncomeController.java
│   ├── ExpenseController.java
│   ├── InvestmentController.java
│   └── LoanController.java
├── dto/
│   ├── request/                  # Validated inbound DTOs
│   └── response/                 # Outbound response shapes
├── entity/                       # JPA entities (User, Income, Expense, Investment, Loan)
├── exception/                    # GlobalExceptionHandler + custom exceptions
├── repository/                   # Spring Data JPA repositories
├── security/
│   ├── JwtService.java           # Token generation + validation
│   └── JwtAuthFilter.java        # Per-request JWT extraction
└── service/impl/                 # Business logic services
```

---

## Coming Next (Detail & Listing APIs)
- `GET /api/v1/incomes?category=SALARY&from=2026-01-01&to=2026-03-31` — filtered listing
- `GET /api/v1/dashboard/net-worth-history` — chart data
- `GET /api/v1/reports/monthly` — monthly breakdown by category
- `GET /api/v1/upcoming-payments` — standalone upcoming payments listing
