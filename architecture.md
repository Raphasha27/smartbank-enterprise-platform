# SmartBank Enterprise Platform — Architecture

## High-Level System Diagram

```
Client (REST)
    │
    ▼
┌─────────────────────────────────────┐
│         API Gateway (:8080)         │
└────┬────┬────┬────┬────┬────┬──────┘
     │    │    │    │    │    │
     ▼    ▼    ▼    ▼    ▼    ▼
  ┌────┐┌────┐┌────┐┌────┐┌────┐┌──────┐
  │Auth││Acct││Txn ││Loan││Aud ││Notif │
  │:8081│:8082│:8083│:8084│:8085│:8086  │
  └──┬─┘└──┬─┘└──┬─┘└──┬─┘└──┬─┘└──┬───┘
     │     │     │     │     │     │
     ▼     ▼     ▼     ▼     ▼     ▼
  ┌────┐┌────┐┌────┐┌────┐┌────┐┌──────┐
  │ PG ││ PG ││ PG ││ PG ││ PG ││  PG  │
  └────┘└────┘└────┘└────┘└────┘└──────┘
                 │
                 ▼
            ┌──────────┐
            │  Kafka   │
            │ Event Bus │
            └────┬─────┘
                 │
         ┌───────┼───────┐
         ▼       ▼       ▼
      ┌────┐ ┌────┐ ┌──────┐
      │Fraud│ │Audit│ │Notif │
      │Rules│ │Log  │ │Alerts│
      └────┘ └────┘ └──────┘
```

## Data Flow

1. Client sends request to API Gateway
2. Gateway routes to appropriate service
3. Auth Service validates JWT token on every request
4. Service processes business logic against its PostgreSQL database
5. Transaction Service publishes events to Kafka/in-memory bus
6. Fraud, Audit, and Notification services consume events asynchronously

## Consistency Model

- ACID transactions within each service's database
- Idempotency keys prevent duplicate transaction processing
- Balance updates use atomic database operations with isolation levels
- Audit trail provides immutable record for reconciliation

## Security Layers

- **Authentication:** JWT with BCrypt password hashing
- **Authorization:** Role-based access control per endpoint
- **Transport:** All internal service communication through gateway
- **Audit:** Every financial operation logged immutably
