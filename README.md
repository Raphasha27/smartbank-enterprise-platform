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

Services:
- API Gateway
- Auth Service
- Account Service
- Transaction Service
- Fraud Detection Service
- Audit Service
- Ledger Service (double-entry accounting)
- Notification Service

Flow:
```
User → API Gateway → Auth → Transaction Service → Account Service → Event Bus → Fraud + Audit + Ledger
```

---

## Cloud-Ready Design

This system is designed with production deployment principles:

- Stateless microservices
- Docker containerization
- Environment-based configuration
- Service isolation for independent scaling
- Event-driven architecture for async processing

---

## Transaction Flow

1. User initiates transfer
2. API Gateway validates JWT
3. Transaction Service checks balance
4. Debit sender account
5. Credit receiver account
6. Commit DB transaction
7. Emit event to Fraud & Audit & Ledger services

---

## Consistency Model

- Uses ACID database transactions for atomicity
- Optimistic locking (version columns) for concurrency control
- Idempotency keys to prevent duplicate transactions
- Kafka-driven reconciler for saga failure recovery

---

## How to Run

```bash
docker-compose up --build
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
