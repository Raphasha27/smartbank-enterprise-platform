# SmartBank Enterprise Platform

Enterprise-grade, event-driven banking backend system built with Java Spring Boot microservices — designed for fintech and banking environments.

**Built by Kirov Dynamics Technology** — Cybersecurity + AI Engineering Systems

---

## Architecture

```mermaid
graph TB
    Client["Client (REST)"]
    GW["API Gateway :8080"]
    subgraph Services
        AUTH["Auth Service :8081<br/>JWT / BCrypt / RBAC"]
        ACCT["Account Service :8082<br/>Accounts / Balances"]
        TXN["Transaction Service :8083<br/>Transfers / History"]
        LOAN["Loan Service :8084<br/>Applications / Approvals"]
        AUDIT["Audit Service :8085<br/>Immutable Audit Trail"]
        NOTIF["Notification Service :8086<br/>Alerts / Events"]
    end
    subgraph Infrastructure
        PG[(PostgreSQL)]
        KAFKA["Event Bus<br/>Kafka / In-Memory"]
    end
    Client --> GW
    GW --> AUTH & ACCT & TXN & LOAN & AUDIT & NOTIF
    AUTH --> PG
    ACCT --> PG
    TXN --> PG & KAFKA
    LOAN --> PG
    AUDIT --> PG
    NOTIF --> PG
    KAFKA --> AUDIT & NOTIF
```

## CV-Ready Project Description

> Designed and implemented a fully containerized, event-driven banking backend system using **Java 21** and **Spring Boot 3.4** across 7 microservices. The platform features **JWT-authenticated** REST APIs, **BCrypt**-hashed user credentials, real-time money transfer processing with balance consistency, loan origination workflows, immutable audit logging for regulatory compliance, and event-driven notification delivery. Deployed via **Docker Compose** with per-service **PostgreSQL** databases and an in-memory event bus for asynchronous fraud evaluation and auditing.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.4, Spring Security, Spring Data JPA |
| Auth | JWT (jjwt), BCrypt |
| API Routing | Spring Cloud Gateway |
| Database | PostgreSQL (per-service) |
| Event Bus | Apache Kafka / In-memory publisher |
| Build | Maven (multi-module) |
| Deployment | Docker Compose |
| Monitoring | Spring Boot Actuator |

## Microservices

| Service | Port | Responsibility |
|---------|------|---------------|
| **Auth Service** | 8081 | User registration, login, JWT generation, BCrypt password hashing, JWT filter enforcement |
| **Account Service** | 8082 | Account CRUD, balance tracking, deposit/withdrawal balance updates |
| **Transaction Service** | 8083 | Money transfers with balance validation, transaction history, event publishing |
| **Loan Service** | 8084 | Loan applications, interest rate modeling, approval workflow |
| **Audit Service** | 8085 | Immutable audit log creation, user action history, queryable trail |
| **Notification Service** | 8086 | Event-triggered alerts, per-user notification inbox, read/unread tracking |
| **API Gateway** | 8080 | Request routing, path-based service distribution |

## Quick Start

```bash
# Build all services
mvn clean package -DskipTests

# Start PostgreSQL + Kafka + all services
docker compose up -d

# Verify: register a user
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@bank.com","password":"SecurePass1!"}'

# Login and get token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@bank.com","password":"SecurePass1!"}'
```

## API Overview

| Service | Endpoints |
|---------|-----------|
| Auth | `POST /auth/register`, `POST /auth/login` |
| Accounts | `POST /accounts`, `GET /accounts/{id}`, `GET /accounts/user/{userId}`, `PUT /accounts/{id}/balance` |
| Transactions | `POST /transactions/transfer`, `GET /transactions/account/{id}` |
| Loans | `POST /loans`, `GET /loans/{id}`, `POST /loans/{id}/approve`, `GET /loans/user/{userId}` |
| Audit | `POST /audit/logs`, `GET /audit/logs`, `GET /audit/logs/user/{email}` |
| Notifications | `POST /notifications`, `GET /notifications/user/{email}`, `GET /notifications/user/{email}/unread` |
| Actuator | `GET /actuator/health`, `GET /actuator/metrics` |

## Event Flow

```
Transaction Service
  → publishes TransactionEvent
  → consumed by Fraud rules (if amount > 5000 → HIGH_RISK)
  → consumed by Audit Service (persistent log)
  → consumed by Notification Service (user alert)
```

## Security

- Passwords hashed with **BCrypt** (configurable rounds)
- JWT tokens with 1-hour expiry, signed with HMAC-SHA256
- JwtFilter validates every authenticated request
- Public endpoints: `/auth/**`, `/actuator/health`
- All other routes require `Authorization: Bearer <token>`

## Repository

GitHub: [github.com/Raphasha27/smartbank-enterprise-platform](https://github.com/Raphasha27/smartbank-enterprise-platform)

---

*Designed as a zero-cost, fully-local fintech architecture simulation. No cloud billing required.*
