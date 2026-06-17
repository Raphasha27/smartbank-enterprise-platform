# SmartBank Enterprise Platform — Architecture

## High-Level Architecture

```
Client (REST)
    │
    ▼
┌─────────────────────────────────────┐
│         API Gateway (:8080)         │
│  JWT validation · Routing · Rate    │
│  limiting · User context injection   │
└────┬────┬────┬────┬────┬────┬──────┘
     │    │    │    │    │    │    │
     ▼    ▼    ▼    ▼    ▼    ▼    ▼
  ┌────┐┌────┐┌────┐┌────┐┌────┐┌────┐┌──────┐
  │Auth││Acct││Txn ││Loan││Aud ││Notif││Ledger│
  │:8081│:8082│:8083│:8084│:8085│:8086│:8087  │
  └──┬─┘└──┬─┘└──┬─┘└──┬─┘└──┬─┘└──┬─┘└──┬───┘
     │     │     │     │     │     │     │
     ▼     ▼     ▼     ▼     ▼     ▼     ▼
  ┌────┐┌────┐┌────┐┌────┐┌────┐┌────┐┌──────┐
  │ PG ││ PG ││ PG ││ PG ││ PG ││ PG ││  PG  │
  └────┘└────┘└────┘└────┘└────┘└────┘└──────┘
                 │
                 ▼
            ┌──────────┐
            │  Kafka   │
            │Event Bus │
            └────┬─────┘
                 │
         ┌───────┼───────┐───────┐
         ▼       ▼       ▼       ▼
      ┌────┐ ┌────┐ ┌──────┐ ┌────┐
      │Fraud│ │Audit│ │Ledger│ │Notif│
      │Rules│ │Log  │ │Entry │ │Alert│
      └────┘ └────┘ └──────┘ └────┘
```

## Design Principles

- **Separation of concerns** — each service owns a single domain (auth, accounts, transactions, etc.)
- **Database-per-service** — each service has its own PostgreSQL database, preventing schema coupling
- **Stateless services** — no in-memory session state; all services scale horizontally
- **Event-driven communication** — Kafka decouples producers from consumers for resilience
- **Defense in depth** — authentication at the Gateway, authorization at every service

## Data Flow: Transfer

```
1. Client POST /transfers (JWT + Idempotency-Key)
2. Gateway validates JWT, injects X-User-Id, routes to Transaction Service
3. Transaction Service checks idempotency key → skip if duplicate
4. Transaction publishes DebitRequest (Kafka, keyed by accountId)
5. Account Service consumes, executes atomic UPDATE ... WHERE version=?
6. Account publishes DebitResponse (success/failure)
7. If debit succeeded → Transaction publishes CreditRequest
8. Account Service credits destination account
9. Transaction marks status = COMPLETED, publishes TransferEvent
10. Kafka consumers: Audit (log), Fraud (evaluate), Ledger (journal entry), Notification (alert)
```

## Consistency Strategy

- **Within a service** — local ACID transactions
- **Across services** — saga pattern: debit → credit → complete. If credit fails → compensating reversal
- **Concurrency** — optimistic locking via version column on `accounts` table
- **Idempotency** — `Idempotency-Key` header prevents duplicate transfers
- **Reconciler** — Kafka-driven consumer resolves `PENDING_REVERSAL` states within seconds
- **Trade-off** — eventual consistency between debit and credit (window ~seconds). Acceptable because reconciler guarantees correctness and the debit is irreversible.

## Security Layers

| Layer | Mechanism |
|-------|-----------|
| Edge | API Gateway validates JWT signature + expiry |
| Service | `X-User-Id` + `X-User-Roles` headers checked per operation |
| Account | Balance mutations guarded by `WHERE balance >= amount` |
| Audit | Immutable append-only log, no updates or deletes |
| Transport | All inter-service calls go through Gateway (network policy) |

## Monitoring & Observability

- **Health** — Spring Boot Actuator (`/actuator/health`) on every service
- **Metrics** — `/actuator/metrics` for JVM, thread pools, DB connections
- **Tracing** — OpenTelemetry + Zipkin for distributed traces across all services
- **Audit** — Immutable service-level audit log for compliance
