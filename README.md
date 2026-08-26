[![CI](https://github.com/Raphasha27/smartbank-enterprise-platform/actions/workflows/ci.yml/badge.svg)](https://github.com/Raphasha27/smartbank-enterprise-platform/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# SmartBank Enterprise Platform

### Microservices-Based Core Banking System

<div align="center">

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=flat-square&logo=docker&logoColor=white)

</div>

---

## Overview

SmartBank Enterprise Platform is a **distributed core banking system** built on a microservices architecture with event-driven communication via Apache Kafka. It implements CQRS patterns, per-service database isolation, and containerised deployment — designed to demonstrate production-ready Java/Spring Boot engineering for financial systems.

> Built to showcase enterprise-grade patterns — not a tutorial, a real implementation.

---

## Architecture

```
                         ┌──────────────────────┐
                         │   API Gateway :8080   │
                         │  Spring Cloud Gateway │
                         └──────┬───────────────┘
                                │
          ┌─────────┬───────────┼───────────┬──────────┬──────────┬──────────┐
          │         │           │           │          │          │          │
     ┌────▼───┐ ┌───▼────┐ ┌───▼────┐ ┌───▼────┐ ┌───▼────┐ ┌───▼────┐ ┌───▼────┐
     │ Auth   │ │Account │ │Transact│ │  Loan  │ │ Ledger │ │ Notif. │ │ Audit  │
     │ :8081  │ │ :8082  │ │ :8083  │ │ :8084  │ │ :8087  │ │ :8086  │ │ :8085  │
     └───┬────┘ └───┬────┘ └───┬────┘ └───┬────┘ └───┬────┘ └───┬────┘ └───┬────┘
         │          │          │          │          │          │          │
         └──────────┴──────────┴────┬─────┴──────────┴──────────┴──────────┘
                                    │
                         ┌──────────▼──────────┐
                         │   Apache Kafka Bus   │
                         │  Event-Driven Comms   │
                         └──────────┬──────────┘
                                    │
                    ┌───────────────▼───────────────┐
                    │        PostgreSQL Cluster      │
                    │   (per-service databases)      │
                    └───────────────────────────────┘
```

---

## Microservices

| Service | Port | Description | Database |
|---------|------|-------------|----------|
| `api-gateway` | 8080 | Spring Cloud Gateway — routing, rate limiting, auth forwarding | — |
| `auth-service` | 8081 | JWT authentication, MFA, BCrypt password hashing, refresh token rotation | `smartbank_auth` |
| `account-service` | 8082 | Account CRUD, balance management, deposit/withdrawal processing | `smartbank_accounts` |
| `transaction-service` | 8083 | Fund transfers, idempotent processing, transaction history | `smartbank_transactions` |
| `loan-service` | 8084 | Credit scoring, amortisation schedules, loan approval workflows | `smartbank_loans` |
| `audit-service` | 8085 | Immutable audit trail logging for all financial operations | `smartbank_audit` |
| `notification-service` | 8086 | Email/SMS/push notifications consumed from Kafka events | `smartbank_notifications` |
| `ledger-service` | 8087 | Double-entry bookkeeping, general ledger, balance reconciliation | `smartbank_ledger` |

---

## Quick Start

### Prerequisites

- Java 21+
- Docker & Docker Compose
- Apache Kafka (included in compose)

### Run All Services

```bash
git clone https://github.com/Raphasha27/smartbank-enterprise-platform.git
cd smartbank-enterprise-platform
docker compose up --build
```

### Run Individual Service

```bash
cd auth-service
./mvnw spring-boot:run
```

---

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| API Gateway | Spring Cloud Gateway |
| Messaging | Apache Kafka |
| Database | PostgreSQL 15 |
| Security | Spring Security, JWT, BCrypt |
| Build | Maven |
| Container | Docker & Docker Compose |

---

## Project Structure

```
smartbank-enterprise-platform/
├── api-gateway/            # Spring Cloud Gateway — routing & rate limiting
├── auth-service/           # JWT + MFA authentication service
├── account-service/        # Account management & balance operations
├── transaction-service/    # Fund transfers & transaction history
├── loan-service/           # Credit scoring & loan workflows
├── audit-service/          # Immutable audit trail logging
├── notification-service/   # Email/SMS/push notification consumer
├── ledger-service/         # Double-entry bookkeeping engine
├── docker/                 # Shared Docker configs
├── docs/                   # Architecture documentation
├── scripts/                # Utility scripts
├── docker-compose.yml      # Full stack orchestration
├── pom.xml                 # Parent Maven POM
├── CONTRIBUTING.md
├── SECURITY.md
└── LICENSE
```

---

## Key Features

- **Event-driven architecture** — all inter-service communication via Kafka topics
- **JWT + MFA** — multi-factor authentication with refresh token rotation
- **Per-service databases** — complete data isolation, no shared schemas
- **Idempotent transfers** — sub-second fund processing with deduplication
- **Audit trails** — immutable logging for every financial operation
- **Containerised** — full Docker Compose orchestration, production-ready

---

## API Documentation

Each service exposes Swagger UI at `http://localhost:{port}/swagger-ui.html`

| Service | Swagger URL |
|---------|-------------|
| Gateway | http://localhost:8080/swagger-ui.html |
| Auth | http://localhost:8081/swagger-ui.html |
| Accounts | http://localhost:8082/swagger-ui.html |
| Transactions | http://localhost:8083/swagger-ui.html |
| Loans | http://localhost:8084/swagger-ui.html |

---

## Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) and open an issue before submitting a PR.

---

## License

MIT License — see [LICENSE](LICENSE) for details.

---

<div align="center">
Built by <a href="https://github.com/Raphasha27">Koketso Raphasha</a> · <a href="https://portfolio-iota-eight-90.vercel.app/">Portfolio</a>
</div>
