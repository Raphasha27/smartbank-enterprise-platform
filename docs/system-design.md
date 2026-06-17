# System Architecture — SmartBank

## High-Level Design

```mermaid
sequenceDiagram
    participant C as Client
    participant GW as API Gateway
    participant AUTH as Auth Service
    participant TX as Transaction Service
    participant ACC as Account Service
    participant K as Kafka
    participant F as Fraud Service
    participant AUD as Audit Service
    participant L as Ledger Service
    participant N as Notification Service

    C->>+GW: POST /api/v1/auth/login
    GW->>+AUTH: validate credentials
    AUTH-->>-GW: JWT token
    GW-->>-C: 200 OK + token

    C->>+GW: POST /api/v1/transfers (JWT)
    GW->>+AUTH: validate JWT
    AUTH-->>-GW: user context
    GW->>+TX: forward request + idempotency-key

    TX->>+K: publish DebitRequest
    K->>-ACC: consume DebitRequest
    ACC->>ACC: atomic UPDATE ... WHERE version = ? AND balance >= ?
    ACC-->>-K: publish DebitResponse

    K-->>-TX: consume DebitResponse
    TX->>TX: CompletableFuture completes

    TX->>+K: publish CreditRequest
    K->>-ACC: consume CreditRequest
    ACC->>ACC: atomic UPDATE credit
    ACC-->>-K: publish CreditResponse

    alt success
        TX->>TX: mark COMPLETED
        TX->>+K: publish TransferCompletedEvent
        K->>F: fraud check (async)
        K->>AUD: audit log (async)
        K->>L: ledger entry (async)
        K->>N: notification (async)
    else timeout
        TX->>TX: mark PENDING_REVERSAL
        TX->>+K: publish ReversalEvent
        K->>RECON: reconciler reverses debit
    end
```

## Design Principles

- Microservices architecture
- Event-driven communication
- Strong consistency for transactions
- Asynchronous processing for logs and fraud detection
- Horizontal scalability support
- Containerized deployment
