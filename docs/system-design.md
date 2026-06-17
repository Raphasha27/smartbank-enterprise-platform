# System Architecture — SmartBank

## High-Level Design

```
Client
  ↓
API Gateway
  ↓
Auth Service
  ↓
Transaction Service
  ↓
Account Service
  ↓
Event Bus (Kafka)
  ↓
Fraud Service + Audit Service + Ledger Service + Notification Service
```

---

## Design Principles

- Microservices architecture
- Event-driven communication
- Strong consistency for transactions
- Asynchronous processing for logs and fraud detection
- Horizontal scalability support
- Containerized deployment
