# 🏦 SmartBank Enterprise Platform

[![CI](https://github.com/Raphasha27/smartbank-enterprise-platform/actions/workflows/ci.yml/badge.svg)](https://github.com/Raphasha27/smartbank-enterprise-platform/actions/workflows/ci.yml)

<div align="center">

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-a78bfa?style=for-the-badge)

**Production-grade event-driven banking platform built with 8 Spring Boot microservices**

[Architecture](#architecture) · [Services](#microservices) · [Quick Start](#quick-start) · [API Docs](#api-documentation)

</div>

---

## 🎯 Overview

SmartBank Enterprise Platform is a **next-generation core banking system** built on a microservices architecture. It demonstrates enterprise-grade patterns including event-driven communication via Apache Kafka, containerised deployment with Docker, and distributed data management with PostgreSQL.

> Built to showcase production-ready Java/Spring Boot engineering for financial systems — not a tutorial, a real implementation.

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    API Gateway / Load Balancer           │
└──────┬──────┬──────┬──────┬──────┬──────┬──────┬───────┘
       │      │      │      │      │      │      │
   [Auth] [KYC] [Loan] [Pay] [Core] [RegRep] [RTP] [Notify]
       │      │      │      │      │      │      │
       └──────┴──────┴──Kafka Event Bus───┴──────┘
                              │
                    ┌─────────┴──────────┐
                    │   PostgreSQL DBs    │
                    │  (per-service)      │
                    └────────────────────┘
```

---

## 📦 Microservices

| Service | Description | Port |
|---------|-------------|------|
| `secure-auth-service` | JWT authentication + MFA with Spring Security & BCrypt | 8081 |
| `kyc-platform` | Know Your Customer identity verification & compliance | 8082 |
| `loan-management-system` | Credit scoring, amortization, approval workflows | 8083 |
| `payment-gateway` | Authorization, capture, settlement processing | 8084 |
| `core-banking-system` | Accounts, deposits, withdrawals, fund transfers | 8085 |
| `regulatory-reporting-platform` | SAR/STR generation & audit trails | 8086 |
| `real-time-payments-platform` | Instant transfers via event-driven architecture | 8087 |
| `notification-service` | Email/SMS/push alerts via event consumption | 8088 |

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Apache Kafka (included in compose)

### Run All Services

```bash
git clone https://github.com/Raphasha27/smartbank-enterprise-platform.git
cd smartbank-enterprise-platform
docker-compose up --build
```

### Run Individual Service

```bash
cd secure-auth-service
./mvnw spring-boot:run
```

---

## 🔑 Key Features

- ✅ **Event-driven** — all inter-service communication via Kafka topics
- ✅ **JWT + MFA** — multi-factor authentication with refresh token rotation
- ✅ **KYC Verification** — identity & compliance checks before account activation
- ✅ **Real-time payments** — sub-second transfer processing with idempotency
- ✅ **Regulatory reporting** — automated SAR/STR generation with full audit trails
- ✅ **Containerised** — full Docker Compose orchestration, production-ready
- ✅ **Per-service databases** — complete data isolation, no shared schemas

---

## 📋 API Documentation

Each service exposes Swagger UI at `http://localhost:{port}/swagger-ui.html`

| Service | Swagger URL |
|---------|-------------|
| Auth | http://localhost:8081/swagger-ui.html |
| KYC | http://localhost:8082/swagger-ui.html |
| Loans | http://localhost:8083/swagger-ui.html |
| Payments | http://localhost:8084/swagger-ui.html |

---

## 🗺️ Roadmap

- [ ] Kubernetes Helm charts for cloud deployment
- [ ] OpenTelemetry distributed tracing
- [ ] gRPC inter-service communication
- [ ] CQRS + Event Sourcing pattern implementation
- [ ] React dashboard for transaction monitoring

---

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) and open an issue before submitting a PR.

---

## 📄 License

MIT License — see [LICENSE](LICENSE) for details.

---

<div align="center">
Built by <a href="https://github.com/Raphasha27">Koketso Raphasha</a> · <a href="https://portfolio-iota-eight-90.vercel.app/">Portfolio</a>
</div>
