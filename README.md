# SmartBank Enterprise Platform

Enterprise-grade banking backend system built with Java Spring Boot microservices.

## Architecture

```
API Gateway (8080)
  ├── Auth Service (8081) — JWT authentication & user management
  ├── Account Service (8082) — Account creation, balances, deposits/withdrawals
  ├── Transaction Service (8083) — Money transfers, transaction history
  ├── Loan Service (8084) — Loan applications, approval workflow
  ├── Audit Service (8085) — Immutable audit trail for compliance
  └── Notification Service (8086) — Transaction alerts, event notifications
```

## Tech Stack

- **Java 21** + Spring Boot 3.4
- Spring Security + JWT authentication
- Spring Data JPA + Hibernate
- PostgreSQL (per-service database)
- Spring Cloud Gateway (API routing)
- Docker Compose (orchestration)
- Maven (multi-module build)

## Quick Start

```bash
# Build all services
mvn clean package -DskipTests

# Start everything
docker compose up -d

# Verify health
curl http://localhost:8080/auth/health
curl http://localhost:8080/accounts/health
curl http://localhost:8080/transactions/health
```

## API Endpoints

### Auth Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a new user (BCrypt hashed) |
| POST | `/auth/login` | Login, returns JWT token |

### Account Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/accounts` | Create account |
| GET | `/accounts/{id}` | Get account by ID |
| GET | `/accounts/user/{userId}` | Get user's accounts |
| PUT | `/accounts/{id}/balance?delta=X` | Update balance |

### Transaction Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/transactions/transfer` | Transfer money between accounts |
| GET | `/transactions/account/{id}` | Get account transactions |

### Loan Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/loans` | Apply for loan |
| GET | `/loans/{id}` | Get loan details |
| POST | `/loans/{id}/approve` | Approve loan |

### Audit Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/audit/logs` | Create audit log entry |
| GET | `/audit/logs` | Get all audit logs |
| GET | `/audit/logs/user/{email}` | Get user audit trail |

### Notification Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/notifications` | Send notification |
| GET | `/notifications/user/{email}` | Get user notifications |
| GET | `/notifications/user/{email}/unread` | Get unread notifications |

## Event-Driven Architecture

Each transaction publishes an event that triggers:
- Fraud rule evaluation
- Audit log creation
- User notification delivery

## Building

```bash
mvn clean package -DskipTests
```

## Built by Kirov Dynamics Technology

Cybersecurity + AI Engineering Systems

[GitHub](https://github.com/Raphasha27)
