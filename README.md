# SmartBank Enterprise Platform

## Overview
SmartBank is a Java Spring Boot-based backend system that simulates real-world banking infrastructure, including secure transactions, account management, fraud detection, and audit logging.

It is designed using a microservices architecture to demonstrate scalability, security, and distributed system design principles.

---

## Core Features

- Secure user authentication (JWT + BCrypt)
- Account balance management
- Money transfer system (ACID transactions)
- Fraud detection service (rule-based monitoring)
- Audit logging for compliance tracking
- Microservices-based architecture
- Dockerized local deployment

---

## Architecture

```mermaid
graph TD
    Client[🌐 Client] -->|HTTPS| GW[API Gateway :8080]
    GW -->|validate JWT| AUTH[Auth Service :8081]
    GW -->|route| TX[Transaction Service :8083]
    GW -->|route| ACC[Account Service :8082]
    GW -->|route| LOAN[Loan Service :8084]

    TX -->|debit/credit request| ACC
    TX -->|publish events| KAFKA[(Kafka Event Bus)]

    KAFKA --> FRAUD[Fraud Detection]
    KAFKA --> AUDIT[Audit Service :8085]
    KAFKA --> NOTIF[Notification Service :8086]
    KAFKA --> LEDGER[Ledger Service :8087]
    KAFKA --> RECON[Reconciler<br/>Reversal Consumer]

    ACC --> DB1[(Account DB)]
    AUTH --> DB2[(Auth DB)]
    TX --> DB3[(Transaction DB)]

    style Client fill:#e3f2fd,stroke:#1565c0
    style GW fill:#c8e6c9,stroke:#2e7d32
    style KAFKA fill:#fff3e0,stroke:#e65100
    style AUTH fill:#f3e5f5,stroke:#6a1b9a
    style TX fill:#e8f5e9,stroke:#1b5e20
    style ACC fill:#e8f5e9,stroke:#1b5e20
    style FRAUD fill:#ffebee,stroke:#b71c1c
    style AUDIT fill:#fce4ec,stroke:#880e4f
    style NOTIF fill:#e0f2f1,stroke:#004d40
    style LEDGER fill:#e8f5e9,stroke:#1b5e20
    style RECON fill:#fff8e1,stroke:#f57f17
```

### Service Mesh

| Service | Port | Responsibility |
|---------|------|---------------|
| API Gateway | 8080 | JWT validation, request routing, rate limiting |
| Auth Service | 8081 | User registration, login, token issuance |
| Account Service | 8082 | Balance management, optimistic locking |
| Transaction Service | 8083 | Transfer orchestration, saga coordinator |
| Loan Service | 8084 | Loan processing, amortization |
| Audit Service | 8085 | Compliance logging, immutable event store |
| Notification Service | 8086 | Email/SMS alerts, event-driven |
| Ledger Service | 8087 | Double-entry accounting, journal entries |

---

## Cloud-Ready Design

This system is designed with production deployment principles:

- Stateless microservices
- Docker containerization
- Environment-based configuration
- Service isolation for independent scaling
- Event-driven architecture for async processing

---

## Transaction Flow (Saga Pattern)

```mermaid
sequenceDiagram
    participant C as Client
    participant GW as API Gateway
    participant TX as Transaction Service
    participant ACC as Account Service
    participant K as Kafka
    participant F as Fraud Service

    C->>+GW: POST /transfer (idempotency-key)
    GW->>GW: validate JWT
    GW->>+TX: forward
    TX->>TX: create PENDING record
    TX->>+K: publish DebitRequest
    K->>-ACC: consume
    ACC->>ACC: atomic debit (optimistic lock)
    ACC-->>K: DebitResponse
    K-->>-TX: response
    TX->>TX: CompletableFuture completes
    TX->>+K: publish CreditRequest
    K->>-ACC: consume
    ACC->>ACC: atomic credit
    ACC-->>K: CreditResponse
    K-->>-TX: response
    TX->>TX: mark COMPLETED
    TX->>+K: publish TransferCompletedEvent
    K->>F: fraud check (async)
    C-->>-C: 200 OK
```

---

## Consistency Model

- Uses ACID database transactions for atomicity
- Optimistic locking (version columns) for concurrency control
- Idempotency keys to prevent duplicate transactions
- Kafka-driven reconciler for saga failure recovery

---

## How to Run

### Local (Docker Compose)

```bash
docker-compose up --build
```

### Cloud Deployment (Render — Free Tier)

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/Raphasha27/smartbank-enterprise-platform)

Deploy via **one-click blueprint** — no CLI needed, no credit card required. Free tier sleeps after 15min idle, wakes on request, and never expires.

The `render.yaml` blueprint auto-provisions:

```yaml
services:
  - smartbank-gateway   # Docker, port 8080, health-checked
  - smartbank-auth      # Docker, port 8081, health-checked
  - smartbank-db        # PostgreSQL, free tier
```

After deploy:

```bash
curl https://your-service.onrender.com/actuator/health
```

---

## Deployment Architecture

```mermaid
graph LR
    REPO[GitHub Repo] -->|render.yaml blueprint| RENDER[Render.com]
    RENDER --> GW[Gateway Service<br/>:8080]
    RENDER --> AUTH[Auth Service<br/>:8081]
    RENDER --> PG[(PostgreSQL<br/>Free Tier)]
    GW --> AUTH
    GW -->|healthcheck| PG

    style REPO fill:#e3f2fd,stroke:#1565c0
    style RENDER fill:#f3e5f5,stroke:#6a1b9a
    style GW fill:#c8e6c9,stroke:#2e7d32
    style AUTH fill:#c8e6c9,stroke:#2e7d32
    style PG fill:#fff3e0,stroke:#e65100
```
---

## Tech Stack

- Java 21
- Spring Boot 3.4
- Spring Security
- PostgreSQL
- Docker
- Apache Kafka

---

## Author

**Kirov Dynamics Technology**  
GitHub: [github.com/Raphasha27](https://github.com/Raphasha27)
