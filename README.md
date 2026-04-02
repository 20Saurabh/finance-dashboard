# Finance Dashboard Backend
Spring Boot REST API for financial data management with role-based access control.

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Overview
Production-ready backend for Finance Dashboard with:
- JWT Authentication & Role-Based Access (VIEWER/ANALYST/ADMIN)
- Financial Records CRUD + Filtering/Search
- Dashboard Analytics APIs
- H2 Database (MySQL ready)
- Comprehensive Error Handling

## 🛠️ Tech Stack
```
• Java 17+ | Spring Boot 3.2.0 | Spring Security (JWT)
• Spring Data JPA | Hibernate 6.3 | H2 Database
• Lombok | BCrypt | Maven 3.9+
• DTOs | Validation | Global Exception Handling
```

## ✨ Features Implemented
```
✅ User Registration/Login (JWT)
✅ Role Management (VIEWER/ANALYST/ADMIN)
✅ Financial Records CRUD (income/expense)
✅ Dashboard Summary APIs (totals/categories/trends)
✅ Role-based Access Control
✅ Record Filtering (date/category/type)
✅ Soft Delete
✅ Data Seeding (3 users + 10 records)
✅ Input Validation + Error Handling
✅ Audit Fields (created/updated)
```

## 📁 Project Structure
```
finance-dashboard/
├── src/main/java/com/finance/dashboard/
│   ├── controller/     # REST APIs
│   ├── service/        # Business Logic
│   ├── repository/     # Data Access
│   ├── entity/         # JPA Models
│   ├── dto/           # Request/Response
│   ├── security/       # JWT + Config
│   ├── exception/      # Error Handling
│   └── config/         # App Config
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+

### 1. Clone & Run
```bash
cd finance-dashboard
mvn clean spring-boot:run
```

### 2. Access
```
App: http://localhost:8082
H2 Console: http://localhost:8082/h2-console
Swagger Docs: http://localhost:8082/swagger-ui.html (optional)
```

### 3. Seeded Credentials
```
ADMIN: admin@finance.com / admin123
ANALYST: analyst@finance.com / analyst123  
VIEWER: viewer@finance.com / viewer123
```

## 🔐 Role-Based Access

| Role | Permissions |
|------|-------------|
| **VIEWER** | Dashboard summaries only |
| **ANALYST** | View records + full dashboard |
| **ADMIN** | Full CRUD + User management |

## 📖 API Endpoints

### Authentication
```
POST /api/auth/register
POST /api/auth/login
```
**Response**: `{ "token": "jwt...", "user": {...} }`

### Dashboard (ANALYST+)
```
GET /api/dashboard           # Full dashboard
GET /api/dashboard/income    # Total income
GET /api/dashboard/expenses  # Total expenses
GET /api/dashboard/balance   # Net balance
GET /api/dashboard/categories # Category totals
GET /api/dashboard/recent    # Last 5 transactions
GET /api/dashboard/monthly   # Monthly trends
```

### Records (ADMIN+ create/update/delete)
```
POST /api/records
GET /api/records             # List all
GET /api/records/{id}
PUT /api/records/{id}
DELETE /api/records/{id}
GET /api/records?startDate=...&category=...  # Filter
```

### Example Requests

**1. Login (cURL):**
```bash
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@finance.com","password":"admin123"}'
```

**2. Dashboard (with JWT):**
```bash
curl -X GET http://localhost:8082/api/dashboard \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**3. Create Record:**
```bash
curl -X POST http://localhost:8082/api/records \
  -H "Authorization: Bearer ADMIN_JWT" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 1000.00,
    "type": "INCOME", 
    "category": "Salary",
    "date": "2024-04-01",
    "notes": "Monthly salary"
  }'
```

## 🗄️ Database (H2 Console)
```
URL: jdbc:h2:mem:financedb
User: sa
Password: (empty)
Tables: users, financial_records
```

## 📝 Assumptions Made
1. H2 for demo (MySQL config commented)
2. Roles hardcoded in enum (DB-driven possible)
3. Soft delete over hard delete
4. JWT stateless (no refresh tokens)
5. Monthly summary last 12 months (configurable)

## 🔮 Future Improvements
```
• Pagination for record lists
• File upload for receipts
• Real-time dashboard (WebSocket)
• Multi-currency support
• Advanced analytics (ML predictions)
• MySQL production migration
• Rate limiting + monitoring
• API documentation (OpenAPI)
• Unit/Integration tests
```

## 📞 Troubleshooting
```
mvn clean spring-boot:run  # Fresh start
Check logs for "Started FinanceDashboardApplication"
Port conflicts? Change server.port=8083
JWT expired? Re-login
```

## 📄 License
MIT License
